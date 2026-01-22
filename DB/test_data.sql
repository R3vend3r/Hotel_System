SET client_encoding = 'UTF8';

TRUNCATE TABLE amenity_orders, room_bookings, clients, amenities, rooms CASCADE;

INSERT INTO rooms (number, type, price, capacity, condition, stars, is_available) VALUES
(101, 'STANDARD', 2500.00, 2, 'EXCELLENT', 3, true),
(102, 'SUITE', 5000.00, 4, 'GOOD', 4, true),
(103, 'DELUXE', 7500.00, 2, 'PERFECT', 5, true),
(201, 'STANDARD', 3000.00, 3, 'GOOD', 3, true),
(202, 'STANDARD', 2800.00, 2, 'EXCELLENT', 4, true);

INSERT INTO clients (id, name, surname, room_number) VALUES
('cl1', 'Ivan', 'Ivanov', NULL),
('cl2', 'Petr', 'Petrov', NULL),
('cl3', 'Maria', 'Sidorova', NULL),
('cl4', 'Anna', 'Kuznetsova', NULL),
('cl5', 'Sergey', 'Vasiliev', NULL);

INSERT INTO amenities (id, name, price) VALUES
('am1', 'Breakfast', 500.00),
('am2', 'Room cleaning', 300.00),
('am3', 'SPA procedures', 2500.00),
('am4', 'Transfer', 1000.00),
('am5', 'Laundry', 400.00);

UPDATE clients SET room_number = 101 WHERE id = 'cl1';
UPDATE clients SET room_number = 102 WHERE id = 'cl2';
UPDATE clients SET room_number = 103 WHERE id = 'cl3';

INSERT INTO room_bookings (id, client_id, room_number, check_in_date, check_out_date, total_price) VALUES
('rb1', 'cl1', 101, '2023-10-01 14:00:00', '2023-10-10 12:00:00', 22500.00),
('rb2', 'cl2', 102, '2023-10-05 14:00:00', '2023-10-15 12:00:00', 50000.00),
('rb3', 'cl3', 103, '2023-10-10 14:00:00', '2023-10-20 12:00:00', 75000.00);

INSERT INTO amenity_orders (id, client_id, amenity_id, service_date, total_price) VALUES
('ao1', 'cl1', 'am1', '2023-10-02 08:00:00', 500.00),
('ao2', 'cl1', 'am2', '2023-10-03 10:00:00', 300.00),
('ao3', 'cl2', 'am3', '2023-10-06 16:00:00', 2500.00),
('ao4', 'cl3', 'am4', '2023-10-11 09:00:00', 1000.00);