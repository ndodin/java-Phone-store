package view.invoice;

import model.Customer;
import model.Invoice;
import model.InvoiceDetail;
import model.Product;

import service.CustomerService;
import service.InvoiceService;
import service.ProductService;

import session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InvoicePanel extends JPanel {

    // =========================================
    // SERVICES
    // =========================================

    private ProductService productService;

    private CustomerService customerService;

    private InvoiceService invoiceService;

    // =========================================
    // COMPONENTS
    // =========================================

    private JTable tblProducts;

    private JTable tblCart;

    private DefaultTableModel productModel;

    private DefaultTableModel cartModel;

    private JComboBox<Customer> cbCustomers;

    private JLabel lblTotal;

    private JButton btnAddToCart;

    private JButton btnCheckout;

    // =========================================
    // DATA
    // =========================================

    private List<Product> productList;

    private List<Customer> customerList;

    private List<InvoiceDetail> cart;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public InvoicePanel() {

        productService = new ProductService();

        customerService = new CustomerService();

        invoiceService = new InvoiceService();

        cart = new ArrayList<>();

        initComponents();

        loadCustomers();

        loadProducts();
    }

    // =========================================
    // INIT
    // =========================================

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        // =====================================
        // TOP
        // =====================================

        JPanel topPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT
                ));

        topPanel.add(new JLabel("Customer:"));

        cbCustomers = new JComboBox<>();

        cbCustomers.setPreferredSize(
                new Dimension(250, 30)
        );

        topPanel.add(cbCustomers);

        lblTotal =
                new JLabel("Total: 0");

        lblTotal.setFont(
                new Font("Arial",
                        Font.BOLD,
                        20)
        );

        topPanel.add(lblTotal);

        add(topPanel, BorderLayout.NORTH);

        // =====================================
        // CENTER
        // =====================================

        JPanel centerPanel =
                new JPanel(new GridLayout(1, 2, 10, 10));

        // PRODUCT TABLE

        String[] productColumns = {
                "ID",
                "Name",
                "Price",
                "Stock"
        };

        productModel =
                new DefaultTableModel(productColumns, 0);

        tblProducts =
                new JTable(productModel);

        JScrollPane productScroll =
                new JScrollPane(tblProducts);

        centerPanel.add(productScroll);

        // CART TABLE

        String[] cartColumns = {
                "Product ID",
                "Product",
                "Quantity",
                "Price",
                "Subtotal"
        };

        cartModel =
                new DefaultTableModel(cartColumns, 0);

        tblCart =
                new JTable(cartModel);

        JScrollPane cartScroll =
                new JScrollPane(tblCart);

        centerPanel.add(cartScroll);

        add(centerPanel, BorderLayout.CENTER);

        // =====================================
        // BOTTOM
        // =====================================

        JPanel bottomPanel = new JPanel();

        btnAddToCart =
                new JButton("ADD TO CART");

        btnCheckout =
                new JButton("CHECKOUT");

        bottomPanel.add(btnAddToCart);

        bottomPanel.add(btnCheckout);

        add(bottomPanel, BorderLayout.SOUTH);

        // =====================================
        // EVENTS
        // =====================================

        btnAddToCart.addActionListener(
                e -> addToCart()
        );

        btnCheckout.addActionListener(
                e -> checkout()
        );
    }

    // =========================================
    // LOAD CUSTOMERS
    // =========================================

    private void loadCustomers() {

        customerList =
                customerService.getAll();

        cbCustomers.removeAllItems();

        for (Customer c : customerList) {

            cbCustomers.addItem(c);
        }
    }

    // =========================================
    // LOAD PRODUCTS
    // =========================================

    private void loadProducts() {

        productModel.setRowCount(0);

        productList =
                productService.getAll();

        for (Product p : productList) {

            Object[] row = {

                    p.getId(),

                    p.getName(),

                    p.getPrice(),

                    p.getQuantity()
            };

            productModel.addRow(row);
        }
    }

    // =========================================
    // ADD TO CART
    // =========================================

    private void addToCart() {

        int row =
                tblProducts.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select product first"
            );

            return;
        }

        String qtyStr =
                JOptionPane.showInputDialog(
                        this,
                        "Enter quantity:"
                );

        if (qtyStr == null) {

            return;
        }

        int qty =
                Integer.parseInt(qtyStr);

        Product p =
                productList.get(row);

        // CHECK STOCK

        if (qty > p.getQuantity()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Not enough stock"
            );

            return;
        }

        // CREATE DETAIL

        InvoiceDetail detail =
                new InvoiceDetail();

        detail.setProductId(p.getId());

        detail.setQuantity(qty);

        detail.setPrice(p.getPrice());

        cart.add(detail);

        // TABLE

        double subtotal =
                qty * p.getPrice();

        Object[] rowData = {

                p.getId(),

                p.getName(),

                qty,

                p.getPrice(),

                subtotal
        };

        cartModel.addRow(rowData);

        updateTotal();
    }

    // =========================================
    // UPDATE TOTAL
    // =========================================

    private void updateTotal() {

        double total =
                invoiceService.calculateTotal(cart);

        lblTotal.setText(
                "Total: " + total
        );
    }

    // =========================================
    // CHECKOUT
    // =========================================

    private void checkout() {

        if (cart.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cart empty"
            );

            return;
        }

        Customer customer =
                (Customer) cbCustomers.getSelectedItem();

        Invoice invoice =
                new Invoice();

        invoice.setCustomerId(
                customer.getId()
        );

        invoice.setUserId(
                UserSession.currentUser.getId()
        );

        boolean result =
                invoiceService.checkout(
                        invoice,
                        cart
                );

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Checkout success"
            );

            cart.clear();

            cartModel.setRowCount(0);

            updateTotal();

            loadProducts();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Checkout failed"
            );
        }
    }
}