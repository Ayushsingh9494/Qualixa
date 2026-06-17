import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { Wand2, Code, Play, ChevronDown, ChevronUp, CheckCircle2, XCircle, Clock, Eye, AlertCircle } from 'lucide-react';
import ExecutionResultModal from './ExecutionResultModal';

export default function TestCaseDetails({ requirementId }) {
  const [testCases, setTestCases] = useState([]);
  const [loading, setLoading] = useState(true);
  const [generatingCases, setGeneratingCases] = useState(false);
  const [expandedId, setExpandedId] = useState(null);

  // Script & Execution states keyed by testCaseId
  const [scripts, setScripts] = useState({});
  const [loadingScripts, setLoadingScripts] = useState({});
  const [executing, setExecuting] = useState({});
  const [executions, setExecutions] = useState({});

  // Active result modal
  const [viewExec, setViewExec] = useState(null);

  useEffect(() => {
    if (requirementId) {
      fetchTestCases();
    }
  }, [requirementId]);

  const fetchTestCases = async () => {
    try {
      setLoading(true);
      const data = await api.getTestCases(requirementId);
      setTestCases(data);
      setExpandedId(null);
      // Reset script and run caches
      setScripts({});
      setExecutions({});
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateTestCases = async () => {
    try {
      setGeneratingCases(true);
      const data = await api.generateTestCases(requirementId);
      setTestCases(data);
      setExpandedId(null);
    } catch (err) {
      console.error(err);
      alert('Failed to generate test cases. Please verify your GEMINI_API_KEY.');
    } finally {
      setGeneratingCases(false);
    }
  };

  const handleExpandTestCase = async (tcId) => {
    if (expandedId === tcId) {
      setExpandedId(null);
      return;
    }
    setExpandedId(tcId);
    // If we haven't loaded the script code for this test case, load it
    if (!scripts[tcId]) {
      fetchScript(tcId);
    }
    // Fetch recent run if we haven't loaded it
    if (!executions[tcId]) {
      fetchLatestExecution(tcId);
    }
  };

  const fetchScript = async (tcId) => {
    try {
      setLoadingScripts(prev => ({ ...prev, [tcId]: true }));
      const scriptData = await api.getScript(tcId);
      setScripts(prev => ({ ...prev, [tcId]: scriptData }));
    } catch (err) {
      // Script might not exist yet, which is fine
      setScripts(prev => ({ ...prev, [tcId]: null }));
    } finally {
      setLoadingScripts(prev => ({ ...prev, [tcId]: false }));
    }
  };

  const handleGenerateScript = async (tcId) => {
    try {
      setLoadingScripts(prev => ({ ...prev, [tcId]: true }));
      const scriptData = await api.generateScript(tcId);
      setScripts(prev => ({ ...prev, [tcId]: scriptData }));
    } catch (err) {
      console.error(err);
      alert('Failed to generate Selenium script code.');
    } finally {
      setLoadingScripts(prev => ({ ...prev, [tcId]: false }));
    }
  };

  const fetchLatestExecution = async (tcId) => {
    try {
      const runs = await api.getExecutions(tcId);
      if (runs.length > 0) {
        setExecutions(prev => ({ ...prev, [tcId]: runs[0] }));
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleExecuteTest = async (tcId) => {
    try {
      setExecuting(prev => ({ ...prev, [tcId]: true }));
      const result = await api.executeTest(tcId);
      setExecutions(prev => ({ ...prev, [tcId]: result }));
    } catch (err) {
      console.error(err);
      alert('Test script execution encountered an error.');
    } finally {
      setExecuting(prev => ({ ...prev, [tcId]: false }));
    }
  };

  if (loading) {
    return (
      <div className="premium-card text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading studio...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="premium-card">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-white mb-0">AI Test Studio</h3>
        <button
          className="btn btn-premium d-flex align-items-center gap-2"
          onClick={handleGenerateTestCases}
          disabled={generatingCases}
        >
          <Wand2 size={16} />
          {generatingCases ? 'Generating Cases...' : 'Generate AI Test Cases'}
        </button>
      </div>

      {testCases.length === 0 ? (
        <div className="text-center py-5 text-secondary border border-dashed border-secondary rounded">
          <Wand2 size={40} className="mb-3 text-secondary opacity-60" />
          <h5 className="text-light">No Test Cases Generated</h5>
          <p className="mb-0">Click the generate button above to create detailed automation test scenarios using Gemini.</p>
        </div>
      ) : (
        <div className="d-flex flex-column gap-3">
          {testCases.map((tc) => {
            const isExpanded = expandedId === tc.id;
            const script = scripts[tc.id];
            const isScriptLoading = loadingScripts[tc.id];
            const isExecuting = executing[tc.id];
            const execResult = executions[tc.id];

            return (
              <div 
                key={tc.id} 
                className="border rounded p-3 animated-fade-in" 
                style={{ 
                  background: isExpanded ? 'rgba(255, 255, 255, 0.02)' : 'transparent',
                  borderColor: isExpanded ? 'var(--accent-primary)' : 'var(--card-border)',
                  transition: 'all 0.2s ease'
                }}
              >
                {/* Header Collapsible Trigger */}
                <div 
                  className="d-flex justify-content-between align-items-center cursor-pointer"
                  onClick={() => handleExpandTestCase(tc.id)}
                  style={{ cursor: 'pointer' }}
                >
                  <div className="d-flex align-items-center gap-3">
                    <span className="badge bg-secondary font-monospace" style={{ fontSize: '13px' }}>{tc.testCaseId}</span>
                    <h5 className="text-white mb-0 font-display">{tc.title}</h5>
                  </div>
                  <div className="d-flex align-items-center gap-3">
                    {execResult && (
                      <span className={`badge-status ${execResult.status === 'PASSED' ? 'badge-passed' : 'badge-failed'}`} style={{ fontSize: '11px', padding: '3px 8px' }}>
                        {execResult.status}
                      </span>
                    )}
                    {isExpanded ? <ChevronUp size={18} className="text-secondary" /> : <ChevronDown size={18} className="text-secondary" />}
                  </div>
                </div>

                {/* Expanded Section Details */}
                {isExpanded && (
                  <div className="mt-4 pt-3 border-top border-secondary animated-fade-in">
                    <div className="row g-4 mb-4">
                      <div className="col-12 col-md-6">
                        <h6 className="text-secondary font-display mb-2">Preconditions:</h6>
                        <p className="text-light bg-dark p-2 rounded border border-secondary" style={{ fontSize: '14px' }}>
                          {tc.preconditions || 'None'}
                        </p>
                      </div>
                      <div className="col-12 col-md-6">
                        <h6 className="text-secondary font-display mb-2">Expected Result:</h6>
                        <p className="text-light bg-dark p-2 rounded border border-secondary" style={{ fontSize: '14px' }}>
                          {tc.expectedResult}
                        </p>
                      </div>
                    </div>

                    <div className="mb-4">
                      <h6 className="text-secondary font-display mb-2">Execution Steps:</h6>
                      <ol className="list-group list-group-numbered bg-transparent border-0">
                        {tc.steps?.map((step, idx) => (
                          <li key={idx} className="list-group-item bg-transparent text-light border-0 py-1" style={{ fontSize: '14px' }}>
                            {step}
                          </li>
                        ))}
                      </ol>
                    </div>

                    {/* Selenium Script Panel */}
                    <div className="border border-secondary rounded p-3 mb-3" style={{ background: 'rgba(0,0,0,0.2)' }}>
                      <div className="d-flex justify-content-between align-items-center mb-3">
                        <h6 className="text-white mb-0 font-display d-flex align-items-center gap-2">
                          <Code size={16} className="text-primary" /> Selenium Script
                        </h6>
                        <button
                          className="btn btn-premium-outline btn-sm"
                          onClick={() => handleGenerateScript(tc.id)}
                          disabled={isScriptLoading}
                        >
                          {script ? 'Regenerate Code' : 'Generate Code'}
                        </button>
                      </div>

                      {isScriptLoading ? (
                        <div className="text-center py-4">
                          <div className="spinner-border spinner-border-sm text-primary" role="status"></div>
                        </div>
                      ) : script ? (
                        <div>
                          <pre className="code-container mb-3">{script.scriptCode}</pre>
                          
                          {/* Test Runner Buttons */}
                          <div className="d-flex align-items-center justify-content-between">
                            <button
                              className="btn btn-premium-success btn-sm d-flex align-items-center gap-2"
                              onClick={() => handleExecuteTest(tc.id)}
                              disabled={isExecuting}
                            >
                              <Play size={14} />
                              {isExecuting ? 'Running Test...' : 'Run Automation Script'}
                            </button>

                            {execResult && (
                              <div className="d-flex align-items-center gap-3">
                                <span className={`badge-status ${execResult.status === 'PASSED' ? 'badge-passed' : 'badge-failed'}`}>
                                  {execResult.status === 'PASSED' ? <CheckCircle2 size={12} /> : <XCircle size={12} />}
                                  {execResult.status}
                                </span>
                                <span className="text-secondary font-monospace" style={{ fontSize: '13px' }}>
                                  <Clock size={12} className="me-1" />
                                  {execResult.executionTimeMs} ms
                                </span>
                                <button 
                                  className="btn btn-premium-outline btn-sm d-flex align-items-center gap-1"
                                  onClick={() => setViewExec(execResult)}
                                >
                                  <Eye size={12} /> View Report
                                </button>
                              </div>
                            )}
                          </div>
                        </div>
                      ) : (
                        <div className="text-center py-3 text-secondary font-monospace" style={{ fontSize: '13px' }}>
                          No code generated. Click 'Generate Code' to translate this test scenario into clean TestNG Selenium script.
                        </div>
                      )}
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}

      {/* Execution modal */}
      {viewExec && (
        <ExecutionResultModal 
          execution={viewExec} 
          onClose={() => setViewExec(null)} 
        />
      )}
    </div>
  );
}
