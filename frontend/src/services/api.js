import axios from 'axios';

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const client = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor to inject session token into headers
client.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('qualixa_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const api = {
  // Auth
  register: (username, email, password) => 
    client.post('/auth/register', { username, email, password }).then(r => r.data),
    
  login: (email, password) => 
    client.post('/auth/login', { email, password }).then(r => r.data),

  // Requirements
  getRequirements: () => client.get('/requirements').then(r => r.data),
  getRequirement: (id) => client.get(`/requirements/${id}`).then(r => r.data),
  createRequirement: (data) => client.post('/requirements', data).then(r => r.data),

  // Test Cases
  getTestCases: (reqId) => client.get(`/requirements/${reqId}/test-cases`).then(r => r.data),
  generateTestCases: (reqId) => client.post(`/requirements/${reqId}/generate-test-cases`).then(r => r.data),

  // Selenium Scripts
  getScript: (tcId) => client.get(`/test-cases/${tcId}/script`).then(r => r.data),
  generateScript: (tcId) => client.post(`/test-cases/${tcId}/generate-script`).then(r => r.data),

  // Executions
  getExecutions: (tcId) => client.get(`/test-cases/${tcId}/executions`).then(r => r.data),
  executeTest: (tcId) => client.post(`/test-cases/${tcId}/execute`).then(r => r.data),

  // Dashboard
  getDashboard: () => client.get('/dashboard').then(r => r.data),
};
