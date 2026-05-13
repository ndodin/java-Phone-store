USE CHDienThoai;
GO

-- Xóa bảng theo thứ tự ngược lại của khóa ngoại để tránh lỗi ràng buộc
DROP TABLE IF EXISTS InvoiceDetails;
DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Users;
GO

-- 1. Bảng Người dùng
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('admin','staff')) NOT NULL,
    status INT DEFAULT 1 -- 1: Active, 0: Disabled
);

-- 2. Bảng Sản phẩm
CREATE TABLE Products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(18, 2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    status INT DEFAULT 1 -- 1: Active, 0: Soft Deleted
);

-- 3. Bảng Khách hàng
CREATE TABLE Customers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    status INT DEFAULT 1
);

-- 4. Bảng Hóa đơn
CREATE TABLE Invoices (
    id INT IDENTITY(1001,1) PRIMARY KEY, -- Bắt đầu từ 1001 cho chuyên nghiệp
    customer_id INT NOT NULL,
    user_id INT NOT NULL,
    date DATETIME DEFAULT GETDATE(),
    total DECIMAL(18, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'COMPLETED', -- COMPLETED, CANCELLED

    FOREIGN KEY (customer_id) REFERENCES Customers(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- 5. Bảng Chi tiết hóa đơn
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

-- INSERT USERS (Admin & Staff)
INSERT INTO Users (username, password, role) VALUES 
('admin', '123', 'admin'), ('staff1', '123', 'staff'), ('staff2', '123', 'staff');

-- INSERT PRODUCTS (20 items)
INSERT INTO Products (name, price, quantity) VALUES 
(N'iPhone 15 Pro Max', 34000000, 50), (N'iPhone 14 Pro', 25000000, 30),
(N'Samsung Galaxy S24 Ultra', 30000000, 40), (N'Samsung Z Fold 5', 32000000, 20),
(N'Xiaomi 14 Pro', 18000000, 60), (N'Oppo Find X7', 17000000, 25),
(N'Google Pixel 8 Pro', 21000000, 15), (N'Realme GT5', 12000000, 45),
(N'Asus ROG Phone 8', 24000000, 10), (N'Sony Xperia 1 V', 28000000, 12),
(N'Huawei P60 Pro', 19000000, 18), (N'Vivo X100 Pro', 20000000, 22),
(N'Nokia G42', 5000000, 100), (N'iPad Pro M2', 22000000, 35),
(N'MacBook Air M2', 26000000, 20), (N'Apple Watch Ultra 2', 18000000, 40),
(N'AirPods Pro 2', 5500000, 80), (N'Samsung Buds 2 Pro', 3000000, 90),
(N'Sạc dự phòng Anker 20k', 1200000, 150), (N'Ốp lưng iPhone 15', 500000, 300);

-- INSERT CUSTOMERS (20 items)
INSERT INTO Customers (name, phone) VALUES 
(N'Nguyễn Văn A', '0901234567'), (N'Trần Thị B', '0902234567'),
(N'Lê Văn C', '0903234567'), (N'Phạm Thị D', '0904234567'),
(N'Hoàng Văn E', '0905234567'), (N'Vũ Thị F', '0906234567'),
(N'Đặng Văn G', '0907234567'), (N'Bùi Thị H', '0908234567'),
(N'Đỗ Văn I', '0909234567'), (N'Ngô Thị K', '0910234567'),
(N'Lý Văn L', '0911234567'), (N'Dương Thị M', '0912234567'),
(N'Chu Văn N', '0913234567'), (N'Phan Thị P', '0914234567'),
(N'Tạ Văn Q', '0915234567'), (N'Hồ Thị R', '0916234567'),
(N'Mai Văn S', '0917234567'), (N'Trịnh Thị T', '0918234567'),
(N'Đoàn Văn U', '0919234567'), (N'Quách Thị V', '0920234567');

-- INSERT INVOICES (Mẫu 5 hóa đơn để test lịch sử)
INSERT INTO Invoices (customer_id, user_id, total, status) VALUES 
(1, 2, 34000000, 'COMPLETED'),
(2, 2, 25000000, 'COMPLETED'),
(3, 3, 30000000, 'CANCELLED'), -- Hóa đơn bị hủy
(4, 2, 6000000, 'COMPLETED');

-- INSERT INVOICE DETAILS
INSERT INTO InvoiceDetails (invoice_id, product_id, quantity, price) VALUES 
(1001, 1, 1, 34000000),
(1002, 2, 1, 25000000),
(1003, 3, 1, 30000000),
(1004, 17, 1, 5500000),
(1004, 20, 1, 500000);
GO