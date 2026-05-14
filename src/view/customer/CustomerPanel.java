package view.customer;

import model.Customer;
import service.CustomerService;
import session.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JLabel lblStatus;

    private CustomerService customerService;
    private TableRowSorter<DefaultTableModel> sorter;

    private String userRole;

    // STYLE
    private static final Color ACCENT = new Color(13, 110, 253);
    private static final Font MAIN_FONT =
            new Font("Segoe UI", Font.PLAIN, 13);

    private static final Font BOLD_FONT =
            new Font("Segoe UI", Font.BOLD, 13);

    private static final Color SELECTION_BG =
            new Color(207, 226, 255);

    public CustomerPanel() {

        customerService = new CustomerService();

        userRole =
                UserSession.getInstance()
                        .getUser()
                        .getRole();

        initComponents();
        loadTable();
    }

    private void initComponents() {

        setLayout(new BorderLayout(0, 10));

        setBorder(new EmptyBorder(20, 20, 20, 20));

        setBackground(new Color(245, 247, 250));
        
        //HEADER
        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Customer Management");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );

        JPanel btnPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        0
                ));

        btnPanel.setOpaque(false);

        txtSearch = new JTextField(25);

        txtSearch.setPreferredSize(
                new Dimension(250, 36)
        );

        txtSearch.setFont(MAIN_FONT);

        txtSearch.setToolTipText("Search customers...");

        txtSearch.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        filter();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filter();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filter();
                    }

                    private void filter() {

                        String text = txtSearch.getText();

                        if (text.trim().isEmpty()) {

                            sorter.setRowFilter(null);

                        } else {

                            sorter.setRowFilter(
                                    RowFilter.regexFilter(
                                            "(?i)" + text
                                    )
                            );
                        }
                    }
                }
        );

        btnAdd = createStyledBtn(
                "Add Customer",
                ACCENT
        );

        btnAdd.addActionListener(e -> {

            CustomerForm form =
                    new CustomerForm(null, this);

            form.setVisible(true);
        });

        btnPanel.add(new JLabel("Search:"));
        btnPanel.add(txtSearch);
        btnPanel.add(btnAdd);

        header.add(lblTitle, BorderLayout.WEST);

        header.add(btnPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        //TABLE
        String[] columns = {
                "ID",
                "Name",
                "Phone",
                "Actions"
        };

        tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return column == 3;
                    }
                };

        table = new JTable(tableModel);

        table.setFont(MAIN_FONT);

        table.setRowHeight(40);

        table.setSelectionBackground(SELECTION_BG);

        table.setSelectionForeground(Color.BLACK);

        table.setShowVerticalLines(false);

        sorter = new TableRowSorter<>(tableModel);

        table.setRowSorter(sorter);

        JTableHeader th = table.getTableHeader();

        th.setFont(BOLD_FONT);

        th.setPreferredSize(new Dimension(0, 35));

        int[] widths = {
                50,
                250,
                150,
                180
        };

        for (int i = 0; i < widths.length; i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        table.getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new ActionButtonsRenderer()
                );

        table.getColumnModel()
                .getColumn(3)
                .setCellEditor(
                        new ActionButtonsEditor()
                );

        add(new JScrollPane(table), BorderLayout.CENTER);

        //FOOTER
        lblStatus = new JLabel("Total: 0 customers");

        lblStatus.setFont(
                new Font("Segoe UI", Font.ITALIC, 12)
        );

        add(lblStatus, BorderLayout.SOUTH);
    }

    //LOAD TABLE
    public void loadTable() {

        tableModel.setRowCount(0);

        List<Customer> list =
                customerService.getAll();

        for (Customer c : list) {

            tableModel.addRow(
                    new Object[]{
                            c.getId(),
                            c.getName(),
                            c.getPhone(),
                            ""
                    }
            );
        }

        lblStatus.setText(
                "Total: " + list.size() + " customers"
        );
    }

    //BUTTON STYLE
    private JButton createStyledBtn(
            String text,
            Color bg
    ) {

        JButton btn = new JButton(text);

        if (text.equals("Edit") || text.equals("Delete")) {

            btn.setPreferredSize(
                    new Dimension(75, 30)
            );

        } else {

            btn.setPreferredSize(
                    new Dimension(130, 32)
            );
        }

        btn.setFont(BOLD_FONT);

        btn.setBackground(bg);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setContentAreaFilled(true);

        btn.setOpaque(true);

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        btn.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return btn;
    }

    //RENDERER
    class ActionButtonsRenderer
            extends JPanel
            implements TableCellRenderer {

        public ActionButtonsRenderer() {

            setLayout(
                    new FlowLayout(
                            FlowLayout.CENTER,
                            5,
                            5
                    )
            );

            setOpaque(true);
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

            removeAll();

            setBackground(
                    isSelected
                            ? table.getSelectionBackground()
                            : Color.WHITE
            );

            add(
                    createStyledBtn(
                            "Edit",
                            ACCENT 
                    )
            );

            if ("admin".equalsIgnoreCase(userRole)) {

                add(
                        createStyledBtn(
                                "Delete",
                                new Color(244, 67, 54)
                        )
                );
            }

            return this;
        }
    }

    //EDITOR
    class ActionButtonsEditor
            extends AbstractCellEditor
            implements TableCellEditor {

        private JPanel panel;

        private int currentRow;

        public ActionButtonsEditor() {

            panel = new JPanel(
                    new FlowLayout(
                            FlowLayout.CENTER,
                            5,
                            5
                    )
            );

            panel.setOpaque(true);

            JButton btnEdit =
                    createStyledBtn(
                            "Edit",
                            new Color(33, 150, 243)
                    );

            panel.add(btnEdit);

            if ("admin".equalsIgnoreCase(userRole)) {

                JButton btnDelete =
                        createStyledBtn(
                                "Delete",
                                new Color(244, 67, 54)
                        );

                btnDelete.addActionListener(e -> {

                    fireEditingStopped();

                    int modelRow =
                            table.convertRowIndexToModel(
                                    currentRow
                            );

                    int id =
                            (int) tableModel.getValueAt(
                                    modelRow,
                                    0
                            );

                    int confirm =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Delete customer ID " + id + "?",
                                    "Confirm Delete",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (confirm ==
                            JOptionPane.YES_OPTION) {

                        if (customerService.delete(id)) {

                            loadTable();
                        }
                    }
                });

                panel.add(btnDelete);
            }

            btnEdit.addActionListener(e -> {

                fireEditingStopped();

                int modelRow =
                        table.convertRowIndexToModel(
                                currentRow
                        );

                int id =
                        (int) tableModel.getValueAt(
                                modelRow,
                                0
                        );

                String name =
                        (String) tableModel.getValueAt(
                                modelRow,
                                1
                        );

                String phone =
                        (String) tableModel.getValueAt(
                                modelRow,
                                2
                        );

                Customer c = new Customer();

                c.setId(id);

                c.setName(name);

                c.setPhone(phone);

                new CustomerForm(
                        c,
                        CustomerPanel.this
                ).setVisible(true);
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

            this.currentRow = row;

            panel.setBackground(
                    table.getSelectionBackground()
            );

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}