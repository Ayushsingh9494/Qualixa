import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { Plus, ListCollapse, ChevronRight, FileText } from 'lucide-react';
import TestCaseDetails from './TestCaseDetails';

export default function Requirements() {
  const [requirements, setRequirements] = useState([]);
  const [selectedReq, setSelectedReq] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Form State
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);

  useEffect(() => {
    fetchRequirements();
  }, []);

  const fetchRequirements = async () => {
    try {
      setLoading(true);
      const data = await api.getRequirements();
      setRequirements(data);
      if (data.length > 0 && !selectedReq) {
        setSelectedReq(data[0]);
      }
      setError(null);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch requirements. Make sure the backend API is online.');
    } finally {
      setLoading(false);
    }
  };

  const handleAddRequirement = async (e) => {
    e.preventDefault();
    if (!title.trim() || !description.trim()) return;

    try {
      setSubmitting(true);
      const newReq = await api.createRequirement({ title, description });
      setRequirements([newReq, ...requirements]);
      setSelectedReq(newReq);
      setTitle('');
      setDescription('');
      setShowAddForm(false);
    } catch (err) {
      console.error(err);
      alert('Failed to save requirement.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading && requirements.length === 0) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '300px' }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading Requirements...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="animated-fade-in">
      <div className="row g-4">
        {/* Left Column - List and Creation */}
        <div className="col-12 col-lg-4">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h2 className="text-white mb-0">Requirements</h2>
            <button 
              className={`btn btn-sm ${showAddForm ? 'btn-premium-outline' : 'btn-premium'}`}
              onClick={() => setShowAddForm(!showAddForm)}
            >
              {showAddForm ? 'Cancel' : <><Plus size={16} className="me-1" /> New</>}
            </button>
          </div>

          {/* Add Form */}
          {showAddForm && (
            <div className="premium-card mb-4 animated-fade-in">
              <h4 className="text-white mb-3">Add Requirement</h4>
              <form onSubmit={handleAddRequirement}>
                <div className="mb-3">
                  <label className="form-label text-secondary font-display">Requirement Title</label>
                  <input 
                    type="text" 
                    className="form-control bg-dark border-secondary text-white"
                    placeholder="e.g. User Authentication"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label text-secondary font-display">Description (Plain English)</label>
                  <textarea 
                    className="form-control bg-dark border-secondary text-white" 
                    rows="4"
                    placeholder="e.g. As a registered user, I should be able to input my email and password in order to authenticate..."
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                  ></textarea>
                </div>
                <button type="submit" className="btn btn-premium w-100" disabled={submitting}>
                  {submitting ? 'Saving...' : 'Save Requirement'}
                </button>
              </form>
            </div>
          )}

          {/* List Card */}
          <div className="premium-card" style={{ maxHeight: '70vh', overflowY: 'auto' }}>
            {error && <div className="alert alert-danger">{error}</div>}
            {requirements.length === 0 ? (
              <div className="text-center py-4 text-secondary">
                <ListCollapse size={36} className="mb-2" />
                <p className="mb-0">No requirements found.</p>
              </div>
            ) : (
              <div className="d-flex flex-column gap-2">
                {requirements.map((req) => (
                  <button
                    key={req.id}
                    className={`nav-item-btn ${selectedReq?.id === req.id ? 'active' : ''}`}
                    onClick={() => setSelectedReq(req)}
                    style={{ justifyContent: 'space-between' }}
                  >
                    <div className="d-flex align-items-center gap-2 text-truncate">
                      <FileText size={18} />
                      <span className="text-truncate">{req.title}</span>
                    </div>
                    <ChevronRight size={16} />
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Right Column - Details and Nested Test Cases */}
        <div className="col-12 col-lg-8">
          {selectedReq ? (
            <div className="d-flex flex-column gap-4">
              {/* Selected Details Header */}
              <div className="premium-card">
                <span className="badge bg-secondary mb-2">Requirement ID: #{selectedReq.id}</span>
                <h2 className="text-white mb-3">{selectedReq.title}</h2>
                <div className="bg-dark p-3 rounded border border-secondary" style={{ whiteSpace: 'pre-wrap' }}>
                  <p className="text-light mb-0" style={{ fontSize: '15px', lineHeight: '1.6' }}>
                    {selectedReq.description}
                  </p>
                </div>
              </div>

              {/* Automation Workspace Container */}
              <TestCaseDetails requirementId={selectedReq.id} />
            </div>
          ) : (
            <div className="premium-card d-flex flex-column justify-content-center align-items-center" style={{ minHeight: '350px' }}>
              <FileText size={48} className="text-secondary mb-3" />
              <h3 className="text-white">No Requirement Selected</h3>
              <p className="text-secondary">Please add or select a requirement on the left panel to begin test case generation.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
