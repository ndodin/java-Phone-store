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
('clara_staff', 'hash_staff_456', 'staff', 1),
('james_sales', 'hash_sales_789', 'staff', 1);

INSERT INTO Customers (name, phone, status) VALUES 
('Alice Johnson', '0901112233', 1),
('Bob Smith', '0904445566', 1),
('Charlie Brown', '0907778899', 1),
('Diana Prince', '0912223344', 1),
('Edward Norton', '0915556677', 1);

INSERT INTO Products (name, price, quantity, status) VALUES 
('Wireless Mouse', 25.00, 100, 1),
('Mechanical Keyboard', 75.50, 50, 1),
('Gaming Monitor 24"', 150.00, 20, 1),
('USB-C Hub', 45.00, 80, 1),
('Webcam HD', 60.00, 35, 1),
('Laptop Stand', 30.00, 120, 1),
('Noise Cancelling Headphones', 199.99, 15, 1),
('External SSD 1TB', 110.00, 40, 1),
('Smartphone Tripod', 15.00, 200, 1),
('Bluetooth Speaker', 55.00, 60, 1);

INSERT INTO Invoices (customer_id, user_id, date, total, status) VALUES 
(1, 2, GETDATE(), 100.50, 'COMPLETED'),
(2, 3, GETDATE(), 150.00, 'COMPLETED'),
(3, 2, GETDATE(), 45.00, 'CANCELLED'),
(4, 3, GETDATE(), 199.99, 'COMPLETED'),
(5, 2, GETDATE(), 85.00, 'COMPLETED');

INSERT INTO InvoiceDetails (invoice_id, product_id, quantity, price) VALUES 
(1001, 1, 1, 25.00), -- Alice bought Mouse
(1001, 2, 1, 75.50), -- Alice bought Keyboard
(1002, 3, 1, 150.00),-- Bob bought Monitor
(1003, 4, 1, 45.00), -- Charlie (Cancelled)
(1004, 7, 1, 199.99),-- Diana bought Headphones
(1005, 1, 1, 25.00), -- Edward bought Mouse
(1005, 5, 1, 60.00);  -- Edward bought Webcam