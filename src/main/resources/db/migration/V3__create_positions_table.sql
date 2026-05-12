CREATE TABLE positions (
                         id VARCHAR(255) PRIMARY KEY,
                         longitude VARCHAR(255) NOT NULL,
                         latitude VARCHAR(255) NOT NULL,
                         device_id VARCHAR(255) REFERENCES devices(id),
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL
);
