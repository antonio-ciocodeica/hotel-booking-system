CREATE TABLE rooms (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   room_type_id UUID NOT NULL REFERENCES room_types(id),
   room_number INTEGER NOT NULL,
   room_status INTEGER NOT NULL DEFAULT 0
    -- 0 = disponibila, 1 = ocupata, 2 = indisponibila
);