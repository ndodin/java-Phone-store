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
    
    private static final Color ACCENT = new Color(13, 110, 253);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 13);
    
    private static final Color SELECTION_BG = new Color(207, 226, 255);
    
    public ProductPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));
        
        controller = new ProductController(this, new ProductService());
        buildHeader();
        buildTable();
        buildFooter();
        refreshTable(new ProductService().getAll());
    }
    
    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Product Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        
        txtSearch = new JTextField(25);
        txtSearch.setPreferredSize(new Dimension(250, 36));
        txtSearch.setToolTipText("Search products...");
        txtSearch.setActionCommand("SEARCH");
        txtSearch.addActionListener(controller);
        
        btnPanel.add(new JLabel("Search:"));
        btnPanel.add(txtSearch);
        btnPanel.add(createButton("Refresh", new Color(108, 117, 125), "REFRESH"));
        btnPanel.add(createButton("Add Product", ACCENT, "ADD"));
        
        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }
    
    private void buildTable() {
        String[] cols = {"ID", "Name", "Price ($)", "Quantity", "Actions"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override public Class<?> getColumnClass(int c) {
                return (c == 0 || c == 3) ? Integer.class : (c == 2 ? Float.class : String.class);
            }
        };
        
        tbProduct = new JTable(model);
        tbProduct.setFont(MAIN_FONT);
        tbProduct.setRowHeight(40);
        tbProduct.setSelectionBackground(new Color(207, 226, 255));
        tbProduct.setSelectionForeground(Color.BLACK);
        tbProduct.setShowVerticalLines(false);
        
        JTableHeader th = tbProduct.getTableHeader();
        th.setFont(BOLD_FONT);
        th.setPreferredSize(new Dimension(0, 35));
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        DefaultTableCellRenderer priceRenderer = new DefaultTableCellRenderer() {
            private final java.text.DecimalFormat fmt = new java.text.DecimalFormat("#,##0");
            @Override
            protected void setValue(Object value) {
                setText(value == null ? "" : fmt.format(value) + " VND");
            }
        };
        center.setHorizontalAlignment(SwingConstants.CENTER);
        int[] widths = {50, 250, 100, 100, 150};
        
        for (int i = 0; i < widths.length; i++) {
            tbProduct.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            if (i != 1 && i != 4) tbProduct.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        
        tbProduct.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);
        tbProduct.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        tbProduct.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor(controller));
        
        add(new JScrollPane(tbProduct), BorderLayout.CENTER);
    }
    
    private void buildFooter() {
        lblStatus = new JLabel("Total: 0 products");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        add(lblStatus, BorderLayout.SOUTH);
    }
    
    private JButton createButton(String text, Color bg, String cmd) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 32));
        btn.setFont(BOLD_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setActionCommand(cmd);
        btn.addActionListener(controller);
        return btn;
    }
    
    public void refreshTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), ""});
        }
        lblStatus.setText("Total: " + products.size() + " products");
    }
    
    public String getSearchText() { return txtSearch.getText(); }
    
    public JTable getTable() { return tbProduct; }
}
