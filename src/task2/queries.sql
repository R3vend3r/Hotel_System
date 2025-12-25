--1
SELECT model, speed, hd
FROM PC
WHERE price::numeric < 500;

--2
SELECT maker
FROM Product
WHERE type = 'Printer';

--3
SELECT model, ram, screen
FROM Laptop
WHERE price::numeric > 1000;

--4
SELECT *
FROM Printer
WHERE color = 'y';

--5
SELECT model, speed, hd
FROM PC
WHERE cd IN ('12x','24x') AND price::numeric < 600;

--6
SELECT p.maker, l.speed
FROM Laptop l
JOIN Product p ON l.model = p.model
WHERE l.hd >= 100;

--7
SELECT p.model, l.price
FROM Product p
JOIN Laptop l ON p.model = l.model
WHERE p.maker = 'B'

UNION

SELECT p.model, pc.price
FROM Product p
JOIN PC pc ON p.model = pc.model
WHERE p.maker = 'B'

UNION

SELECT p.model, r.price
FROM Product p
JOIN Printer r ON p.model = r.model
WHERE p.maker = 'B';

--8
SELECT DISTINCT p.maker
FROM Product p
JOIN PC ON p.model = PC.model
WHERE p.maker NOT IN (
    SELECT maker FROM Product WHERE type = 'Laptop'
);

--9
SELECT DISTINCT P.maker
FROM Product P
JOIN PC ON P.model = PC.model
WHERE PC.speed >= 450;

--10
SELECT model, price
FROM Printer
WHERE price = (SELECT MAX(price) FROM Printer);

--11
SELECT AVG(speed) AS avg_speed
FROM PC;

--12
SELECT AVG(speed) AS avg_speed
FROM Laptop
WHERE price::numeric > 1000;

--13
SELECT AVG(PC.speed) AS avg_speed
FROM PC
JOIN Product P ON PC.model = P.model
WHERE P.maker = 'A';

--14
SELECT speed, AVG(price::numeric) AS avg_price
FROM PC
GROUP BY speed;

--15
SELECT hd
FROM PC
GROUP BY hd
HAVING COUNT(*) >= 2;

--16
SELECT DISTINCT PC1.model AS model1, PC2.model AS model2, PC1.speed, PC1.ram
FROM PC PC1, PC PC2
WHERE PC1.speed = PC2.speed
  AND PC1.ram = PC2.ram
  AND PC1.model < PC2.model;

--17
SELECT 'Laptop' AS type, model, speed
FROM Laptop
WHERE speed < ALL (SELECT speed FROM PC);

--18
SELECT P.maker, MIN(Pr.price::numeric) AS min_price
FROM Printer Pr
JOIN Product P ON Pr.model = P.model
WHERE Pr.color = 'y'
GROUP BY P.maker
HAVING MIN(Pr.price::numeric) = (SELECT MIN(price::numeric) FROM Printer WHERE color = 'y');

--19
SELECT p.maker, AVG(l.screen) AS avg_screen
FROM Laptop l
JOIN Product p ON l.model = p.model
GROUP BY p.maker;

--20
SELECT maker, COUNT(*) AS pc_count
FROM Product
WHERE type = 'PC'
GROUP BY maker
HAVING COUNT(*) >= 3;

--21
SELECT p.maker, MAX(PC.price::numeric) AS max_price
FROM PC
JOIN Product p ON PC.model = p.model
GROUP BY p.maker;

--22
SELECT speed, AVG(price::numeric) AS avg_price
FROM PC
WHERE speed > 600
GROUP BY speed;

--23
SELECT DISTINCT p.maker
FROM Product p
WHERE EXISTS (
    SELECT 1 FROM PC
    WHERE PC.model = p.model AND PC.speed >= 750
) AND EXISTS (
    SELECT 1 FROM Laptop l
    WHERE l.model = p.model AND l.speed >= 750
);

--24
WITH AllProducts AS (
    SELECT model, price::numeric AS price FROM PC
    UNION
    SELECT model, price::numeric AS price FROM Laptop
    UNION
    SELECT model, price::numeric AS price FROM Printer
)
SELECT model
FROM AllProducts
WHERE price = (SELECT MAX(price) FROM AllProducts);

--25
SELECT DISTINCT p.maker
FROM Product p
WHERE p.type = 'Printer'
AND p.maker IN (
    SELECT pr.maker
    FROM Product pr
    JOIN PC ON pr.model = PC.model
    WHERE PC.ram = (SELECT MIN(ram) FROM PC)
    AND PC.speed = (
        SELECT MAX(speed)
        FROM PC
        WHERE ram = (SELECT MIN(ram) FROM PC)
    )
);