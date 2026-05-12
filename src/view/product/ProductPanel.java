package view.product;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import model.Product;
import service.ProductService;

public class ProductPanel extends JPanel {

    private JTable tbProduct;
    private DefaultTableModel model;
    private JButton btnAdd;
    private JButton btnRefresh;
    private JTextField txtSearch;
    private JLabel lblStatus;
    private ProductController controller;

    // Design tokens - Light theme ☀️
    private static final Color BG = new Color(245, 247, 250); // Xám cực nhạt cho background
    private static final Color CARD_BG = new Color(255, 255, 255); // Trắng tinh cho bảng
    private static final Color ACCENT = new Color(13, 110, 253); // Xanh dương đậm để nổi chữ
    private static final Color TEXT_MAIN = new Color(33, 37, 41); // Chữ xám đen (chữ đen thui sẽ bị chói)
    private static final Color TEXT_SUB = new Color(108, 117, 125); // Chữ phụ xám mờ
    private static final Color BORDER_C = new Color(222, 226, 230); // Viền xám nhạt
    private static final Color INPUT_BG = new Color(255, 255, 255); // Ô search trắng
    private static final Color ROW_ODD = new Color(255, 255, 255); // Trắng
    private static final Color ROW_EVEN = new Color(248, 249, 250); // Xám nhẹ phân biệt dòng
    private static final Color HEADER_BG = new Color(233, 236, 239); // Header xám nhẹ
    private static final Color SUCCESS = new Color(25, 135, 84); // Xanh lá đậm hơn
    private static final Color DANGER = new Color(220, 53, 69); // Đỏ đậm hơn
    private static final Color ADD_BTN = new Color(13, 202, 240); // Cyan

    private static final Color SELECTION_BG = new Color(207, 226, 255);

    public ProductPanel() {
        ProductService service = new ProductService();
        setBackground(BG);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        controller = new ProductController(this, service);
        buildHeader();
        buildTable();
        buildFooter();

        refreshTable(service.getAll());
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Title
        JLabel lblTitle = new JLabel("Product Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_MAIN);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(0, 0));
        searchPanel.setBackground(INPUT_BG);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_C, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setForeground(TEXT_MAIN);
        txtSearch.setCaretColor(ACCENT);
        txtSearch.setBackground(INPUT_BG);
        txtSearch.setOpaque(false);
        txtSearch.setBorder(BorderFactory.createEmptyBorder());
        txtSearch.setActionCommand("SEARCH");
        txtSearch.addActionListener(controller);

        JLabel lblSearch = new JLabel("");
        lblSearch.setForeground(TEXT_SUB);
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.setPreferredSize(new Dimension(220, 38));

        // Buttons
        btnAdd = createButton("Add Product", ADD_BTN, new Color(10, 50, 50), "ADD");
        btnRefresh = createButton("Refresh", new Color(66, 66, 99), TEXT_SUB, "REFRESH");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(BG);
        btnPanel.add(searchPanel);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnAdd);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    // ── Table ──────────────────────────────────────────────────────────────────
    private void buildTable() {
        String[] columns = {"ID", "Name", "Price ($)", "Quantity", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 4; // Only actions column is editable
            }

            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) {
                    return Integer.class;
                }
                if (col == 2) {
                    return Float.class;
                }
                if (col == 3) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tbProduct = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? ROW_ODD : ROW_EVEN);
                    c.setForeground(TEXT_MAIN);
                }
                return c;
            }
        };

        tbProduct.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbProduct.setForeground(TEXT_MAIN);
        tbProduct.setBackground(ROW_ODD);
        tbProduct.setRowHeight(44);
        tbProduct.setShowGrid(false);
        tbProduct.setIntercellSpacing(new Dimension(0, 0));
        tbProduct.setSelectionBackground(new Color(35, 45, 80));
        tbProduct.setSelectionForeground(TEXT_MAIN);
        tbProduct.setFillsViewportHeight(true);
        tbProduct.setAutoCreateRowSorter(true);

        // Header style
        JTableHeader th = tbProduct.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(HEADER_BG);
        th.setForeground(ACCENT);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0, 40));

        // Column widths
        int[] widths = {50, 220, 100, 90, 180};
        for (int i = 0; i < widths.length; i++) {
            tbProduct.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : new int[]{0, 2, 3}) {
            tbProduct.getColumnModel().getColumn(col).setCellRenderer(centerRenderer);
        }

        // Action column
        tbProduct.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        tbProduct.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor(controller));

        JScrollPane scrollPane = new JScrollPane(tbProduct);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));
        scrollPane.getViewport().setBackground(ROW_ODD);
        scrollPane.setBackground(BG);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD_BG);
        tableCard.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);
    }

    // ── Footer: status bar ─────────────────────────────────────────────────────
    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(10, 2, 0, 0));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(TEXT_SUB);
        footer.add(lblStatus);

        add(footer, BorderLayout.SOUTH);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    private JButton createButton(String text, Color bg, Color fg, String command) {
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setActionCommand(command);
        btn.addActionListener(controller);
        btn.setPreferredSize(new Dimension(130, 38));
        return btn;
    }

    public void refreshTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), ""});
        }
        lblStatus.setText("Total: " + products.size() + " products");
        lblStatus.setForeground(TEXT_SUB);
    }

    public void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setForeground(success ? SUCCESS : DANGER);
    }

    public int getSelectedModelRow() {
        int viewRow = tbProduct.getSelectedRow();
        if (viewRow == -1) {
            return -1;
        }
        return tbProduct.convertRowIndexToModel(viewRow);
    }

    public String getSearchText() {
        return txtSearch.getText();
    }

    public void clearSearch() {
        txtSearch.setText("");
    }

    public JTable getTable() {
        return tbProduct;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
