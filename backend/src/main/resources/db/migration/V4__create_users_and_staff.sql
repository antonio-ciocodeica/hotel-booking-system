CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    account_creation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE staff (
    staff_id SERIAL PRIMARY KEY,
    hotel_id INTEGER NOT NULL REFERENCES hotels(hotel_id),
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role INTEGER NOT NULL,
    -- 1 = receptioner, 2 = administrator
    account_status INTEGER NOT NULL DEFAULT 1
    -- 1 = activ, 0 = suspendat
);