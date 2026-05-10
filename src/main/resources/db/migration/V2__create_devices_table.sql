CREATE TABLE devices (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    serial_number VARCHAR(255) NOT NULL,
    pin VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) REFERENCES users(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
