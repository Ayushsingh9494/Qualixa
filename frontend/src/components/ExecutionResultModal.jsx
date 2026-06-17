import React from 'react';
import { X, CheckCircle2, XCircle, Clock, Calendar, AlertTriangle } from 'lucide-react';

export default function ExecutionResultModal({ execution, onClose }) {
  if (!execution) return null;

  return (
    <div 
      className="modal fade show d-block modal-glass" 
      tabIndex="-1" 
      style={{ backgroundColor: 'rgba(0, 0, 0, 0.7)', zIndex: 1050 }}
    >
      <div className="modal-dialog modal-lg modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header d-flex justify-content-between align-items-center">
            <h5 className="modal-title text-white font-display">Test Run Execution Report</h5>
            <button 
              type="button" 
              className="btn-close btn-close-white" 
              onClick={onClose}
              aria-label="Close"
            ></button>
          </div>

          <div className="modal-body" style={{ maxHeight: '75vh', overflowY: 'auto' }}>
            {/* Status Header */}
            <div className="d-flex align-items-center justify-content-between mb-4 bg-dark p-3 rounded border border-secondary">
              <div className="d-flex align-items-center gap-3">
                <span className={`badge-status ${execution.status === 'PASSED' ? 'badge-passed' : 'badge-failed'}`}>
                  {execution.status === 'PASSED' ? <CheckCircle2 size={14} /> : <XCircle size={14} />}
                  {execution.status}
                </span>
                <span className="text-secondary font-monospace">Run #{execution.id}</span>
              </div>
              <div className="d-flex gap-4 text-secondary font-monospace" style={{ fontSize: '13px' }}>
                <span className="d-flex align-items-center gap-1">
                  <Clock size={14} />
                  {execution.executionTimeMs} ms
                </span>
                <span className="d-flex align-items-center gap-1">
                  <Calendar size={14} />
                  {new Date(execution.executedAt).toLocaleString()}
                </span>
              </div>
            </div>

            {/* Error Message if FAILED */}
            {execution.status === 'FAILED' && execution.errorMessage && (
              <div className="alert alert-danger d-flex gap-3 align-items-start mb-4" role="alert">
                <AlertTriangle size={20} className="flex-shrink-0 mt-1" />
                <div>
                  <h6 className="alert-heading font-display font-weight-bold">Failure Log Detail:</h6>
                  <p className="mb-0 font-monospace" style={{ fontSize: '13px', whiteSpace: 'pre-wrap' }}>
                    {execution.errorMessage}
                  </p>
                </div>
              </div>
            )}

            {/* Screenshot Header */}
            <h6 className="text-secondary font-display mb-3">Final Execution Screenshot State:</h6>
            {execution.screenshotBase64 ? (
              <div className="border border-secondary rounded p-2 text-center bg-dark" style={{ overflow: 'hidden' }}>
                <img 
                  src={execution.screenshotBase64} 
                  alt="Execution Report Graphic" 
                  className="img-fluid rounded border" 
                  style={{ maxHeight: '380px', objectFit: 'contain', width: '100%' }}
                />
              </div>
            ) : (
              <div className="text-center py-4 border border-secondary border-dashed rounded text-secondary">
                No screenshot data recorded for this run.
              </div>
            )}
          </div>

          <div className="modal-footer">
            <button type="button" className="btn btn-premium-outline" onClick={onClose}>
              Close Report
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
