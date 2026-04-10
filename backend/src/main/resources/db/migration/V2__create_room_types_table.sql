CREATE TABLE room_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hotel_id UUID NOT NULL REFERENCES hotels(id),
    room_name VARCHAR(100) NOT NULL,
    room_facilities TEXT,
    child_capacity INTEGER,
    adult_capacity INTEGER,
    base_price DECIMAL(10,2) NOT NULL
);