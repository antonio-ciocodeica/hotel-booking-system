import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Login from './Login.jsx';
import Register from './Register.jsx';
import Dashboard from './Dashboard.jsx'; // <-- IMPORTĂ NOUA COMPONENTĂ

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <Router>
            <Routes>
                <Route path="/" element={<App />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* RUTA PENTRU LOGOUT ȘI STAFF */}
                <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
        </Router>
    </StrictMode>
)