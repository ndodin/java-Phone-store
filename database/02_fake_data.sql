USE CHDienThoai;
GO

INSERT INTO Users(username, password, role)
VALUES 
('admin','123','admin'),
('staff','123','staff');

INSERT INTO Products(name, price, quantity)
VALUES 
('iPhone 15 Pro Max', 32000000, 20),
('Samsung Galaxy S24 Ultra', 28000000, 15);

INSERT INTO Customers(name, phone)
VALUES 
('Lê Văn Tư', '0912345678'),
('Hoàng Thảo', '0905123456');