package service;

import dao.ProductDAO;
import java.util.List;
import model.Product;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    // =========================================
    // GET ALL
    // =========================================
    public List<Product> getAll() {

        return productDAO.getAll();
    }

    public Product getById(int id) {
        return productDAO.getById(id);
    }

    // =========================================
    // INSERT
    // =========================================
    public boolean insert(Product p) {

        // VALIDATE
        if (p.getName() == null
                || p.getName().trim().isEmpty()) {

            return false;
        }

        if (p.getPrice() <= 0) {

            return false;
        }

        if (p.getQuantity() < 0) {

            return false;
        }

        return productDAO.insert(p);
    }

    // =========================================
    // UPDATE
    // =========================================
    public boolean update(Product p) {

        if (p.getId() <= 0) {

            return false;
        }

        return productDAO.update(p);
    }

    // =========================================
    // DELETE
    // =========================================
    public boolean delete(int id) {

        if (id <= 0) {

            return false;
        }

        return productDAO.delete(id);
    }
}
