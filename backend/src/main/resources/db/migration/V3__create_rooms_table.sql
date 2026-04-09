CREATE TABLE rooms (
   room_id SERIAL PRIMARY KEY,
   room_type_id INTEGER NOT NULL REFERENCES room_types(room_type_id),
   camera_number INTEGER NOT NULL,
   camera_status INTEGER NOT NULL DEFAULT 0
    -- 0 = disponibila, 1 = ocupata, 2 = indisponibila
);