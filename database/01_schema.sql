USE CHDienThoai;
GO

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

    FOREIGN KEY (customer_id) REFERENCES Customers(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE InvoiceDetails (
    id INT IDENTITY PRIMARY KEY,
    invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price FLOAT NOT NULL,

    FOREIGN KEY (invoice_id) REFERENCES Invoices(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);