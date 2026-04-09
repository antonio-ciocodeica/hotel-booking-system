CREATE TABLE room_types (
    room_type_id SERIAL PRIMARY KEY,
    hotel_id INTEGER NOT NULL REFERENCES hotels(hotel_id),
    room_name VARCHAR(100) NOT NULL,
    room_facilities TEXT,
    child_capacity INTEGER,
    adult_capacity INTEGER,
    base_price DECIMAL(10,2) NOT NULL
);