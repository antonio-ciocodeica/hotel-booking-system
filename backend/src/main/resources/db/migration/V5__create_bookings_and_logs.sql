CREATE TABLE bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    room_id UUID NOT NULL REFERENCES rooms(id),
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    status INTEGER NOT NULL DEFAULT 0,
    -- 0 = in asteptare, 1 = validata, 2 = finalizata, 3 = anulata
    price DECIMAL(10,2),
    reservation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    staff_id UUID NOT NULL REFERENCES staff(id),
    action_type INTEGER NOT NULL,
    timestamp BIGINT NOT NULL,
    details TEXT
);