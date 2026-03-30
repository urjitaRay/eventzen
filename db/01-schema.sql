CREATE DATABASE IF NOT EXISTS eventzen_db;
USE eventzen_db;

CREATE TABLE IF NOT EXISTS users (
id BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
password VARCHAR(255) NOT NULL,
role VARCHAR(20),
phone VARCHAR(20),
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_users_email (email),
CHECK (role IN ('ADMIN','CUSTOMER') OR role IS NULL)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS events (
event_id BIGINT NOT NULL AUTO_INCREMENT,
event_name VARCHAR(255),
event_type VARCHAR(255),
event_date DATE,
event_time TIME(6),
venue VARCHAR(255),
description VARCHAR(255),
budget DOUBLE,
created_by BIGINT,
PRIMARY KEY (event_id),
KEY idx_events_created_by (created_by),
CONSTRAINT fk_events_created_by
FOREIGN KEY (created_by) REFERENCES users (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS bookings (
booking_id BIGINT NOT NULL AUTO_INCREMENT,
user_id BIGINT,
event_id BIGINT,
rsvp_status VARCHAR(20),
booking_date DATE,
PRIMARY KEY (booking_id),
KEY idx_bookings_user_id (user_id),
KEY idx_bookings_event_id (event_id),
CONSTRAINT fk_bookings_user
FOREIGN KEY (user_id) REFERENCES users (id),
CONSTRAINT fk_bookings_event
FOREIGN KEY (event_id) REFERENCES events (event_id),
CHECK (rsvp_status IN ('CONFIRMED','PENDING','CANCELLED') OR rsvp_status IS NULL)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vendors (
vendor_id BIGINT NOT NULL AUTO_INCREMENT,
vendor_name VARCHAR(255),
service_type VARCHAR(255),
phone VARCHAR(255),
email VARCHAR(255),
event_id BIGINT,
PRIMARY KEY (vendor_id),
KEY idx_vendors_event_id (event_id),
CONSTRAINT fk_vendors_event
FOREIGN KEY (event_id) REFERENCES events (event_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS expenses (
expense_id BIGINT NOT NULL AUTO_INCREMENT,
event_id BIGINT,
vendor_id BIGINT,
amount DOUBLE,
description VARCHAR(255),
payment_status VARCHAR(20),
expense_date DATE,
PRIMARY KEY (expense_id),
KEY idx_expenses_event_id (event_id),
KEY idx_expenses_vendor_id (vendor_id),
CONSTRAINT fk_expenses_event
FOREIGN KEY (event_id) REFERENCES events (event_id),
CONSTRAINT fk_expenses_vendor
FOREIGN KEY (vendor_id) REFERENCES vendors (vendor_id),
CHECK (payment_status IN ('PAID','PENDING') OR payment_status IS NULL)
) ENGINE=InnoDB;