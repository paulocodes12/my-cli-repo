-- Student Enrollment Management System: database schema
CREATE DATABASE IF NOT EXISTS enrollment_db;
USE enrollment_db;

CREATE TABLE IF NOT EXISTS users (
    user_id   INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    role      VARCHAR(20)  NOT NULL      -- 'Administrator' or 'Enrollment Staff'
);

CREATE TABLE IF NOT EXISTS students (
    student_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT NULL,
    student_number VARCHAR(20)  NOT NULL UNIQUE,
    name           VARCHAR(100) NOT NULL,
    address        VARCHAR(200),
    year_level     INT NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',  -- record state: 'ACTIVE' or 'ARCHIVED' (not enrollment status)
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT IGNORE INTO users (username, password, role) VALUES
('Admin1', '123', 'Enrollment Staff'),
('Admin2', '123', 'Administrator');