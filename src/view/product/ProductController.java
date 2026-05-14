package view.product;

import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import model.*;
import service.*;


public class ProductController implements ActionListener {

    private ProductPanel view;
    private ProductService service;

    public ProductController(ProductPanel view, ProductService service) {
        this.view = view;
        this.service = service;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "ADD" -> handleAdd();
            case "EDIT" -> handleEdit();
            case "DELETE" -> handleDelete();
            case "SEARCH" -> handleSearch();
            case "REFRESH" -> handleRefresh();
        }
    }
    
    private void handleAdd() {
        ProductForm dialog = new ProductForm(null, "Add New Product", null);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            if (service.insert(dialog.getProduct())) {
                handleRefresh();
                JOptionPane.showMessageDialog(view, "Add product success!");
            } else {
                JOptionPane.showMessageDialog(view, "Error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Select product to edit!");
            return;
        }
        
        int id = (int) view.getTable().getValueAt(row, 0);
        Product p = service.getById(id);
        
        if (p != null) {
            ProductForm dialog = new ProductForm(null, "Edit Product", p);
            dialog.setVisible(true);
            if (dialog.isSaved() && service.update(dialog.getProduct())) {
                handleRefresh();
                JOptionPane.showMessageDialog(view, "Update success!");
            }
        }
    }
    
    private void handleDelete() {
    	String role =
                session.UserSession
                        .getInstance()
                        .getUser()
                        .getRole();
        if (!role.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(
                    view,
                    "Access denied!"
            );
            return;
        }
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Select products to remove!");
            return;
        }
      
        int id = (int) view.getTable().getValueAt(row, 0);
        String name = view.getTable().getValueAt(row, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(view,
                "Delete product ID \"" + name + "\"?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (service.delete(id)) {
                handleRefresh();
                System.out.println("Delete product: " + name);
            }
        }
    }
    
    private void handleSearch() {
        String keyword = view.getSearchText().toLowerCase().trim();
        List<Product> all = service.getAll();
        
        if (keyword.isEmpty()) {
            view.refreshTable(all);
        } else {
            List<Product> filtered = all.stream().filter(p -> p.getName().toLowerCase().contains(keyword)).collect(Collectors.toList());
            view.refreshTable(filtered);
        }
    }
    
    private void handleRefresh() {
        List<Product> data = service.getAll();
        if (data != null) {
            view.refreshTable(data);
        }
    }
}
