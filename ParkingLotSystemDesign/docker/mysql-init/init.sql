-- Initialize parking database and user (idempotent)
CREATE DATABASE IF NOT EXISTS parking;
CREATE USER IF NOT EXISTS 'parking_user'@'%' IDENTIFIED BY 'parking_pass';
GRANT ALL PRIVILEGES ON parking.* TO 'parking_user'@'%';
FLUSH PRIVILEGES;
