import React, { useState } from 'react';
import { api } from '../services/api';
import { Cpu, Mail, Lock, User, ArrowRight } from 'lucide-react';

export default function Auth({ onLoginSuccess }) {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      if (isLogin) {
        const data = await api.login(email, password);
        onLoginSuccess(data.token, data.username, data.email);
      } else {
        const data = await api.register(username, email, password);
        onLoginSuccess(data.token, data.username, data.email);
      }
    } catch (err) {
      console.error(err);
      if (err.response && err.response.data) {
        setError(typeof err.response.data === 'string' ? err.response.data : 'Authentication failed.');
      } else {
        setError('Connection failed. Make sure the backend is online.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrapper d-flex align-items-center justify-content-center" style={{ minHeight: '100vh', background: 'radial-gradient(circle at top right, #1f4068 0%, #16213e 50%, #1a1a2e 100%)' }}>
      <div className="auth-card p-5 rounded-4 animated-fade-in" style={{ width: '100%', maxWidth: '440px', background: 'rgba(22, 33, 62, 0.7)', backdropFilter: 'blur(12px)', border: '1px solid rgba(255, 255, 255, 0.08)', boxShadow: '0 20px 40px rgba(0,0,0,0.3)' }}>
        
        {/* App Logo & Header */}
        <div className="text-center mb-5">
          <div className="d-inline-flex rounded-3 p-3 mb-3" style={{ background: 'linear-gradient(135deg, var(--accent-primary) 0%, var(--accent-info) 100%)', color: '#ffffff' }}>
            <Cpu size={32} className="animated-pulse" />
          </div>
          <h2 className="text-white font-display font-weight-bold mb-1" style={{ letterSpacing: '1px' }}>QUALIXA</h2>
          <p className="text-secondary font-monospace" style={{ fontSize: '11px', textTransform: 'uppercase', letterSpacing: '2px' }}>AI-Powered Test Studio</p>
        </div>

        <h3 className="text-white font-display mb-4 text-center">{isLogin ? 'Sign In' : 'Create Account'}</h3>

        {/* Error Alert */}
        {error && (
          <div className="alert alert-danger border-0 text-white font-display py-2 px-3 mb-4 rounded-3" style={{ backgroundColor: 'rgba(244, 67, 54, 0.15)', fontSize: '14px', borderLeft: '3px solid #f44336' }}>
            {error}
          </div>
        )}

        {/* Auth Form */}
        <form onSubmit={handleSubmit}>
          {!isLogin && (
            <div className="mb-3">
              <label className="form-label text-secondary font-display" style={{ fontSize: '13px' }}>Username</label>
              <div className="input-group">
                <span className="input-group-text bg-dark border-secondary border-end-0 text-secondary" style={{ borderTopLeftRadius: '8px', borderBottomLeftRadius: '8px' }}>
                  <User size={18} />
                </span>
                <input 
                  type="text" 
                  className="form-control bg-dark border-secondary text-white border-start-0 py-2"
                  style={{ borderTopRightRadius: '8px', borderBottomRightRadius: '8px' }}
                  placeholder="Choose username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
            </div>
          )}

          <div className="mb-3">
            <label className="form-label text-secondary font-display" style={{ fontSize: '13px' }}>Email Address</label>
            <div className="input-group">
              <span className="input-group-text bg-dark border-secondary border-end-0 text-secondary" style={{ borderTopLeftRadius: '8px', borderBottomLeftRadius: '8px' }}>
                <Mail size={18} />
              </span>
              <input 
                type="email" 
                className="form-control bg-dark border-secondary text-white border-start-0 py-2"
                style={{ borderTopRightRadius: '8px', borderBottomRightRadius: '8px' }}
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
          </div>

          <div className="mb-4">
            <label className="form-label text-secondary font-display" style={{ fontSize: '13px' }}>Password</label>
            <div className="input-group">
              <span className="input-group-text bg-dark border-secondary border-end-0 text-secondary" style={{ borderTopLeftRadius: '8px', borderBottomLeftRadius: '8px' }}>
                <Lock size={18} />
              </span>
              <input 
                type="password" 
                className="form-control bg-dark border-secondary text-white border-start-0 py-2"
                style={{ borderTopRightRadius: '8px', borderBottomRightRadius: '8px' }}
                placeholder="Enter password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn btn-premium w-100 py-2.5 rounded-3 d-flex align-items-center justify-content-center gap-2 mb-4 font-display font-weight-bold" 
            disabled={loading}
          >
            {loading ? (
              <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            ) : (
              <>
                {isLogin ? 'Sign In' : 'Sign Up'} <ArrowRight size={18} />
              </>
            )}
          </button>
        </form>

        {/* View Toggle */}
        <div className="text-center mt-3">
          <span className="text-secondary font-display" style={{ fontSize: '14px' }}>
            {isLogin ? "Don't have an account? " : "Already have an account? "}
          </span>
          <button 
            className="btn btn-link text-white p-0 border-0 font-display font-weight-bold" 
            style={{ fontSize: '14px', textDecoration: 'none', transition: 'color 0.2s' }}
            onClick={() => {
              setIsLogin(!isLogin);
              setError(null);
            }}
          >
            {isLogin ? 'Register' : 'Login'}
          </button>
        </div>

      </div>
    </div>
  );
}
