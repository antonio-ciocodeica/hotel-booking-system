import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setMessage('');

    try {
      const response = await fetch('http://127.0.0.1:8080/auth/register/staff', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        setIsError(false);
        setMessage('Account created! Pending admin approval. Redirecting...');
        setTimeout(() => navigate('/login'), 3000);
      } else {
        const errorText = await response.text(); // Backend-ul trimite un simplu String acum
        setIsError(true);
        setMessage(errorText || 'Registration failed. Email might already exist.');
      }
    } catch (err) {
      setIsError(true);
      setMessage('Connection error. Please try again.');
    }
  };

  return (
      <div style={styles.pageContainer}>
        <div style={styles.formCard}>
          <div style={styles.headerArea}>
            <h1 style={styles.title}>Create Account</h1>
            <p style={styles.subtitle}>Join our hotel staff network</p>
          </div>
          <form onSubmit={handleRegister} style={styles.form}>

            <div style={styles.inputGroup}>
              <label style={styles.label}>Email Address</label>
              <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  style={styles.input}
                  placeholder="name@hotel.com"
              />
            </div>

            <div style={styles.inputGroup}>
              <label style={styles.label}>Password</label>
              <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  style={styles.input}
                  placeholder="••••••••"
              />
            </div>

            {message && (
                <div style={{ ...styles.messageBox, backgroundColor: isError ? 'rgba(255, 50, 50, 0.2)' : 'rgba(50, 255, 50, 0.2)', color: 'white', border: `1px solid ${isError ? 'rgba(255, 107, 107, 0.5)' : 'rgba(107, 255, 107, 0.5)'}` }}>
                  {message}
                </div>
            )}

            <button type="submit" style={styles.button}>Register Account</button>

            <div style={styles.footerText}>
              Already have an account? <Link to="/login" style={{color: '#66b2ff', textDecoration: 'none', fontWeight: 'bold'}}>Sign in here</Link>
            </div>
          </form>
        </div>
      </div>
  );
};

const styles = {
  pageContainer: {
    display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', width: '100vw',
    backgroundImage: 'url("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1920&q=80")',
    backgroundSize: 'cover', backgroundPosition: 'center', fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif'
  },
  formCard: {
    backgroundColor: 'rgba(20, 35, 55, 0.45)', backdropFilter: 'blur(16px)', WebkitBackdropFilter: 'blur(16px)',
    border: '1px solid rgba(255, 255, 255, 0.2)', padding: '40px 50px', borderRadius: '20px',
    boxShadow: '0 15px 35px rgba(0, 0, 0, 0.5)', width: '100%', maxWidth: '420px', boxSizing: 'border-box', margin: 'auto'
  },
  headerArea: { textAlign: 'center', marginBottom: '30px' },
  title: { margin: '0 0 5px 0', color: 'white', fontSize: '28px', fontWeight: 'bold', fontFamily: 'Georgia, serif' },
  subtitle: { margin: 0, color: 'white', fontSize: '15px', opacity: 0.8 },
  form: { display: 'flex', flexDirection: 'column' },
  inputGroup: { marginBottom: '20px' },
  label: { display: 'block', marginBottom: '8px', color: 'white', fontWeight: '600', fontSize: '14px' },
  input: { width: '100%', padding: '14px', backgroundColor: 'rgba(0, 0, 0, 0.3)', border: '1px solid rgba(255, 255, 255, 0.3)', color: 'white', borderRadius: '8px', boxSizing: 'border-box', fontSize: '15px', outline: 'none' },
  button: { padding: '16px', fontSize: '16px', fontWeight: 'bold', background: 'linear-gradient(to right, #003580, #00509e)', color: 'white', border: '1px solid rgba(255,255,255,0.3)', borderRadius: '8px', cursor: 'pointer', marginTop: '10px' },
  messageBox: { padding: '12px', borderRadius: '6px', marginBottom: '15px', textAlign: 'center', fontSize: '14px' },
  footerText: { textAlign: 'center', marginTop: '20px', fontSize: '14px', color: 'white', opacity: 0.9 }
};

export default Register;