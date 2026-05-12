package dao;

import model.Invoice;
import model.InvoiceDetail;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    // =========================================
    // CHECKOUT
    // =========================================

    public boolean checkout(
            Invoice invoice,
            List<InvoiceDetail> details
    ) {

        Connection conn = null;

        try {

            conn = DBConnection.getConnection();

            // TRANSACTION
            conn.setAutoCommit(false);

            // =====================================
            // INSERT INVOICE
            // =====================================

            String invoiceSql =
                    "INSERT INTO Invoices(customer_id, user_id, total, status) "
                            + "VALUES (?, ?, ?, ?)";

            PreparedStatement invoicePs =
                    conn.prepareStatement(
                            invoiceSql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            invoicePs.setInt(1, invoice.getCustomerId());

            invoicePs.setInt(2, invoice.getUserId());

            invoicePs.setDouble(3, invoice.getTotal());

            invoicePs.setString(4, invoice.getStatus());

            invoicePs.executeUpdate();

            // =====================================
            // GET GENERATED ID
            // =====================================

            ResultSet generatedKeys =
                    invoicePs.getGeneratedKeys();

            int invoiceId = 0;

            if (generatedKeys.next()) {

                invoiceId = generatedKeys.getInt(1);
            }

            // =====================================
            // INSERT DETAILS
            // =====================================

            String detailSql =
                    "INSERT INTO InvoiceDetails(invoice_id, product_id, quantity, price) "
                            + "VALUES (?, ?, ?, ?)";

            PreparedStatement detailPs =
                    conn.prepareStatement(detailSql);

            // =====================================
            // UPDATE STOCK
            // =====================================

            String stockSql =
                    "UPDATE Products "
                            + "SET quantity = quantity - ? "
                            + "WHERE id=?";

            PreparedStatement stockPs =
                    conn.prepareStatement(stockSql);

            for (InvoiceDetail d : details) {

                // INSERT DETAIL
                detailPs.setInt(1, invoiceId);

                detailPs.setInt(2, d.getProductId());

                detailPs.setInt(3, d.getQuantity());

                detailPs.setDouble(4, d.getPrice());

                detailPs.executeUpdate();

                // UPDATE STOCK
                stockPs.setInt(1, d.getQuantity());

                stockPs.setInt(2, d.getProductId());

                stockPs.executeUpdate();
            }

            // COMMIT
            conn.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (conn != null) {

                    conn.rollback();
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        return false;
    }

    // =========================================
    // GET ALL INVOICES
    // =========================================

    public List<Invoice> getAll() {

        List<Invoice> list = new ArrayList<>();

        String sql =
                "SELECT * FROM Invoices ORDER BY id DESC";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Invoice i = new Invoice();

                i.setId(rs.getInt("id"));

                i.setCustomerId(rs.getInt("customer_id"));

                i.setUserId(rs.getInt("user_id"));

                i.setDate(rs.getTimestamp("date"));

                i.setTotal(rs.getDouble("total"));

                i.setStatus(rs.getString("status"));

                list.add(i);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =========================================
    // GET DETAILS BY INVOICE ID
    // =========================================

    public List<InvoiceDetail> getDetailsByInvoiceId(int invoiceId) {

        List<InvoiceDetail> list = new ArrayList<>();

        String sql =
                "SELECT * FROM InvoiceDetails WHERE invoice_id=?";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, invoiceId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                InvoiceDetail d = new InvoiceDetail();

                d.setId(rs.getInt("id"));

                d.setInvoiceId(rs.getInt("invoice_id"));

                d.setProductId(rs.getInt("product_id"));

                d.setQuantity(rs.getInt("quantity"));

                d.setPrice(rs.getDouble("price"));

                list.add(d);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =========================================
    // CANCEL INVOICE
    // =========================================

    public boolean cancelInvoice(int invoiceId) {

        Connection conn = null;

        try {

            conn = DBConnection.getConnection();

            conn.setAutoCommit(false);

            // =====================================
            // GET DETAILS
            // =====================================

            List<InvoiceDetail> details =
                    getDetailsByInvoiceId(invoiceId);

            // =====================================
            // RESTORE STOCK
            // =====================================

            String stockSql =
                    "UPDATE Products "
                            + "SET quantity = quantity + ? "
                            + "WHERE id=?";

            PreparedStatement stockPs =
                    conn.prepareStatement(stockSql);

            for (InvoiceDetail d : details) {

                stockPs.setInt(1, d.getQuantity());

                stockPs.setInt(2, d.getProductId());

                stockPs.executeUpdate();
            }

            // =====================================
            // UPDATE STATUS
            // =====================================

            String invoiceSql =
                    "UPDATE Invoices "
                            + "SET status='CANCELLED' "
                            + "WHERE id=?";

            PreparedStatement invoicePs =
                    conn.prepareStatement(invoiceSql);

            invoicePs.setInt(1, invoiceId);

            invoicePs.executeUpdate();

            conn.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (conn != null) {

                    conn.rollback();
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        return false;
    }
}