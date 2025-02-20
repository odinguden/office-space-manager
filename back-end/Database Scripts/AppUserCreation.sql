-- Create the user with a secure password
CREATE USER chairspace_user PASSWORD 'dhsjkafheuihfeqy8374y2783reuiwqryohfuqw';

-- Grant necessary privileges for connection
GRANT CONNECT ON DATABASE your_database TO chairspace_user;

-- Grant usage and required privileges on the schema
GRANT USAGE, CREATE ON SCHEMA app_data TO chairspace_user;

-- Ensure the user has the correct table privileges
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA app_data TO chairspace_user;

-- Ensure the user has the correct sequence privileges
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA app_data TO chairspace_user;

-- Ensure default privileges apply to future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA app_data
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO chairspace_user;

-- Ensure default privileges apply to future sequences
ALTER DEFAULT PRIVILEGES IN SCHEMA app_data

GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO chairspace_user;
