CREATE TABLE IF NOT EXISTS employee (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    dob DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    department VARCHAR(100) NOT NULL
);