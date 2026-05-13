package view.customer;

import model.Customer;
import service.CustomerService;
import session.UserSession; 
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd;
    private CustomerService customerService;
    private TableRowSorter<DefaultTableModel> sorter;
    private String userRole; 
    public CustomerPanel() {
        customerService = new CustomerService();
        userRole = UserSession.getInstance().getUser().getRole(); 
        initComponents();
        loadTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(350, 40));
        txtSearch.setBorder(BorderFactory.createTitledBorder("Search:"));
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText();
                if (text.trim().isEmpty()) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        btnAdd = createStyledBtn("Add Customer", new Color(76, 175, 80));

        btnAdd.addActionListener(e -> {
            CustomerForm form = new CustomerForm(null, this);
            form.setVisible(true);
        });

        topPanel.add(txtSearch, BorderLayout.WEST);
        topPanel.add(btnAdd, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"ID", "Name", "Phone", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 3; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(50);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(3).setCellRenderer(new ActionButtonsRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ActionButtonsEditor());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadTable() {
        tableModel.setRowCount(0);
        List<Customer> list = customerService.getAll();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), ""});
        }
    }

    private JButton createStyledBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false); 
        return btn;
    }

    // =========================================
    // RENDERER: Vẽ giao diện nút trong bảng
    // =========================================
    class ActionButtonsRenderer extends JPanel implements TableCellRenderer {
        public ActionButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.removeAll();
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            
            // Luôn hiện nút Edit
            add(createStyledBtn("Edit", new Color(33, 150, 243)));
            
            // CHỈ hiện nút Delete nếu là Admin
            if ("admin".equalsIgnoreCase(userRole)) {
                add(createStyledBtn("Delete", new Color(244, 67, 54)));
            }
            
            return this;
        }
    }

    // =========================================
    // EDITOR: Xử lý sự kiện click nút trong bảng
    // =========================================
    class ActionButtonsEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int currentRow;

        public ActionButtonsEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(true);
            
            JButton btnEdit = createStyledBtn("Edit", new Color(33, 150, 243));
            panel.add(btnEdit);

            // CHỈ thêm nút Delete vào xử lý nếu là Admin
            if ("admin".equalsIgnoreCase(userRole)) {
                JButton btnDelete = createStyledBtn("Delete", new Color(244, 67, 54));
                btnDelete.addActionListener(e -> {
                    fireEditingStopped();
                    int modelRow = table.convertRowIndexToModel(currentRow);
                    int id = (int) tableModel.getValueAt(modelRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(
                        null, 
                        "Delete customer ID " + id + "?", 
                        "Confirm Delete", 
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (customerService.delete(id)) {
                            loadTable();
                        }
                    }
                });
                panel.add(btnDelete);
            }

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = table.convertRowIndexToModel(currentRow);
                int id = (int) tableModel.getValueAt(modelRow, 0); 
                String name = (String) tableModel.getValueAt(modelRow, 1);
                String phone = (String) tableModel.getValueAt(modelRow, 2);
                Customer c = new Customer(); 
                c.setId(id);      
                c.setName(name);
                c.setPhone(phone);
                new CustomerForm(c, CustomerPanel.this).setVisible(true);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
        @Override
        public Object getCellEditorValue() { return ""; }
    }
}