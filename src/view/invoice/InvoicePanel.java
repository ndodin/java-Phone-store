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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InvoicePanel extends JPanel {

    private ProductService productService;
    private CustomerService customerService;
    private InvoiceService invoiceService;

    private List<Product> productList;
    private List<Customer> customerList;
    private List<InvoiceDetail> cart;

    private JComboBox<String> cbCustomer;
    private JLabel lblCurrentCustomer;
    private JPanel panelCurrentCustomer;
    private JComboBox<String> cbProduct;
    private JTextField txtQuantity;
    private JButton btnAddToCart;
    private JButton btnHistory;

    private JTable tableCart;
    private DefaultTableModel cartModel;
    private JLabel lblTotal;
    private JButton btnCheckout;
    private JButton btnClear;

    public InvoicePanel() {
        productService = new ProductService();
        customerService = new CustomerService();
        invoiceService = new InvoiceService();
        cart = new ArrayList<>();
        productList = productService.getAll();
        customerList = customerService.getAll();

        initComponents();
        loadCustomers();
        loadProducts();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- TOP HEADER ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Invoice (POS)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        btnHistory = new JButton("History");
        btnHistory.setFocusPainted(false);
        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(btnHistory, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- MAIN BODY ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 15);

        // --- LEFT PANEL ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Create Order", 0, 0, new Font("Arial", Font.BOLD, 14)));

        JPanel formPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        cbCustomer = new JComboBox<>();
        cbCustomer.setBackground(Color.WHITE);
        
        panelCurrentCustomer = new JPanel(new BorderLayout());
        panelCurrentCustomer.setBackground(new Color(225, 240, 255));
        panelCurrentCustomer.setBorder(new EmptyBorder(10, 10, 10, 10));
        lblCurrentCustomer = new JLabel("Current Customer: ");
        lblCurrentCustomer.setForeground(new Color(0, 102, 204));
        panelCurrentCustomer.add(lblCurrentCustomer, BorderLayout.CENTER);
        panelCurrentCustomer.setVisible(false);

        cbProduct = new JComboBox<>();
        cbProduct.setBackground(Color.WHITE);
        txtQuantity = new JTextField("1");
        
        btnAddToCart = new JButton("Add To Cart");
        btnAddToCart.setBackground(new Color(33, 150, 243)); // Blue
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setOpaque(true);             
        btnAddToCart.setBorderPainted(false);
  
        formPanel.add(new JLabel("Customer:"));
        formPanel.add(cbCustomer);
        formPanel.add(panelCurrentCustomer);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(cbProduct);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(txtQuantity);
        
        JPanel btnAddPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        btnAddPanel.add(btnAddToCart);

        leftPanel.add(formPanel);
        leftPanel.add(btnAddPanel);
        
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        mainPanel.add(leftPanel, gbc);

        // --- RIGHT PANEL ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Shopping Cart", 0, 0, new Font("Arial", Font.BOLD, 14)));
        
        String[] columns = {"Product", "Qty", "Price", "Subtotal", "Action"};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; 
            }
        };
        tableCart = new JTable(cartModel);
        tableCart.setRowHeight(35);
        tableCart.getColumn("Action").setCellRenderer(new RemoveButtonRenderer());
        tableCart.getColumn("Action").setCellEditor(new RemoveButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(tableCart);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(245, 245, 245));
        totalPanel.setBorder(new EmptyBorder(15, 10, 15, 10));
        lblTotal = new JLabel("Total: $0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(lblTotal, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnClear = new JButton("Clear");
        btnClear.setBackground(Color.WHITE);
        btnClear.setFocusPainted(false);
        
        btnCheckout = new JButton("Checkout");
        btnCheckout.setBackground(new Color(76, 175, 80)); // Green
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setOpaque(true);              
        btnCheckout.setBorderPainted(false);

        buttonPanel.add(btnClear);
        buttonPanel.add(btnCheckout);

        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(rightPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // --- EVENTS ---
        btnAddToCart.addActionListener(e -> addToCart());
        btnCheckout.addActionListener(e -> checkout());
        btnClear.addActionListener(e -> clearCart());
        
    }

    private void loadCustomers() {
        cbCustomer.removeAllItems();
        for (Customer c : customerList) {
            cbCustomer.addItem(c.getName());
        }
    }

    private void loadProducts() {
        cbProduct.removeAllItems();
        cbProduct.addItem("-- Select --");
        for (Product p : productList) {
            cbProduct.addItem(p.getName());
        }
    }

    private void addToCart() {
        try {
            if (cbProduct.getSelectedIndex() == 0) return;
            int productIndex = cbProduct.getSelectedIndex() - 1;
            Product p = productList.get(productIndex);
            int quantity = Integer.parseInt(txtQuantity.getText());

            if (quantity <= 0 || quantity > p.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity or Out of stock");
                return;
            }

            InvoiceDetail d = new InvoiceDetail();
            d.setProductId(p.getId());
            d.setProductName(p.getName());
            d.setQuantity(quantity);
            d.setPrice(p.getPrice());
            cart.add(d);
            loadCart();

            // Lock customer & update UI
            cbCustomer.setEnabled(false);
            panelCurrentCustomer.setVisible(true);
            lblCurrentCustomer.setText("Current Customer: " + cbCustomer.getSelectedItem());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void loadCart() {
        cartModel.setRowCount(0);
        double total = 0;
        for (InvoiceDetail d : cart) {
            Object[] row = {
                    d.getProductName(),
                    d.getQuantity(),
                    "$" + d.getPrice(),
                    "$" + d.getSubtotal(),
                    "Remove"
            };
            cartModel.addRow(row);
            total += d.getSubtotal();
        }
        lblTotal.setText("Total: $" + total);
    }

    private void checkout() {
        try {
            if (cart.isEmpty()) return;
            int customerIndex = cbCustomer.getSelectedIndex();
            Customer c = customerList.get(customerIndex);
            Invoice invoice = new Invoice();
            invoice.setCustomerId(c.getId());
            if(UserSession.getInstance() != null && UserSession.getInstance().getUser() != null) {
                invoice.setUserId(UserSession.getInstance().getUser().getId());
            }
            double total = cart.stream().mapToDouble(InvoiceDetail::getSubtotal).sum();
            invoice.setTotal(total);

            boolean result = invoiceService.checkout(invoice, cart);
            if (result) {
                JOptionPane.showMessageDialog(this, "Checkout success");
                clearCart();
                productList = productService.getAll();
                loadProducts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCart() {
        cart.clear();
        loadCart();
        cbCustomer.setEnabled(true);
        panelCurrentCustomer.setVisible(false);
    }

    // --- INNER CLASSES FOR TABLE BUTTON ---
    class RemoveButtonRenderer extends JButton implements TableCellRenderer {
        public RemoveButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(244, 67, 54)); // Red
            setForeground(Color.WHITE);
            setOpaque(true);             
            setBorderPainted(false);
            //setFocusPainted(false);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Remove" : value.toString());
            return this;
        }
    }

    class RemoveButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private int clickedRow;
    private JTable table; // Lưu tham chiếu tới table

    public RemoveButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Remove");
        button.setOpaque(true);
        button.setBackground(new Color(244, 67, 54));
        button.setForeground(Color.WHITE);
        
        
        button.addActionListener(e -> {
            fireEditingStopped();

            if (clickedRow >= 0 && clickedRow < cart.size()) {
                cart.remove(clickedRow);
                loadCart();

                if (cart.isEmpty()) {
                    cbCustomer.setEnabled(true);
                    panelCurrentCustomer.setVisible(false);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.clickedRow = table.convertRowIndexToModel(row); 
        button.setText((value == null) ? "Remove" : value.toString());
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
}