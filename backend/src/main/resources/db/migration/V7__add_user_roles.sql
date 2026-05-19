-- Adds a role column to users so Spring Security can grant authorities (ROLE_USER, ROLE_STAFF, ROLE_MANAGER)
-- Existing rows default to USER.

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'USER';

