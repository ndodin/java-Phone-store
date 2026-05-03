CREATE TABLE Users (
    id INT IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('admin','staff')) NOT NULL
);
CREATE TABLE Products (
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0)
);
CREATE TABLE Customers (
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);
CREATE TABLE Invoices (
    id INT IDENTITY PRIMARY KEY,
    customer_id INT NOT NULL,
    user_id INT NOT NULL,
    date DATETIME DEFAULT GETDATE(),
    total FLOAT NOT NULL,

    FOREIGN KEY (customer_id) 
        REFERENCES Customers(id)
        ON DELETE NO ACTION,

    FOREIGN KEY (user_id) 
        REFERENCES Users(id)
        ON DELETE NO ACTION
);
CREATE TABLE InvoiceDetails (
    id INT IDENTITY PRIMARY KEY,
    invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price FLOAT NOT NULL,

    FOREIGN KEY (invoice_id) 
        REFERENCES Invoices(id)
        ON DELETE CASCADE,

    FOREIGN KEY (product_id) 
        REFERENCES Products(id)
        ON DELETE NO ACTION
);

INSERT INTO Users(username, password, role)
VALUES 
('admin','123','admin'),
('staff','123','staff');

INSERT INTO Products(name, price, quantity)
VALUES 
('iPhone 15 Pro Max', 32000000, 20),
('Samsung Galaxy S24 Ultra', 28000000, 15),
('MacBook Air M2', 24500000, 8),
('iPad Pro M2', 21000000, 12),
('Sony WH-1000XM5', 7500000, 30),
('AirPods Pro Gen 2', 5800000, 50),
('Apple Watch Series 9', 10500000, 25),
('Laptop Dell XPS 13', 35000000, 5),
('Chuột Logitech MX Master 3S', 2200000, 40),
('Bàn phím Keychron K2', 1800000, 20),
('Samsung Galaxy Z Fold 5', 36000000, 7),
('Google Pixel 8 Pro', 22000000, 10),
('Sạc dự phòng Anker 20000mAh', 1200000, 100),
('Loa Marshall Emberton II', 3900000, 15),
('Màn hình LG DualUp', 15000000, 4),
('Ổ cứng SSD Samsung T7 1TB', 2800000, 60),
('Nintendo Switch OLED', 8500000, 18),
('Thẻ nhớ SanDisk 128GB', 450000, 200),
('Cáp sạc USB-C Apple 2m', 650000, 150);

INSERT INTO Customers(name, phone)
VALUES 
('Lê Văn Tư', '0912345678'),
('Hoàng Thảo', '0905123456'),
('Nguyễn Di', '0934556677'),
('Võ Thị Bốn', '0977112233'),
('Đặng Côn', '0944332211'),
('Đỗ Bình Minh', '0922883344'),
('Đinh Hoàng', '0988001122'),
('Lâm Sàng', '0977441122'),
('Tạ Hiên', '0966335577');

SELECT * FROM Users;
SELECT * FROM Products;
SELECT * FROM Customers;