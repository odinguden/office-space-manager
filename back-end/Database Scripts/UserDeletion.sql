-- Reassign objects to another user (e.g., postgres)
REASSIGN OWNED BY chairspace_user TO postgres;

-- Drop any privileges and owned objects
DROP OWNED BY chairspace_user CASCADE;

-- Now drop the user
DROP USER chairspace_user;
