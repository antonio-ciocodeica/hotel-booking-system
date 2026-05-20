import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('http://127.0.0.1:8080/auth/login/staff', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorText = await response.text();

        if (response.status === 403 && errorText.toLowerCase().includes('pending')) {
          throw new Error('Your account is pending admin approval.');
        }
        if (response.status === 403) {
          throw new Error('Not a staff account.');
        }
        if (response.status === 401 || response.status === 404 || response.status === 400) {
          throw new Error('Invalid email or password.');
        }
        throw new Error('Authentication failed.');
      }

      const data = await JSON.parse(await response.text());

      if (data.token) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role);
        localStorage.setItem('userEmail', email);

        // --- NOU: Salvăm hotelul ---
        if (data.hotelId) {
          localStorage.setItem('hotelId', data.hotelId);
        } else {
          localStorage.removeItem('hotelId'); // Ștergem dacă nu are hotel
        }

        navigate('/dashboard');
      } else {
        setError('Failed to generate token.');
      }
    } catch (err) {
      console.error("Fetch error details:", err);
      setError(err.message || 'Connection error. Is the server running?');
    }
  };

  return (
      <div style={styles.pageContainer}>
        <div style={styles.overlay}>
          <div style={styles.formCard}>
            <div style={styles.headerArea}>
              <h1 style={styles.title}>Staff Portal</h1>
              <p style={styles.subtitle}>Sign in to manage bookings</p>
            </div>
            <form onSubmit={handleLogin} style={styles.form}>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Email Address</label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="name@hotel.com"
                    required
                    style={styles.input}
                />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Password</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="••••••••"
                    required
                    style={styles.input}
                />
              </div>
              {error && <div style={styles.errorBox}>{error}</div>}

              <button type="submit" style={styles.button}>Sign In</button>

              <div style={styles.footerText}>
                Don't have an account? <Link to="/register" style={{color: '#66b2ff', textDecoration: 'none', fontWeight: 'bold'}}>Register here</Link>
              </div>

            </form>
          </div>
        </div>
      </div>
  );
};

const styles = {
  pageContainer: { display: 'flex', flexDirection: 'column', minHeight: '100vh', width: '100vw', backgroundImage: 'url("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1920&q=80")', backgroundSize: 'cover', backgroundPosition: 'center', fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif' },
  overlay: { flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, rgba(4, 15, 30, 0.7) 0%, rgba(10, 30, 60, 0.4) 100%)', padding: '20px' },
  formCard: { backgroundColor: 'rgba(20, 35, 55, 0.45)', backdropFilter: 'blur(16px)', WebkitBackdropFilter: 'blur(16px)', border: '1px solid rgba(255, 255, 255, 0.2)', padding: '45px 50px', borderRadius: '20px', boxShadow: '0 15px 35px rgba(0, 0, 0, 0.5)', width: '100%', maxWidth: '420px', boxSizing: 'border-box' },
  headerArea: { textAlign: 'center', marginBottom: '35px' },
  title: { margin: '0 0 8px 0', color: 'white', fontSize: '32px', fontWeight: 'bold', fontFamily: 'Georgia, serif', letterSpacing: '1px' },
  subtitle: { margin: 0, color: 'white', fontSize: '15px', opacity: 0.8 },
  form: { display: 'flex', flexDirection: 'column' },
  inputGroup: { marginBottom: '20px' },
  label: { display: 'block', marginBottom: '8px', color: 'white', fontWeight: '600', fontSize: '14px', letterSpacing: '0.5px' },
  input: { width: '100%', padding: '14px', backgroundColor: 'rgba(0, 0, 0, 0.3)', border: '1px solid rgba(255, 255, 255, 0.3)', color: 'white', borderRadius: '8px', boxSizing: 'border-box', fontSize: '15px', outline: 'none' },
  button: { padding: '16px', fontSize: '16px', fontWeight: 'bold', letterSpacing: '1px', background: 'linear-gradient(to right, #003580, #00509e)', color: 'white', border: '1px solid rgba(255,255,255,0.3)', borderRadius: '8px', cursor: 'pointer', marginTop: '15px', boxShadow: '0 4px 15px rgba(0, 0, 0, 0.3)' },
  errorBox: { backgroundColor: 'rgba(255, 50, 50, 0.2)', color: 'white', padding: '12px', borderRadius: '6px', marginBottom: '15px', textAlign: 'center', fontSize: '14px', border: '1px solid rgba(255, 107, 107, 0.5)' },
  footerText: { textAlign: 'center', marginTop: '20px', fontSize: '14px', color: 'white', opacity: 0.9 }
};

export default Login;