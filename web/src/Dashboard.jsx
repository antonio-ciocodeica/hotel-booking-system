import React from 'react';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div style={styles.pageContainer}>
            <nav style={styles.navbar}>
                <div style={styles.navBrand}>Luxury Stays Admin</div>
                <button onClick={handleLogout} style={styles.logoutButton}>Logout</button>
            </nav>

            <div style={styles.content}>
                <div style={styles.welcomeCard}>
                    <h1 style={styles.title}>Dashboard Overview</h1>
                    <p style={styles.text}>Welcome back! You are securely logged into the staff portal.</p>

                    <div style={styles.statsGrid}>
                        <div style={styles.statBox}>
                            <h3 style={styles.statNumber}>12</h3>
                            <p style={styles.statLabel}>Today's Check-ins</p>
                        </div>
                        <div style={styles.statBox}>
                            <h3 style={styles.statNumber}>5</h3>
                            <p style={styles.statLabel}>Pending Requests</p>
                        </div>
                        <div style={styles.statBox}>
                            <h3 style={styles.statNumber}>89%</h3>
                            <p style={styles.statLabel}>Occupancy Rate</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

const styles = {
    pageContainer: {
        minHeight: '100vh', width: '100vw',
        backgroundImage: 'url("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1920&q=80")',
        backgroundSize: 'cover', backgroundPosition: 'center', backgroundAttachment: 'fixed',
        fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif'
    },
    navbar: {
        backgroundColor: 'rgba(0, 0, 0, 0.6)', backdropFilter: 'blur(10px)',
        padding: '20px 40px', display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        borderBottom: '1px solid rgba(255, 255, 255, 0.1)'
    },
    navBrand: { color: 'white', fontSize: '22px', fontWeight: 'bold', letterSpacing: '1px', fontFamily: 'Georgia, serif' },
    logoutButton: {
        padding: '10px 25px', fontSize: '14px', backgroundColor: 'transparent',
        color: 'white', border: '1px solid white', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold'
    },
    content: { padding: '40px', maxWidth: '1200px', margin: '0 auto' },
    welcomeCard: {
        backgroundColor: 'rgba(20, 35, 55, 0.6)', backdropFilter: 'blur(16px)', WebkitBackdropFilter: 'blur(16px)',
        padding: '40px', borderRadius: '12px', border: '1px solid rgba(255, 255, 255, 0.2)'
    },
    title: { color: 'white', margin: '0 0 10px 0', fontFamily: 'Georgia, serif' },
    text: { color: 'white', opacity: 0.8, fontSize: '16px', marginBottom: '30px' },
    statsGrid: { display: 'flex', gap: '20px', marginTop: '20px' },
    statBox: {
        flex: 1, backgroundColor: 'rgba(0, 0, 0, 0.4)', padding: '25px', borderRadius: '8px',
        borderLeft: '4px solid #0071c2', borderTop: '1px solid rgba(255,255,255,0.1)', borderRight: '1px solid rgba(255,255,255,0.1)', borderBottom: '1px solid rgba(255,255,255,0.1)'
    },
    statNumber: { margin: '0', fontSize: '38px', color: 'white' },
    statLabel: { margin: '5px 0 0 0', color: 'white', opacity: 0.9, fontSize: '15px', fontWeight: '600' }
};

export default Dashboard;