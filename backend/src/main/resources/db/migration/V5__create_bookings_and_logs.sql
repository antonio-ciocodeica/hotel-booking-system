CREATE TABLE bookings (
    booking_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    room_id INTEGER NOT NULL REFERENCES rooms(room_id),
    data_checkin DATE NOT NULL,
    data_checkout DATE NOT NULL,
    status_reservation INTEGER NOT NULL DEFAULT 0,
    -- 0 = in asteptare, 1 = validata, 2 = finalizata, 3 = anulata
    total_sum DECIMAL(10,2),
    reservation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE audit_logs (
    log_id SERIAL PRIMARY KEY,
    staff_id INTEGER NOT NULL REFERENCES staff(staff_id),
    action_type INTEGER NOT NULL,
    timestamp BIGINT NOT NULL,
    details TEXT
);