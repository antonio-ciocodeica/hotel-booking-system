import { Link } from 'react-router-dom';
import './App.css'; // Aici importăm fișierul CSS (acum golit de erori)

function App() {
  return (
      <div style={styles.background}>
        <div style={styles.overlay}>
          <div style={styles.content}>
            <h1 style={styles.title}>Luxury Stays</h1>
            <p style={styles.subtitle}>Hotel Management & Staff Portal</p>
            <div style={styles.buttonContainer}>
              <Link to="/login" style={{ textDecoration: 'none' }}>
                <button style={styles.primaryButton}>Staff Login</button>
              </Link>
              <Link to="/register" style={{ textDecoration: 'none' }}>
                <button style={styles.secondaryButton}>Register Account</button>
              </Link>
            </div>
          </div>
        </div>
      </div>
  );
}

const styles = {
  background: {
    height: '100vh',
    backgroundImage: 'url("https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80")',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif'
  },
  overlay: { height: '100%', width: '100%', backgroundColor: 'rgba(0, 34, 79, 0.6)', display: 'flex', alignItems: 'center', justifyContent: 'center' },
  content: { textAlign: 'center', color: 'white', padding: '40px', backgroundColor: 'rgba(255, 255, 255, 0.1)', backdropFilter: 'blur(10px)', borderRadius: '16px', boxShadow: '0 8px 32px rgba(0, 0, 0, 0.3)' },
  title: { fontSize: '3rem', margin: '0 0 10px 0', fontWeight: 'bold', letterSpacing: '2px' },
  subtitle: { fontSize: '1.2rem', margin: '0 0 40px 0', opacity: 0.9 },
  buttonContainer: { display: 'flex', gap: '20px', justifyContent: 'center' },
  primaryButton: { padding: '15px 35px', fontSize: '16px', fontWeight: 'bold', backgroundColor: '#0071c2', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', boxShadow: '0 4px 6px rgba(0, 0, 0, 0.2)', transition: 'transform 0.2s' },
  secondaryButton: { padding: '15px 35px', fontSize: '16px', fontWeight: 'bold', backgroundColor: 'transparent', color: 'white', border: '2px solid white', borderRadius: '8px', cursor: 'pointer', transition: 'background-color 0.2s, color 0.2s' }
};

export default App;