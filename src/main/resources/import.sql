INSERT INTO owner (id, name, email) VALUES (1, 'Ana Pop', 'ana.pop@example.com');
INSERT INTO owner (id, name, email) VALUES (2, 'Bogdan Ionescu', 'bogdan.ionescu@example.com');

INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (1, 'VIN12345', 'Dacia', 'Logan', 2018, 1);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (2, 'VIN67890', 'VW', 'Golf', 2021, 2);

INSERT INTO insurancepolicy (car_id, provider, start_date, end_date) VALUES (1, 'Allianz', DATE '2024-01-01', DATE '2024-12-31');
INSERT INTO insurancepolicy (car_id, provider, start_date, end_date) VALUES (1, 'Groupama', DATE '2025-01-01', DATE '2025-09-27' );
INSERT INTO insurancepolicy (car_id, provider, start_date, end_date) VALUES (2, 'Allianz', DATE '2025-03-01', DATE '2025-09-30');

--A Acceptance criteria - use a default validity period of 1 year for existing policies !--
--UPDATE insurancepolicy SET end_date = start_date + INTERVAL '1 year' WHERE end_date IS NULL

UPDATE insurancepolicy SET end_date = DATEADD('YEAR', 1, start_date) WHERE end_date IS NULL