INSERT INTO Product (maker, model, type) VALUES
('HP', 'Pavilion 590', 'PC'),
('HP', 'EliteBook 850', 'Laptop'),
('HP', 'LaserJet Pro', 'Printer'),
('Lenovo', 'ThinkPad X1', 'Laptop'),
('Lenovo', 'IdeaCentre 510', 'PC'),
('Dell', 'XPS 13', 'Laptop'),
('Dell', 'OptiPlex 7070', 'PC'),
('Canon', 'PIXMA MG3640', 'Printer'),
('Epson', 'EcoTank ET-2750', 'Printer'),
('Acer', 'Aspire 5', 'Laptop');

INSERT INTO Laptop (code, model, speed, ram, hd, price, screen) VALUES
(1, 'EliteBook 850', 2700, 16, 512, 15.6, 950.00),
(2, 'ThinkPad X1', 3200, 32, 1024, 14.0, 1500.00),
(3, 'XPS 13', 2900, 8, 256, 13.3, 1100.00),
(4, 'Aspire 5', 2400, 12, 512, 15.6, 700.00);

INSERT INTO PC (code, model, speed, ram, hd, cd, price) VALUES
(10, 'Pavilion 590', 3200, 16, 1024, '12x', 800.00),
(11, 'OptiPlex 7070', 2900, 8, 512, '8x', 650.00),
(12, 'IdeaCentre 510', 3500, 32, 2048, '16x', 1200.00);

INSERT INTO Printer (code, model, color, type, price) VALUES
(20, 'LaserJet Pro', 'n', 'Laser', 250.00),
(21, 'PIXMA MG3640', 'y', 'Jet', 120.00),
(22, 'EcoTank ET-2750', 'y', 'Matrix', 300.00);