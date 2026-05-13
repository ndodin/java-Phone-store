package service;

import dao.CustomerDAO;
import model.Customer;

import java.util.List;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService() {

        customerDAO = new CustomerDAO();
    }

    // =========================================
    // GET ALL
    // =========================================

    public List<Customer> getAll() {

        return customerDAO.getAll();
    }
    
    public int getTotalCustomers() {
        return customerDAO.getTotalCustomers();
    }

    // =========================================
    // INSERT
    // =========================================

    public boolean insert(Customer c) {

        if (c.getName() == null ||
                c.getName().trim().isEmpty()) {

            return false;
        }

        return customerDAO.insert(c);
    }

    // =========================================
    // UPDATE
    // =========================================

    public boolean update(Customer c) {

        if (c.getId() <= 0) {

            return false;
        }

        return customerDAO.update(c);
    }

    // =========================================
    // DELETE
    // =========================================

    public boolean delete(int id) {

        if (id <= 0) {

            return false;
        }

        return customerDAO.delete(id);
    }
}