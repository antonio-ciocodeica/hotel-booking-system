CREATE TABLE room_type_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_type_id UUID NOT NULL REFERENCES room_types(id) ON DELETE CASCADE,
    url TEXT NOT NULL,
    sort_order INTEGER
);

CREATE INDEX idx_room_type_images_room_type_id ON room_type_images(room_type_id);

