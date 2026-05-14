package service;

import java.math.BigDecimal;
import java.util.List;

import dao.ProductDAO;
import model.Product;


public class ProductService {

    private ProductDAO productDAO;
    public ProductService() {
        productDAO = new ProductDAO();
    }
    //GET ALL
    public List<Product> getAll() {
        return productDAO.getAll();
    }
    public Product getById(int id) {
        return productDAO.getById(id);
    }
    public int getTotalProduct() {
        return productDAO.getTotalProducts();
    }  
    //INSERT
    public boolean insert(Product p) {
        if (p.getName() == null
                || p.getName().trim().isEmpty()) {
            return false;
        }
        if (p.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (p.getQuantity() < 0) {
            return false;
        }
        return productDAO.insert(p);
    }
    //UPDATE
    public boolean update(Product p) {
        if (p.getId() <= 0) {
            return false;
        }
        return productDAO.update(p);
    }
    //DELETE
    public boolean delete(int id) {
        if (id <= 0) {
            return false;
        }
        return productDAO.delete(id);
    }  
    //LOW STOCK
    public int getLowStockCount(int threshold) {
        return productDAO.getLowStockCount(threshold);
    }
}
