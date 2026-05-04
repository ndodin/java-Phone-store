USE CHDienThoai;
GO

ALTER TABLE Invoices
ADD status VARCHAR(20) DEFAULT 'COMPLETED';

UPDATE Invoices
SET status = 'COMPLETED'
WHERE status IS NULL;

INSERT INTO Invoices (customer_id, user_id, date, total, status)
VALUES
(1, 1, GETDATE(), 64000000, 'COMPLETED'),
(2, 2, GETDATE(), 28000000, 'CANCELLED');

INSERT INTO InvoiceDetails (invoice_id, product_id, quantity, price)
VALUES
(1, 1, 2, 32000000),
(2, 2, 1, 28000000);