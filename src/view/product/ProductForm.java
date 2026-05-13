package view.product;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import model.*;

public class ProductForm extends JDialog {

    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtQuantity;
    private JButton btnSave;
    private JButton btnCancel;

    private Product product;
    private boolean saved = false;

    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color ACCENT = new Color(13, 110, 253);
    private static final Color TEXT_MAIN = new Color(33, 37, 41);
    private static final Color TEXT_SUB = new Color(108, 117, 125);
    private static final Color BORDER_C = new Color(222, 226, 230);
    private static final Color INPUT_BG = new Color(255, 255, 255);
    private static final Color SUCCESS = new Color(25, 135, 84);
    public ProductForm(Frame owner, String title, Product product) {
        super(owner, title, true);
        this.product = (product != null) ? product : new Product();
        buildUI();
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C),
                new EmptyBorder(16, 24, 16, 24)
        ));
        JLabel lblTitle = new JLabel(getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_MAIN);
        header.add(lblTitle, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Form body
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(24, 32, 16, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1.0;

        txtName = createStyledField(product.getName() != null ? product.getName() : "");
        txtPrice = createStyledField(product.getPrice() > 0 ? String.valueOf(product.getPrice()) : "");
        txtQuantity = createStyledField(product.getQuantity() > 0 ? String.valueOf(product.getQuantity()) : "");

        addFormRow(body, gbc, 0, "Product Name", txtName);
        addFormRow(body, gbc, 2, "Price ($)", txtPrice);
        addFormRow(body, gbc, 4, "Quantity", txtQuantity);

        add(body, BorderLayout.CENTER);

        // Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footer.setBackground(BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_C));

        btnCancel = createButton("Cancel", BORDER_C, TEXT_SUB);
        btnSave = createButton("Save", SUCCESS, new Color(20, 40, 30));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(new SaveListener());

        footer.add(btnCancel);
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(400, 500));
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_SUB);
        gbc.gridy = row;
        panel.add(lbl, gbc);

        gbc.gridy = row + 1;
        panel.add(field, gbc);
    }

    private JTextField createStyledField(String text) {
        JTextField field = new JTextField(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_MAIN);
        field.setCaretColor(ACCENT);
        field.setBackground(INPUT_BG);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_C, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(0, 42));
        return field;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 36));
        return btn;
    }

    // Inner ActionListener for Save
    private class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = txtName.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String qtyStr = txtQuantity.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                showError("Please fill in all fields.");
                return;
            }
            try {
                float price = Float.parseFloat(priceStr);
                int qty = Integer.parseInt(qtyStr);
                if (price < 0 || qty < 0) {
                    showError("Price and Quantity must be non-negative.");
                    return;
                }
                product.setName(name);
                product.setPrice(price);
                product.setQuantity(qty);
                saved = true;
                dispose();
            } catch (NumberFormatException ex) {
                showError("Price must be a number and Quantity must be an integer.");
            }
        }

        private void showError(String msg) {
            JOptionPane.showMessageDialog(ProductForm.this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Product getProduct() {
        return this.product;
    }
}
