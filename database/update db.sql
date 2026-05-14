USE CHDienThoai;
GO

DROP TABLE IF EXISTS InvoiceDetails;
DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Users;
GO

CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('admin','staff')) NOT NULL,
    status INT DEFAULT 1 -- 1: Active, 0: Disabled
);

CREATE TABLE Products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(18, 2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    status INT DEFAULT 1 -- 1: Active, 0: Soft Deleted
);


CREATE TABLE Customers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    status INT DEFAULT 1
);

CREATE TABLE Invoices (
    id INT IDENTITY(1001,1) PRIMARY KEY, 
    customer_id INT NOT NULL,
    user_id INT NOT NULL,
    date DATETIME DEFAULT GETDATE(),
    total DECIMAL(18, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'COMPLETED', -- COMPLETED, CANCELLED

    FOREIGN KEY (customer_id) REFERENCES Customers(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE InvoiceDetails (
    id INT IDENTITY(1,1) PRIMARY KEY,
    invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(18, 2) NOT NULL,

    FOREIGN KEY (invoice_id) REFERENCES Invoices(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);
GO

INSERT INTO Users (username, password, role, status) VALUES 
('admin_master', 'hash_admin_123', 'admin', 1),
('admin2', '123', 'admin', 1),
('staff', '1234', 'staff', 1),
('clara_staff', 'hash_staff_456', 'staff', 1),
('james_sales', 'hash_sales_789', 'staff', 0);

INSERT INTO Customers (name, phone, status) VALUES 
('Alice Johnson', '0901112233', 1),
('Bob Smith', '0904445566', 1),
('Charlie Brown', '0907778899', 1),
('Diana Prince', '0912223344', 1),
('Martin Edward', '0912223678', 0),
('Edward Norton', '0915556677', 1);

INSERT INTO Products (name, price, quantity, status) VALUES 
('iPhone 15 Pro Max', 1200.00, 15, 1),
('Samsung Galaxy S24 Ultra', 1150.00, 5, 1),  -- Low Stock
('Google Pixel 8 Pro', 900.00, 12, 1),
('Xiaomi 14 Ultra', 1000.00, 3, 1),           -- Low Stock
('Oppo Find X7 Ultra', 950.00, 20, 1),
('Asus ROG Phone 8', 1100.00, 8, 1),          -- Low Stock
('iPad Air M2', 600.00, 25, 1),
('AirPods Pro Gen 2', 250.00, 50, 1),
('Samsung Galaxy Buds 3', 180.00, 40, 1),
('GaN 65W Fast Charger', 45.00, 100, 1),
('iPhone MagSafe Case', 30.00, 150, 1),
('Screen Protector', 15.00, 200, 1);

INSERT INTO Invoices (customer_id, user_id, date, total, status) VALUES 
(1, 2, GETDATE(), 1450.00, 'COMPLETED'), -- Alice
(2, 3, GETDATE(), 1150.00, 'COMPLETED'), -- Bob
(3, 2, GETDATE(), 900.00, 'CANCELLED'),  -- Charlie
(4, 3, GETDATE(), 250.00, 'COMPLETED'),  -- Diana
(5, 2, GETDATE(), 1030.00, 'COMPLETED'); -- Edward

INSERT INTO InvoiceDetails (invoice_id, product_id, quantity, price) VALUES 
-- Invoice 1001: Alice (iPhone + AirPods)
(1001, 1, 1, 1200.00), 
(1001, 8, 1, 250.00),

-- Invoice 1002: Bob (Samsung S24 Ultra)
(1002, 2, 1, 1150.00),

-- Invoice 1003: Charlie (Google Pixel - Cancelled)
(1003, 3, 1, 900.00),

-- Invoice 1004: Diana (AirPods Pro)
(1004, 8, 1, 250.00),

-- Invoice 1005: Edward (Xiaomi + Case)
(1005, 4, 1, 1000.00),
(1005, 11, 1, 30.00);