ALTER TABLE IF EXISTS clients DROP CONSTRAINT IF EXISTS fk_client_room;
ALTER TABLE IF EXISTS room_bookings DROP CONSTRAINT IF EXISTS room_bookings_client_id_fkey;
ALTER TABLE IF EXISTS room_bookings DROP CONSTRAINT IF EXISTS room_bookings_room_number_fkey;
ALTER TABLE IF EXISTS amenity_orders DROP CONSTRAINT IF EXISTS amenity_orders_client_id_fkey;
ALTER TABLE IF EXISTS amenity_orders DROP CONSTRAINT IF EXISTS amenity_orders_amenity_id_fkey;

CREATE TABLE IF NOT EXISTS rooms (
    number INT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL,
    condition VARCHAR(50) NOT NULL,
    stars INT NOT NULL,
    is_available BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS clients (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    room_number INT REFERENCES rooms(number) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS amenities (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS room_bookings (
    id VARCHAR(50) PRIMARY KEY,
    client_id VARCHAR(50) NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    room_number INT NOT NULL REFERENCES rooms(number) ON DELETE CASCADE,
    check_in_date TIMESTAMP NOT NULL,
    check_out_date TIMESTAMP NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS amenity_orders (
    id VARCHAR(50) PRIMARY KEY,
    client_id VARCHAR(50) NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    amenity_id VARCHAR(50) NOT NULL REFERENCES amenities(id) ON DELETE CASCADE,
    service_date TIMESTAMP NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
);