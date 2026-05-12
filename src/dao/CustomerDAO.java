package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // =========================================
    // GET ALL CUSTOMERS
    // =========================================

    public List<Customer> getAll() {

        List<Customer> list = new ArrayList<>();

        String sql = "SELECT * FROM Customers";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

            	Customer c = new Customer();

                c.setId(rs.getInt("id"));

                c.setName(rs.getString("name"));

                c.setPhone(rs.getString("phone"));

                list.add(c);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =========================================
    // INSERT CUSTOMER
    // =========================================

    public boolean insert(Customer c) {

        String sql =
                "INSERT INTO Customers(name, phone) VALUES (?, ?)";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, c.getName());

            ps.setString(2, c.getPhone());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================================
    // UPDATE CUSTOMER
    // =========================================

    public boolean update(Customer c) {

        String sql =
                "UPDATE Customers SET name=?, phone=? WHERE id=?";

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, c.getName());

            ps.setString(2, c.getPhone());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================================
    // DELETE 	CUSTOMER
    // =========================================

    public boolean delete(int id) {

        String sql =
                "DELETE FROM Customers WHERE id=?";

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