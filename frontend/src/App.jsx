import React, { useState } from 'react';
import { LayoutDashboard, FileCode2, Cpu, LogOut, User } from 'lucide-react';
import Dashboard from './components/Dashboard';
import Requirements from './components/Requirements';
import Auth from './components/Auth';

export default function App() {
  const [token, setToken] = useState(localStorage.getItem('qualixa_token'));
  const [username, setUsername] = useState(localStorage.getItem('qualixa_username'));
  const [activeTab, setActiveTab] = useState('dashboard');

  const handleLoginSuccess = (newToken, newUsername) => {
    localStorage.setItem('qualixa_token', newToken);
    localStorage.setItem('qualixa_username', newUsername);
    setToken(newToken);
    setUsername(newUsername);
  };

  const handleLogout = () => {
    localStorage.removeItem('qualixa_token');
    localStorage.removeItem('qualixa_username');
    setToken(null);
    setUsername(null);
  };

  if (!token) {
    return <Auth onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <div className="app-container">
      {/* Sidebar Navigation */}
      <aside className="sidebar">
        <div className="d-flex align-items-center gap-3 mb-5 px-2">
          <div className="rounded-3 p-2 d-flex align-items-center justify-content-center" style={{ background: 'linear-gradient(135deg, var(--accent-primary) 0%, var(--accent-info) 100%)', color: '#ffffff' }}>
            <Cpu size={24} />
          </div>
          <div>
            <h4 className="text-white mb-0 font-display font-weight-bold" style={{ letterSpacing: '0.5px' }}>QUALIXA</h4>
            <span className="text-secondary font-monospace" style={{ fontSize: '10px', textTransform: 'uppercase', letterSpacing: '1px' }}>AI-Powered Studio</span>
          </div>
        </div>

        <nav className="d-flex flex-column gap-2 flex-grow-1">
          <button
            className={`nav-item-btn ${activeTab === 'dashboard' ? 'active' : ''}`}
            onClick={() => setActiveTab('dashboard')}
          >
            <LayoutDashboard size={20} />
            Dashboard
          </button>
          <button
            className={`nav-item-btn ${activeTab === 'requirements' ? 'active' : ''}`}
            onClick={() => setActiveTab('requirements')}
          >
            <FileCode2 size={20} />
            QA Studio
          </button>
        </nav>

        <div className="border-top border-secondary pt-3 px-2 d-flex flex-column gap-3">
          <div className="d-flex align-items-center justify-content-between text-light">
            <div className="d-flex align-items-center gap-2 text-truncate">
              <div className="rounded-circle bg-dark border border-secondary d-flex align-items-center justify-content-center" style={{ width: '32px', height: '32px', flexShrink: 0 }}>
                <User size={16} className="text-secondary" />
              </div>
              <span className="text-truncate font-display" style={{ fontSize: '13px', fontWeight: '500' }}>
                {username}
              </span>
            </div>
            <button 
              onClick={handleLogout}
              className="btn btn-link text-secondary p-1 border-0 d-flex align-items-center"
              title="Logout"
              style={{ textDecoration: 'none', cursor: 'pointer' }}
            >
              <LogOut size={16} />
            </button>
          </div>
          <div className="d-flex align-items-center gap-2 text-secondary font-monospace" style={{ fontSize: '11px' }}>
            <span className="d-inline-block rounded-circle bg-success" style={{ width: '8px', height: '8px' }}></span>
            <span>API Status: Online</span>
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="main-content">
        {activeTab === 'dashboard' ? <Dashboard /> : <Requirements />}
      </main>
    </div>
  );
}
