DROP TABLE IF EXISTS Printer;
DROP TABLE IF EXISTS Laptop;
DROP TABLE IF EXISTS PC;
DROP TABLE IF EXISTS Product;

CREATE TABLE Product (
    maker varchar(10) NOT NULL,
    model varchar(50) NOT NULL PRIMARY KEY,
    type varchar(50) NOT NULL CHECK (type IN ('Laptop', 'PC', 'Printer'))
);

CREATE TABLE Laptop (
    code int NOT NULL PRIMARY KEY,
    model varchar(50) NOT NULL,
    speed smallint NOT NULL CHECK (speed > 0),
    ram smallint NOT NULL CHECK (ram > 0),
    hd real NOT NULL CHECK (hd > 0),
    price MONEY,
    screen smallint NOT NULL CHECK (screen > 0),
    FOREIGN KEY (model) REFERENCES product (model)

);

CREATE TABLE PC (
    code int NOT NULL PRIMARY KEY,
    model varchar(50) NOT NULL,
    speed smallint NOT NULL CHECK (speed >0),
    ram smallint NOT NULL CHECK (ram > 0),
    hd real NOT NULL CHECK (hd > 0),
    cd varchar(10) NOT NULL CHECK (cd IN ('4x', '8x', '12x', '16x', '24x', '32x', '48x', '52x')),
    price MONEY,
	FOREIGN KEY (model) REFERENCES product (model)
);

CREATE TABLE Printer (
    code int NOT NULL PRIMARY KEY,
    model varchar(50) NOT NULL,
    color char(1) NOT NULL CHECK (color IN ('y', 'n')),
    type varchar(10) NOT NULL CHECK (type IN ('Laser', 'Jet', 'Matrix')),
    price Money,
	FOREIGN KEY (model) REFERENCES product (model)
);