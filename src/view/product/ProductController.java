package view.product;

import java.awt.event.*;
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
            case "ADD":
                handleAdd();
                break;
            case "EDIT":
                handleEdit();
                break;
            case "DELETE":
                handleDelete();
                break;
            case "SEARCH":
                handleSearch();
                break;
            case "REFRESH":
                handleRefresh();
                break;
        }
    }

    private void handleAdd() {
        ProductDetail dialog = new ProductDetail(
                SwingUtilities.getWindowAncestor(view) instanceof java.awt.Frame
                ? (java.awt.Frame) SwingUtilities.getWindowAncestor(view)
                : null,
                "Add New Product",
                null
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                service.insert(dialog.getProduct());
                view.refreshTable(service.getAll());
                view.showStatus("Product added successfully!", true);
            } catch (Exception ex) {
                view.showStatus("Error: " + ex.getMessage(), false);
            }
        }
    }

    private void handleEdit() {
        int row = view.getSelectedModelRow();
        if (row == -1) {
            view.showStatus("Please select a product to edit.", false);
            return;
        }
        int id = (int) view.getModel().getValueAt(row, 0);
        Product p = service.getById(id);
        if (p == null) {
            return;
        }

        ProductDetail dialog = new ProductDetail(
                SwingUtilities.getWindowAncestor(view) instanceof java.awt.Frame
                ? (java.awt.Frame) SwingUtilities.getWindowAncestor(view)
                : null,
                "Edit Product",
                p
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                service.update(dialog.getProduct());
                view.refreshTable(service.getAll());
                view.showStatus("Product updated successfully!", true);
            } catch (Exception ex) {
                view.showStatus("Error: " + ex.getMessage(), false);
            }
        }
    }

    private void handleDelete() {
        int row = view.getSelectedModelRow();
        if (row == -1) {
            view.showStatus("Please select a product to delete.", false);
            return;
        }
        String name = view.getModel().getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Delete product \"" + name + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) view.getModel().getValueAt(row, 0);
            service.delete(id);
            view.refreshTable(service.getAll());
            view.showStatus("Product \"" + name + "\" deleted.", false);
        }
    }

    private void handleSearch() {
        String keyword = view.getSearchText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            view.refreshTable(service.getAll());
        } else {
            java.util.List<Product> filtered = new java.util.ArrayList<>();
            for (Product p : service.getAll()) {
                if (p.getName().toLowerCase().contains(keyword)) {
                    filtered.add(p);
                }
            }
            view.refreshTable(filtered);
            view.showStatus(filtered.size() + " result(s) found.", true);
        }
    }

    private void handleRefresh() {
        view.clearSearch();
        view.refreshTable(service.getAll());
        view.showStatus("Data refreshed.", true);
    }
}
