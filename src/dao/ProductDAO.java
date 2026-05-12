package dao;

import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // =========================================
    // GET ALL PRODUCTS
    // =========================================

    public List<Product> getAll() {

        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Products";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = new Product();

                p.setId(rs.getInt("id"));

                p.setName(rs.getString("name"));

                p.setPrice(rs.getDouble("price"));

                p.setQuantity(rs.getInt("quantity"));

                list.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =========================================
    // INSERT PRODUCT
    // =========================================

    public boolean insert(Product p) {

        String sql =
                "INSERT INTO Products(name, price, quantity) VALUES (?, ?, ?)";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, p.getName());

            ps.setDouble(2, p.getPrice());

            ps.setInt(3, p.getQuantity());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================================
    // UPDATE PRODUCT
    // =========================================

    public boolean update(Product p) {

        String sql =
                "UPDATE Products SET name=?, price=?, quantity=? WHERE id=?";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, p.getName());

            ps.setDouble(2, p.getPrice());

            ps.setInt(3, p.getQuantity());

            ps.setInt(4, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================================
    // DELETE PRODUCT
    // =========================================

    public boolean delete(int id) {

        String sql =
                "DELETE FROM Products WHERE id=?";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}