import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
    const navigate = useNavigate();
    const userRole = localStorage.getItem('role');
    const isAdmin = userRole === '2';

    const [pendingStaff, setPendingStaff] = useState([]);
    const [allStaff, setAllStaff] = useState([]);
    const [allHotels, setAllHotels] = useState([]);
    const [allRoomTypes, setAllRoomTypes] = useState([]);
    const [showHotelForm, setShowHotelForm] = useState(false);
    const [myHotelId, setMyHotelId] = useState(localStorage.getItem('hotelId') || null);

    const [newHotel, setNewHotel] = useState({ name: '', location: '', facilities: '', description: '' });
    const [newRoomType, setNewRoomType] = useState({ hotelId: '', roomName: '', basePrice: '', childCapacity: '', adultCapacity: '', roomFacilities: '' });
    const [newRoom, setNewRoom] = useState({ roomTypeId: '', roomNumber: '' });

    // State for images
    const [roomTypeImages, setRoomTypeImages] = useState(null);

    useEffect(() => {
        fetchAllHotels();
        fetchAllRoomTypes();
        if (isAdmin) {
            fetchAllStaff();
            fetchPendingStaff();
        }
    }, [isAdmin]);

    useEffect(() => {
        if (!isAdmin) {
            const email = localStorage.getItem('userEmail');
            if (email && allStaff.length > 0) {
                const me = allStaff.find(s => s.email?.toLowerCase() === email?.toLowerCase());
                if (me) {
                    if (me.hotel && me.hotel.id) {
                        setMyHotelId(me.hotel.id);
                    } else if (me.hotelId) {
                        setMyHotelId(me.hotelId);
                    }
                }
            }
        }
    }, [allStaff, isAdmin]);

    const fetchAllHotels = async () => {
        const res = await fetch('http://127.0.0.1:8080/hotels', { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if (res.ok) setAllHotels(await res.json());
    };

    const fetchAllStaff = async () => {
        const res = await fetch('http://127.0.0.1:8080/auth/staff', { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if (res.ok) setAllStaff(await res.json());
    };

    const fetchPendingStaff = async () => {
        const res = await fetch('http://127.0.0.1:8080/auth/staff/pending', { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if (res.ok) setPendingStaff(await res.json());
    };

    const fetchAllRoomTypes = async () => {
        const res = await fetch('http://127.0.0.1:8080/room-types', { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if (res.ok) setAllRoomTypes(await res.json());
    };

    const handleAssignHotel = async (staffId, hotelId) => {
        if(!hotelId) return;
        await fetch(`http://127.0.0.1:8080/auth/staff/${staffId}/assign-hotel/${hotelId}`, {
            method: 'PUT', headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        });
        fetchAllStaff();
    };

    const handleCreateHotel = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch('http://127.0.0.1:8080/hotels', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(newHotel)
            });

            if (res.ok) {
                alert('Hotel created successfully!');
                setShowHotelForm(false);
                fetchAllHotels();
                setNewHotel({ name: '', location: '', facilities: '', description: '' });
            } else {
                const errorData = await res.json();
                console.error("Server error:", errorData);
                alert('Failed to create hotel: ' + (errorData.message || 'Check console for details'));
            }
        } catch (err) {
            console.error("Network error:", err);
            alert("Network error. Check if backend is running.");
        }
    };

    const handleCreateRoomType = async (e) => {
        e.preventDefault();
        const finalHotelId = isAdmin ? newRoomType.hotelId : myHotelId;

        if(!finalHotelId) return alert("Please select a hotel or ensure you are assigned to one!");

        try {
            const res = await fetch(`http://127.0.0.1:8080/hotels/${finalHotelId}/room-types`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}` },
                body: JSON.stringify({
                    roomName: newRoomType.roomName,
                    basePrice: newRoomType.basePrice,
                    childCapacity: parseInt(newRoomType.childCapacity),
                    adultCapacity: parseInt(newRoomType.adultCapacity),
                    roomFacilities: newRoomType.roomFacilities
                })
            });

            if (res.ok) {
                const createdRoomType = await res.json();
                const newRoomTypeId = createdRoomType.id;

                if (roomTypeImages && roomTypeImages.length > 0) {
                    const formData = new FormData();
                    for (let i = 0; i < roomTypeImages.length; i++) {
                        formData.append('files', roomTypeImages[i]);
                    }

                    const imgRes = await fetch(`http://127.0.0.1:8080/room-types/${newRoomTypeId}/images`, {
                        method: 'POST',
                        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` },
                        body: formData
                    });

                    if (!imgRes.ok) {
                        alert("Room Type created, but image upload failed!");
                    }
                }

                alert('Room Type created successfully!');
                fetchAllRoomTypes();
                setNewRoomType({ hotelId: isAdmin ? newRoomType.hotelId : '', roomName: '', basePrice: '', childCapacity: '', adultCapacity: '', roomFacilities: '' });
                setRoomTypeImages(null);
                document.getElementById('image-upload-input').value = "";
            } else {
                const errorData = await res.json();
                alert('Failed: ' + (errorData.message || 'Error creating room type'));
            }
        } catch (err) {
            console.error(err);
            alert("A network error occurred.");
        }
    };

    const handleCreateRoom = async (e) => {
        e.preventDefault();
        if(!newRoom.roomTypeId) return alert("Please select a Room Type!");

        const res = await fetch(`http://127.0.0.1:8080/room-types/${newRoom.roomTypeId}/rooms`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}` },
            body: JSON.stringify({ roomNumber: parseInt(newRoom.roomNumber), roomStatus: 0 })
        });

        if (res.ok) {
            alert('Room added successfully!');
            setNewRoom({...newRoom, roomNumber: ''});
        } else {
            const errorData = await res.json();
            alert('Failed: ' + (errorData.message || 'Error creating room'));
        }
    };

    return (
        <div style={styles.pageContainer}>
            <nav style={styles.navbar}>
                <div style={styles.navBrand}>Luxury Stays {isAdmin ? 'Admin' : 'Staff'}</div>
                <button onClick={() => {localStorage.clear(); navigate('/login');}} style={styles.logoutButton}>Logout</button>
            </nav>

            <div style={styles.content}>
                <div style={styles.welcomeCard}>
                    <h1 style={styles.title}>{isAdmin ? 'Admin Dashboard' : 'Staff Dashboard'}</h1>

                    {isAdmin && (
                        <>
                            {/* HOTEL MANAGEMENT SECTION */}
                            <div style={styles.adminSection}>
                                <h2 style={styles.sectionTitle}>Hotel Management</h2>
                                <button onClick={() => setShowHotelForm(!showHotelForm)} style={styles.submitButton}>
                                    {showHotelForm ? 'Cancel' : 'Add New Hotel'}
                                </button>
                                {showHotelForm && (
                                    <form onSubmit={handleCreateHotel} style={{...styles.hotelForm, marginTop: '15px'}}>
                                        <input
                                            style={styles.input}
                                            placeholder="Hotel Name"
                                            value={newHotel.name}
                                            onChange={e => setNewHotel({...newHotel, name: e.target.value})}
                                            required
                                        />
                                        <input
                                            style={styles.input}
                                            placeholder="Location"
                                            value={newHotel.location}
                                            onChange={e => setNewHotel({...newHotel, location: e.target.value})}
                                            required
                                        />
                                        <input
                                            style={styles.input}
                                            placeholder="Facilities (e.g., Wi-Fi, Pool)"
                                            value={newHotel.facilities}
                                            onChange={e => setNewHotel({...newHotel, facilities: e.target.value})}
                                            required
                                        />
                                        <input
                                            style={styles.input}
                                            placeholder="Description"
                                            value={newHotel.description}
                                            onChange={e => setNewHotel({...newHotel, description: e.target.value})}
                                            required
                                        />
                                        <button type="submit" style={{...styles.submitButton, backgroundColor: '#28a745'}}>Save Hotel</button>
                                    </form>
                                )}
                            </div>

                            <div style={styles.adminSection}>
                                <h2 style={styles.sectionTitle}>Pending Approvals ({pendingStaff.length})</h2>
                                {pendingStaff.map(s => (
                                    <div key={s.id} style={styles.listItem}>
                                        <span>{s.email}</span>
                                        <button onClick={async()=>{await fetch(`http://127.0.0.1:8080/auth/staff/${s.id}/approve`, {method:'PUT', headers:{'Authorization': `Bearer ${localStorage.getItem('token')}`}}); fetchPendingStaff();}} style={styles.approveButton}>Approve</button>
                                    </div>
                                ))}
                            </div>
                            <div style={styles.adminSection}>
                                <h2 style={styles.sectionTitle}>Staff Directory & Assignments</h2>
                                {allStaff.map(s => (
                                    <div key={s.id} style={styles.listItem}>
                                        <span>{s.email} - Hotel: {s.hotel?.name || 'None'}</span>
                                        <select onChange={e => handleAssignHotel(s.id, e.target.value)} style={styles.dropdownSelect}>
                                            <option value="">Assign Hotel...</option>
                                            {allHotels.map(h => <option key={h.id} value={h.id}>{h.name}</option>)}
                                        </select>
                                    </div>
                                ))}
                            </div>
                        </>
                    )}

                    <div style={styles.adminSection}>
                        <h2 style={styles.sectionTitle}>Room Management</h2>
                        <form onSubmit={handleCreateRoomType} style={styles.hotelForm}>
                            {isAdmin ? (
                                <select onChange={e => setNewRoomType({...newRoomType, hotelId: e.target.value})} style={styles.dropdownSelect} value={newRoomType.hotelId}>
                                    <option value="">Select Hotel</option>
                                    {allHotels.map(h => <option key={h.id} value={h.id}>{h.name}</option>)}
                                </select>
                            ) : (
                                <div style={{...styles.input, backgroundColor: 'rgba(255,255,255,0.05)', color: '#ccc'}}>
                                    Assigned Hotel: {myHotelId ? (allHotels.find(h => h.id === myHotelId)?.name || 'Loading...') : 'No hotel assigned'}
                                </div>
                            )}
                            <input style={styles.input} placeholder="Room Name" value={newRoomType.roomName} onChange={e => setNewRoomType({...newRoomType, roomName: e.target.value})} required/>
                            <input style={styles.input} placeholder="Price" type="number" value={newRoomType.basePrice} onChange={e => setNewRoomType({...newRoomType, basePrice: e.target.value})} required/>
                            <input style={styles.input} placeholder="Adult Cap" type="number" value={newRoomType.adultCapacity} onChange={e => setNewRoomType({...newRoomType, adultCapacity: e.target.value})} required/>
                            <input style={styles.input} placeholder="Child Cap" type="number" value={newRoomType.childCapacity} onChange={e => setNewRoomType({...newRoomType, childCapacity: e.target.value})} required/>
                            <input style={styles.input} placeholder="Facilities" value={newRoomType.roomFacilities} onChange={e => setNewRoomType({...newRoomType, roomFacilities: e.target.value})} required/>
                            <input id="image-upload-input" type="file" multiple accept="image/*" style={{...styles.input, padding: '10px'}} onChange={e => setRoomTypeImages(e.target.files)}/>
                            <button type="submit" style={styles.submitButton}>Create Room Type</button>
                        </form>

                        <form onSubmit={handleCreateRoom} style={{...styles.hotelForm, marginTop: '20px'}}>
                            <select onChange={e => setNewRoom({...newRoom, roomTypeId: e.target.value})} style={styles.dropdownSelect} value={newRoom.roomTypeId} required>
                                <option value="">Select Room Type</option>
                                {allRoomTypes.filter(rt => isAdmin ? true : rt.hotelId === myHotelId).map(rt => (
                                    <option key={rt.id} value={rt.id}>{rt.roomName} ({allHotels.find(h => h.id === rt.hotelId)?.name || 'Unknown'})</option>
                                ))}
                            </select>
                            <input style={styles.input} placeholder="Room Number" type="number" value={newRoom.roomNumber} onChange={e => setNewRoom({...newRoom, roomNumber: e.target.value})} required/>
                            <button type="submit" style={styles.submitButton}>Add Room</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

const styles = {
    pageContainer: { minHeight: '100vh', backgroundImage: 'url("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1920&q=80")', backgroundSize: 'cover', backgroundAttachment: 'fixed', fontFamily: '"Segoe UI", sans-serif' },
    navbar: { backgroundColor: 'rgba(0,0,0,0.6)', padding: '20px 40px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    navBrand: { color: 'white', fontSize: '22px', fontWeight: 'bold' },
    logoutButton: { padding: '10px 20px', backgroundColor: 'transparent', color: 'white', border: '1px solid white', borderRadius: '4px', cursor: 'pointer' },
    content: { padding: '40px', maxWidth: '1000px', margin: '0 auto' },
    welcomeCard: { backgroundColor: 'rgba(20, 35, 55, 0.7)', backdropFilter: 'blur(10px)', padding: '40px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.1)' },
    title: { color: 'white', marginBottom: '20px' },
    adminSection: { marginTop: '20px', padding: '25px', backgroundColor: 'rgba(0,0,0,0.3)', borderRadius: '10px', border: '1px solid rgba(255,255,255,0.1)' },
    sectionTitle: { color: 'white', fontSize: '20px', marginBottom: '15px' },
    listItem: { display: 'flex', justifyContent: 'space-between', padding: '12px 20px', backgroundColor: 'rgba(255,255,255,0.05)', marginBottom: '8px', borderRadius: '6px', color: 'white', alignItems: 'center', border: '1px solid rgba(255,255,255,0.1)' },
    approveButton: { backgroundColor: '#4CAF50', color: 'white', border: 'none', padding: '8px 16px', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' },
    hotelForm: { display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '10px' },
    input: { padding: '12px', borderRadius: '6px', border: '1px solid rgba(255,255,255,0.2)', backgroundColor: 'rgba(0,0,0,0.4)', color: 'white', width: '100%', boxSizing: 'border-box' },
    dropdownSelect: { padding: '12px', borderRadius: '6px', border: '1px solid rgba(255,255,255,0.2)', backgroundColor: '#1a2a3a', color: 'white', cursor: 'pointer', width: '100%' },
    submitButton: { padding: '12px', backgroundColor: '#00509e', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' }
};

export default Dashboard;