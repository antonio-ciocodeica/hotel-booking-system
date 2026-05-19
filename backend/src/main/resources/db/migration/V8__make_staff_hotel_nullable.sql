-- Allow global admins to exist in the staff table without belonging to a specific hotel.
-- (hotel_id becomes nullable)

ALTER TABLE staff
    ALTER COLUMN hotel_id DROP NOT NULL;

