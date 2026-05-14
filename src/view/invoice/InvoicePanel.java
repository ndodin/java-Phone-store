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
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private JComboBox<String> cbProduct;

    private JTextField txtQuantity;

    private JLabel lblCurrentCustomer;
    private JLabel lblTotal;

    private JPanel panelCurrentCustomer;

    private JButton btnAddToCart;
    private JButton btnCheckout;
    private JButton btnClear;
    private JButton btnHistory;

    private JTable tableCart;
    private DefaultTableModel cartModel;

    // STYLE
    private static final Color BG = new Color(245, 247, 250);

    private static final Color CARD_BG = Color.WHITE;

    private static final Color ACCENT = new Color(13, 110, 253);

    private static final Color SUCCESS = new Color(25, 135, 84);

    private static final Color DANGER = new Color(220, 53, 69);

    private static final Color BORDER_C = new Color(222, 226, 230);

    private static final Color TEXT_MAIN = new Color(33, 37, 41);

    private static final Color TEXT_SUB = new Color(108, 117, 125);

    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 13);

    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    // =====================================================

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

    // =====================================================

    private void initComponents() {

        setLayout(new BorderLayout(15, 15));

        setBackground(BG);

        setBorder(new EmptyBorder(20, 20, 20, 20));

        // HEADER

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Invoice (POS)");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        lblTitle.setForeground(TEXT_MAIN);

        btnHistory =
                createButton(
                        "History",
                        ACCENT,
                        Color.WHITE
                );

        topPanel.add(lblTitle, BorderLayout.WEST);

        topPanel.add(btnHistory, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // MAIN

        JPanel mainPanel = new JPanel(new GridBagLayout());

        mainPanel.setBackground(BG);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        gbc.weighty = 1.0;

        gbc.insets = new Insets(0, 0, 0, 15);

        // LEFT PANEL
        JPanel leftPanel = new JPanel();

        leftPanel.setBackground(CARD_BG);

        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.Y_AXIS
                )
        );

        leftPanel.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER_C, 1, true),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel lblCreateOrder = new JLabel("Create Order");

        lblCreateOrder.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblCreateOrder.setBorder(
                new EmptyBorder(0, 0, 15, 0)
        );

        JPanel formPanel =
                new JPanel(
                        new GridLayout(8, 1, 5, 5)
                );

        formPanel.setBackground(CARD_BG);

        cbCustomer = new JComboBox<>();

        cbCustomer.setFont(MAIN_FONT);

        cbProduct = new JComboBox<>();

        cbProduct.setFont(MAIN_FONT);

        txtQuantity = createStyledField("1");

        panelCurrentCustomer = new JPanel(new BorderLayout());

        panelCurrentCustomer.setBackground(
                new Color(225, 240, 255)
        );

        panelCurrentCustomer.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        lblCurrentCustomer = new JLabel("Current Customer:");

        lblCurrentCustomer.setFont(MAIN_FONT);

        lblCurrentCustomer.setForeground(ACCENT);

        panelCurrentCustomer.add(
                lblCurrentCustomer,
                BorderLayout.CENTER
        );

        panelCurrentCustomer.setVisible(false);

        btnAddToCart =
                createButton(
                        "Add To Cart",
                        ACCENT,
                        Color.WHITE
                );

        formPanel.add(createLabel("Customer"));
        formPanel.add(cbCustomer);

        formPanel.add(panelCurrentCustomer);

        formPanel.add(createLabel("Product"));
        formPanel.add(cbProduct);

        formPanel.add(createLabel("Quantity"));
        formPanel.add(txtQuantity);

        JPanel btnPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                10
                        )
                );

        btnPanel.setBackground(CARD_BG);

        btnPanel.add(btnAddToCart);

        leftPanel.add(lblCreateOrder);

        leftPanel.add(formPanel);

        leftPanel.add(btnPanel);

        gbc.gridx = 0;

        gbc.weightx = 0.35;

        mainPanel.add(leftPanel, gbc);

        // =================================================
        // RIGHT PANEL
        // =================================================

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        rightPanel.setBackground(CARD_BG);

        rightPanel.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER_C, 1, true),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel lblCart = new JLabel("Shopping Cart");

        lblCart.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        rightPanel.add(lblCart, BorderLayout.NORTH);

        String[] columns = {
                "Product",
                "Qty",
                "Price",
                "Subtotal",
                "Action"
        };

        cartModel = new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return column == 4;
                    }
                };

        tableCart = new JTable(cartModel);

        tableCart.setFont(MAIN_FONT);

        tableCart.setRowHeight(40);

        tableCart.setSelectionBackground(
                new Color(207, 226, 255)
        );

        tableCart.setSelectionForeground(Color.BLACK);

        tableCart.setShowVerticalLines(false);

        JTableHeader th = tableCart.getTableHeader();

        th.setFont(BOLD_FONT);

        th.setPreferredSize(
                new Dimension(0, 35)
        );

        tableCart.getColumn("Action")
                .setCellRenderer(
                        new RemoveButtonRenderer()
                );

        tableCart.getColumn("Action")
                .setCellEditor(
                        new RemoveButtonEditor(
                                new JCheckBox()
                        )
                );

        JScrollPane scroll =
                new JScrollPane(tableCart);

        scroll.setBorder(BorderFactory.createEmptyBorder());

        rightPanel.add(scroll, BorderLayout.CENTER);

        // BOTTOM
        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.setBackground(CARD_BG);

        bottomPanel.setBorder(
                new EmptyBorder(15, 0, 0, 0)
        );

        lblTotal = new JLabel("Total: $0.00");

        lblTotal.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                10
                        )
                );

        actionPanel.setBackground(CARD_BG);

        btnClear =
                createButton(
                        "Clear",
                        new Color(108,117,125),
                        Color.WHITE
                );

        btnCheckout =
                createButton(
                        "Checkout",
                        SUCCESS,
                        Color.WHITE
                );

        actionPanel.add(btnClear);

        actionPanel.add(btnCheckout);

        bottomPanel.add(lblTotal, BorderLayout.NORTH);

        bottomPanel.add(actionPanel, BorderLayout.CENTER);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        gbc.gridx = 1;

        gbc.weightx = 0.65;

        gbc.insets = new Insets(0,0,0,0);

        mainPanel.add(rightPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // EVENTS
        btnAddToCart.addActionListener(
                e -> addToCart()
        );

        btnCheckout.addActionListener(
                e -> checkout()
        );

        btnClear.addActionListener(
                e -> clearCart()
        );
    }

    // =====================================================

    private JLabel createLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setFont(MAIN_FONT);

        lbl.setForeground(TEXT_SUB);

        return lbl;
    }

    // =====================================================

    private JTextField createStyledField(String text) {

        JTextField field = new JTextField(text);

        field.setFont(MAIN_FONT);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_C,1,true),
                        new EmptyBorder(8,12,8,12)
                )
        );

        field.setPreferredSize(
                new Dimension(0,40)
        );

        return field;
    }

    // =====================================================

    private JButton createButton(
            String text,
            Color bg,
            Color fg
    ) {

        JButton btn = new JButton(text);

        btn.setFont(BOLD_FONT);

        btn.setBackground(bg);

        btn.setForeground(fg);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(130, 36)
        );

        return btn;
    }

    // =====================================================

    private void loadCustomers() {

        cbCustomer.removeAllItems();

        for (Customer c : customerList) {

            cbCustomer.addItem(c.getName());
        }
    }

    // =====================================================

    private void loadProducts() {

        cbProduct.removeAllItems();

        cbProduct.addItem("-- Select --");

        for (Product p : productList) {

            cbProduct.addItem(p.getName());
        }
    }

    // =====================================================

    private void addToCart() {

        try {

            if (cbProduct.getSelectedIndex() == 0) {
                return;
            }

            int productIndex = cbProduct.getSelectedIndex() - 1;

            Product p = productList.get(productIndex);

            int quantity =
                    Integer.parseInt(
                            txtQuantity.getText()
                    );

            if (
                    quantity <= 0 ||
                    quantity > p.getQuantity()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid quantity or Out of stock"
                );

                return;
            }

            InvoiceDetail d = new InvoiceDetail();

            d.setProductId(p.getId());

            d.setProductName(p.getName());

            d.setQuantity(quantity);

            d.setPrice(p.getPrice());

            cart.add(d);

            loadCart();

            cbCustomer.setEnabled(false);

            panelCurrentCustomer.setVisible(true);

            lblCurrentCustomer.setText(
                    "Current Customer: "
                            + cbCustomer.getSelectedItem()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input"
            );
        }
    }

    // =====================================================

    private void loadCart() {

        cartModel.setRowCount(0);

        BigDecimal total =
                BigDecimal.ZERO;

        for (InvoiceDetail d : cart) {

            Object[] row = {

                    d.getProductName(),

                    d.getQuantity(),

                    "$" + moneyFormat.format(d.getPrice()),

                    "$" + moneyFormat.format(
                            d.getSubtotal()
                    ),

                    "Remove"
            };

            cartModel.addRow(row);

            total = total.add(d.getSubtotal());
        }

        lblTotal.setText(
                "Total: $"
                        + moneyFormat.format(total)
        );
    }

    // =====================================================

    private void checkout() {

        try {

            if (cart.isEmpty()) {
                return;
            }

            int customerIndex =
                    cbCustomer.getSelectedIndex();

            Customer c =
                    customerList.get(customerIndex);

            Invoice invoice =
                    new Invoice();

            invoice.setCustomerId(c.getId());

            if (
                    UserSession.getInstance() != null &&
                    UserSession.getInstance().getUser() != null
            ) {

                invoice.setUserId(
                        UserSession
                                .getInstance()
                                .getUser()
                                .getId()
                );
            }

            BigDecimal total =
                    BigDecimal.ZERO;

            for (InvoiceDetail d : cart) {

                total = total.add(
                        d.getSubtotal()
                );
            }

            invoice.setTotal(total);

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

                clearCart();

                productList =
                        productService.getAll();

                loadProducts();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================

    private void clearCart() {

        cart.clear();

        loadCart();

        cbCustomer.setEnabled(true);

        panelCurrentCustomer.setVisible(false);
    }

    // BUTTON RENDERER
    class RemoveButtonRenderer
            extends JButton
            implements TableCellRenderer {

        public RemoveButtonRenderer() {

            setOpaque(true);

            setBackground(DANGER);

            setForeground(Color.WHITE);

            setBorderPainted(false);

            setFocusPainted(false);

            setFont(BOLD_FONT);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            setText("Remove");

            return this;
        }
    }

    // BUTTON EDITOR
    class RemoveButtonEditor
            extends DefaultCellEditor {

        protected JButton button;

        private int clickedRow;

        public RemoveButtonEditor(
                JCheckBox checkBox
        ) {

            super(checkBox);

            button =
                    createButton(
                            "Remove",
                            DANGER,
                            Color.WHITE
                    );

            button.addActionListener(e -> {

                fireEditingStopped();

                if (
                        clickedRow >= 0 &&
                        clickedRow < cart.size()
                ) {

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
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {

            clickedRow = table.convertRowIndexToModel(row);

            return button;
        }

        @Override
        public Object getCellEditorValue() {

            return "Remove";
        }
    }
}