import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { FileText, CheckCircle2, XCircle, HelpCircle, Clock, Calendar, Eye } from 'lucide-react';
import ExecutionResultModal from './ExecutionResultModal';

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedExec, setSelectedExec] = useState(null);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const data = await api.getDashboard();
      setStats(data);
      setError(null);
    } catch (err) {
      console.error(err);
      setError('Failed to load dashboard metrics. Ensure the backend is running.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '300px' }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading Dashboard...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger" role="alert">
        {error}
      </div>
    );
  }

  return (
    <div className="animated-fade-in">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1 className="text-white mb-1">Quality Assurance Dashboard</h1>
          <p className="text-secondary">Overview of test cases execution state and automated runs.</p>
        </div>
        <button className="btn btn-premium-outline" onClick={fetchStats}>Refresh Metrics</button>
      </div>

      {/* Stats Cards */}
      <div className="row g-4 mb-5">
        <div className="col-12 col-md-6 col-lg-3">
          <div className="premium-card h-100 d-flex align-items-center justify-content-between">
            <div>
              <p className="text-secondary mb-1 font-display">Total Test Cases</p>
              <h2 className="text-white mb-0 display-font">{stats?.totalTestCases || 0}</h2>
            </div>
            <div className="rounded-3 p-3" style={{ background: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' }}>
              <FileText size={24} />
            </div>
          </div>
        </div>

        <div className="col-12 col-md-6 col-lg-3">
          <div className="premium-card h-100 d-flex align-items-center justify-content-between" style={{ borderLeft: '3px solid var(--accent-success)' }}>
            <div>
              <p className="text-secondary mb-1 font-display">Passed (Latest Run)</p>
              <h2 className="text-white mb-0 display-font">{stats?.passedTestCases || 0}</h2>
            </div>
            <div className="rounded-3 p-3" style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#10b981' }}>
              <CheckCircle2 size={24} />
            </div>
          </div>
        </div>

        <div className="col-12 col-md-6 col-lg-3">
          <div className="premium-card h-100 d-flex align-items-center justify-content-between" style={{ borderLeft: '3px solid var(--accent-danger)' }}>
            <div>
              <p className="text-secondary mb-1 font-display">Failed (Latest Run)</p>
              <h2 className="text-white mb-0 display-font">{stats?.failedTestCases || 0}</h2>
            </div>
            <div className="rounded-3 p-3" style={{ background: 'rgba(244, 63, 94, 0.15)', color: '#f43f5e' }}>
              <XCircle size={24} />
            </div>
          </div>
        </div>

        <div className="col-12 col-md-6 col-lg-3">
          <div className="premium-card h-100 d-flex align-items-center justify-content-between" style={{ borderLeft: '3px solid var(--accent-warning)' }}>
            <div>
              <p className="text-secondary mb-1 font-display">Untested Cases</p>
              <h2 className="text-white mb-0 display-font">{stats?.untestedTestCases || 0}</h2>
            </div>
            <div className="rounded-3 p-3" style={{ background: 'rgba(245, 158, 11, 0.15)', color: '#f59e0b' }}>
              <HelpCircle size={24} />
            </div>
          </div>
        </div>
      </div>

      {/* Recent Executions */}
      <div className="premium-card">
        <h3 className="text-white mb-4">Recent Test Executions</h3>
        {(!stats?.recentExecutions || stats.recentExecutions.length === 0) ? (
          <div className="text-center py-5">
            <HelpCircle size={48} className="text-secondary mb-3" />
            <p className="text-secondary mb-0">No recent test runs detected. Select a test case in Automation Studio and trigger a run.</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table className="table premium-table">
              <thead>
                <tr>
                  <th>Execution ID</th>
                  <th>Test Case ID</th>
                  <th>Status</th>
                  <th>Duration</th>
                  <th>Executed At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {stats.recentExecutions.map((exec) => (
                  <tr key={exec.id}>
                    <td className="text-white font-monospace">#{exec.id}</td>
                    <td>
                      <span className="badge bg-secondary">TC #{exec.testCaseId}</span>
                    </td>
                    <td>
                      <span className={`badge-status ${exec.status === 'PASSED' ? 'badge-passed' : 'badge-failed'}`}>
                        {exec.status === 'PASSED' ? <CheckCircle2 size={12} /> : <XCircle size={12} />}
                        {exec.status}
                      </span>
                    </td>
                    <td>
                      <div className="d-flex align-items-center gap-2 text-secondary">
                        <Clock size={14} />
                        {exec.executionTimeMs} ms
                      </div>
                    </td>
                    <td>
                      <div className="d-flex align-items-center gap-2 text-secondary">
                        <Calendar size={14} />
                        {new Date(exec.executedAt).toLocaleString()}
                      </div>
                    </td>
                    <td>
                      <button 
                        className="btn btn-premium-outline btn-sm d-flex align-items-center gap-2"
                        onClick={() => setSelectedExec(exec)}
                      >
                        <Eye size={14} />
                        View Report
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Result Modal */}
      {selectedExec && (
        <ExecutionResultModal 
          execution={selectedExec} 
          onClose={() => setSelectedExec(null)} 
        />
      )}
    </div>
  );
}
