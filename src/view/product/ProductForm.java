package view.product;

import model.Product;
import service.ProductService;

import javax.swing.*;
import java.awt.*;

public class ProductForm extends JDialog {

    private JTextField txtName;

    private JTextField txtPrice;

    private JTextField txtQuantity;

    private JButton btnSave;

    private Product product;

    private ProductService productService;

    private ProductPanel parentPanel;

    public ProductForm(
            Product product,
            ProductPanel parentPanel
    ) {

        this.product = product;

        this.parentPanel = parentPanel;

        productService = new ProductService();

        initComponents();

        fillData();

        initDialog();
    }

    private void initComponents() {

        setLayout(new GridLayout(8, 1, 10, 10));

        add(new JLabel("Product Name"));

        txtName = new JTextField();

        add(txtName);

        add(new JLabel("Price"));

        txtPrice = new JTextField();

        add(txtPrice);

        add(new JLabel("Quantity"));

        txtQuantity = new JTextField();

        add(txtQuantity);

        btnSave = new JButton("SAVE");

        add(btnSave);

        btnSave.addActionListener(e -> saveProduct());
    }

    // =========================================
    // FILL DATA
    // =========================================

    private void fillData() {

        if (product == null) {

            return;
        }

        txtName.setText(product.getName());

        txtPrice.setText(
                String.valueOf(product.getPrice())
        );

        txtQuantity.setText(
                String.valueOf(product.getQuantity())
        );
    }

    // =========================================
    // SAVE
    // =========================================

    private void saveProduct() {

        try {

            String name =
                    txtName.getText().trim();

            double price =
                    Double.parseDouble(
                            txtPrice.getText().trim()
                    );

            int quantity =
                    Integer.parseInt(
                            txtQuantity.getText().trim()
                    );

            // ADD

            if (product == null) {

                Product p = new Product();

                p.setName(name);

                p.setPrice(price);

                p.setQuantity(quantity);

                boolean result =
                        productService.insert(p);

                if (result) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Add success"
                    );

                    parentPanel.loadTable();

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Add failed"
                    );
                }
            }

            // UPDATE

            else {

                product.setName(name);

                product.setPrice(price);

                product.setQuantity(quantity);

                boolean result =
                        productService.update(product);

                if (result) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Update success"
                    );

                    parentPanel.loadTable();

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Update failed"
                    );
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid data"
            );
        }
    }

    private void initDialog() {

        setTitle(
                product == null
                        ? "Add Product"
                        : "Edit Product"
        );

        setSize(400, 350);

        setLocationRelativeTo(null);

        setModal(true);
    }
}