-- data.sql — SAFE VERSION (NO TRUNCATE, NO DELETE)

-- SYSTEM ADMIN
INSERT INTO system_admins (username, email, phone, password, created_by, created_at)
VALUES ('admin','admin@example.com','9999999999',
        '$2a$10$wH6xRLV6jwSiY2VIBwsR1utU1T2WsTm1kQ/70ZRmTr8qRLVQDPtBW',
        'system', CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;


-- DEPARTMENTS
INSERT INTO departments (name, description, created_by, created_at)
VALUES
 ('IT Department','Handles software, servers, and networks.','system',CURRENT_TIMESTAMP),
 ('HR Department','Handles hiring, payroll, and employee relations.','system',CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;


-- EMPLOYEES
INSERT INTO employees (first_name, last_name, email, phone, position, join_date, department_id, created_by, created_at)
SELECT 'Ravi','Sharma','ravi.sharma@example.com','9876543210','SENIOR','2018-02-10', d.id,'system',CURRENT_TIMESTAMP
FROM departments d WHERE d.name='IT Department'
ON CONFLICT (email) DO NOTHING;

INSERT INTO employees (first_name, last_name, email, phone, position, join_date, department_id, created_by, created_at)
SELECT 'Aman','Kumar','aman.kumar@example.com','9988776655','INTERN','2024-11-20', d.id,'system',CURRENT_TIMESTAMP
FROM departments d WHERE d.name='IT Department'
ON CONFLICT (email) DO NOTHING;

INSERT INTO employees (first_name, last_name, email, phone, position, join_date, department_id, created_by, created_at)
SELECT 'Neha','Verma','neha.verma@example.com','9123456780','MANAGER','2021-06-01', d.id,'system',CURRENT_TIMESTAMP
FROM departments d WHERE d.name='HR Department'
ON CONFLICT (email) DO NOTHING;


-- ADDRESSES
INSERT INTO addresses (line1, city, state, pin_code, country, type, employee_id, created_by, created_at)
SELECT 'House No. 10','Delhi','Delhi','110001','India','PERMANENT', e.id,'system',CURRENT_TIMESTAMP
FROM employees e WHERE e.email='ravi.sharma@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO addresses (line1, city, state, pin_code, country, type, employee_id, created_by, created_at)
SELECT 'PG-202, Sector 62','Noida','UP','201301','India','CURRENT', e.id,'system',CURRENT_TIMESTAMP
FROM employees e WHERE e.email='ravi.sharma@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO addresses (line1, city, state, pin_code, country, type, employee_id, created_by, created_at)
SELECT 'Room 12, Hostel A','Noida','UP','201301','India','PERMANENT', e.id,'system',CURRENT_TIMESTAMP
FROM employees e WHERE e.email='aman.kumar@example.com'
ON CONFLICT DO NOTHING;
















--
--TRUNCATE TABLE addresses, employees, departments, system_admins
--RESTART IDENTITY CASCADE;
--
---- system admin
--INSERT INTO system_admins (username, email, phone, password, created_by)
--VALUES (
--    'admin',
--    'admin@example.com',
--    '9999999999',
--    '$2a$10$wH6xRLV6jwSiY2VIBwsR1utU1T2WsTm1kQ/70ZRmTr8qRLVQDPtBW',
--    'system'
--);
--
---- departments
--INSERT INTO departments (name, description, created_by)
--VALUES
--('IT Department', 'Handles software, servers, and networks.', 'system'),
--('HR Department', 'Handles hiring, payroll, and employee relations.', 'system');
--
---- employees
--INSERT INTO employees (
--    first_name, last_name, email, phone,
--    position, join_date, department_id, created_by
--)
--VALUES
--(
--    'Ravi', 'Sharma', 'ravi.sharma@example.com',
--    '9876543210', 'SENIOR', '2018-02-10',
--    (SELECT id FROM departments WHERE name = 'IT Department'),
--    'system'
--),
--(
--    'Anita', 'Verma', 'anita.verma@example.com',
--    '9123456780', 'JUNIOR', '2021-07-01',
--    (SELECT id FROM departments WHERE name = 'HR Department'),
--    'system'
--);
--
---- addresses
--INSERT INTO addresses (
--    line1, city, state, pin_code, country, type, employee_id, created_by
--)
--VALUES
--(
--    '123 MG Road', 'Mumbai', 'Maharashtra',
--    '400001', 'India', 'PERMANENT',
--    (SELECT id FROM employees WHERE email = 'ravi.sharma@example.com'),
--    'system'
--),
--(
--    '45 Park Lane', 'Pune', 'Maharashtra',
--    '411001', 'India', 'CURRENT',
--    (SELECT id FROM employees WHERE email = 'anita.verma@example.com'),
--    'system'
--);




