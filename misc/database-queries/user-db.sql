CREATE DATABASE IF NOT EXISTS user_service_db;

USE user_service_db;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ADMIN', 'USER', 'MODERATOR') NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Sample Population Queries
-- Insert sample users into the 'users' table
INSERT INTO users (name, email, password) VALUES 
('Alice Smith', 'alice.smith@example.com', 'password123'),
('Bob Johnson', 'bob.johnson@example.com', 'securepass456'),
('Carol White', 'carol.white@example.com', 'mypassword789'),
('David Brown', 'david.brown@example.com', 'adminpassword321'),
('Eve Davis', 'eve.davis@example.com', 'pass1234');


-- CREATE TABLE IF NOT EXISTS users (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     email VARCHAR(255) NOT NULL UNIQUE,
--     password VARCHAR(255) NOT NULL,
--     role ENUM('MODERATOR', 'USER') NOT NULL
-- );