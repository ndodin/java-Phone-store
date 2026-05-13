package dao;

import model.Invoice;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    // INSERT
    public int insert(Connection conn, Invoice invoice) throws Exception {
        String sql = """
                INSERT INTO Invoices
                (customer_id, user_id, total, status)
                VALUES (?, ?, ?, ?)
                """;
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1,invoice.getCustomerId());
        ps.setInt(2,invoice.getUserId());
        ps.setDouble(3,invoice.getTotal());
        ps.setString(4,invoice.getStatus());
        ps.executeUpdate();
        ResultSet rs =ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }
    
    // GET ALL
    public List<Invoice> getAll() {

        List<Invoice> list =
                new ArrayList<>();

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql = """
                    SELECT i.*,
                           c.name AS customer_name,
                           u.username
                    FROM Invoices i
                    JOIN Customers c
                    ON i.customer_id = c.id
                    JOIN Users u
                    ON i.user_id = u.id
                    ORDER BY i.id DESC
                    """;

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            while (rs.next()) {

                Invoice i = new Invoice();

                i.setId(rs.getInt("id"));

                i.setCustomerId(
                        rs.getInt("customer_id")
                );

                i.setUserId(
                        rs.getInt("user_id")
                );

                i.setDate(
                        rs.getTimestamp("date")
                );

                i.setTotal(
                        rs.getDouble("total")
                );

                i.setStatus(
                        rs.getString("status")
                );

                i.setCustomerName(
                        rs.getString("customer_name")
                );

                i.setUsername(
                        rs.getString("username")
                );

                list.add(i);
            }

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }
    
    
    public List<Invoice> getRecent(int limit) {
        List<Invoice> list = new ArrayList<>();
        String sql = """
                    SELECT TOP (?) i.*,
                           c.name AS customer_name,
                           u.username
                    FROM Invoices i
                    JOIN Customers c ON i.customer_id = c.id
                    JOIN Users u ON i.user_id = u.id
                    ORDER BY i.date DESC
                    """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Invoice i = new Invoice();
                i.setId(rs.getInt("id"));
                i.setCustomerId(rs.getInt("customer_id"));
                i.setUserId(rs.getInt("user_id"));
                i.setDate(rs.getTimestamp("date"));
                i.setTotal(rs.getDouble("total"));
                i.setStatus(rs.getString("status"));
                i.setCustomerName(rs.getString("customer_name"));
                i.setUsername(rs.getString("username"));
                list.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    public double getTodayRevenue() {
        double total = 0;
        String sql = """
                     SELECT SUM(total) AS total
                     FROM Invoices
                     WHERE CAST(date AS DATE) = CAST(GETDATE() AS DATE)
                     AND status = 'COMPLETED'
                     """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // CANCEL
    public boolean cancelInvoice(
            int invoiceId
    ) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql = """
                    UPDATE Invoices
                    SET status = 'CANCELLED'
                    WHERE id = ?
                    """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, invoiceId);

            int result =
                    ps.executeUpdate();

            conn.close();

            return result > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}