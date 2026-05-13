package dao;

import model.InvoiceDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {

    // INSERT
    public boolean insert(
            Connection conn,
            InvoiceDetail d
    ) throws Exception {

        String sql = """
                INSERT INTO InvoiceDetails
                (invoice_id, product_id,
                 quantity, price)
                VALUES (?, ?, ?, ?)
                """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, d.getInvoiceId());

        ps.setInt(2, d.getProductId());

        ps.setInt(3, d.getQuantity());

        ps.setDouble(4, d.getPrice());

        int result = ps.executeUpdate();

        return result > 0;
    }

    // GET BY INVOICE
    public List<InvoiceDetail> getByInvoiceId(
            int invoiceId
    ) {

        List<InvoiceDetail> list =
                new ArrayList<>();

        try {

            Connection conn =
                    util.DBConnection.getConnection();

            String sql = """
                    SELECT d.*,
                           p.name AS product_name
                    FROM InvoiceDetails d
                    JOIN Products p
                    ON d.product_id = p.id
                    WHERE invoice_id = ?
                    """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, invoiceId);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                InvoiceDetail d =
                        new InvoiceDetail();

                d.setId(rs.getInt("id"));

                d.setInvoiceId(
                        rs.getInt("invoice_id")
                );

                d.setProductId(
                        rs.getInt("product_id")
                );

                d.setQuantity(
                        rs.getInt("quantity")
                );

                d.setPrice(
                        rs.getDouble("price")
                );

                d.setProductName(
                        rs.getString("product_name")
                );

                list.add(d);
            }

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }
}