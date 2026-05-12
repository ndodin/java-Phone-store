package view.product;

import model.Product;
import service.ProductService;
import session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {

    private JTable table;

    private DefaultTableModel tableModel;

    private JTextField txtSearch;

    private JButton btnAdd;

    private JButton btnEdit;

    private JButton btnDelete;

    private ProductService productService;

    private TableRowSorter<DefaultTableModel> sorter;

    public ProductPanel() {

        productService = new ProductService();

        initComponents();

        loadTable();

        checkRole();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        // =====================================
        // TOP PANEL
        // =====================================

        JPanel topPanel =
                new JPanel(new BorderLayout());

        JLabel lblTitle =
                new JLabel("PRODUCT MANAGEMENT");

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        topPanel.add(lblTitle, BorderLayout.WEST);

        // SEARCH

        txtSearch = new JTextField(20);

        JPanel searchPanel = new JPanel();

        searchPanel.add(new JLabel("Search:"));

        searchPanel.add(txtSearch);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // =====================================
        // TABLE
        // =====================================

        String[] columns = {
                "ID",
                "Name",
                "Price",
                "Quantity"
        };

        tableModel =
                new DefaultTableModel(columns, 0);

        table = new JTable(tableModel);

        table.setRowHeight(30);

        sorter =
                new TableRowSorter<>(tableModel);

        table.setRowSorter(sorter);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // =====================================
        // BUTTON PANEL
        // =====================================

        JPanel buttonPanel = new JPanel();

        btnAdd = new JButton("ADD");

        btnEdit = new JButton("EDIT");

        btnDelete = new JButton("DELETE");

        buttonPanel.add(btnAdd);

        buttonPanel.add(btnEdit);

        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        // =====================================
        // EVENTS
        // =====================================

        btnAdd.addActionListener(e -> openAddForm());

        btnEdit.addActionListener(e -> openEditForm());

        btnDelete.addActionListener(e -> deleteProduct());

        txtSearch.addActionListener(e -> search());
    }

    // =========================================
    // LOAD TABLE
    // =========================================

    public void loadTable() {

        tableModel.setRowCount(0);

        List<Product> list =
                productService.getAll();

        for (Product p : list) {

            Object[] row = {

                    p.getId(),

                    p.getName(),

                    p.getPrice(),

                    p.getQuantity()
            };

            tableModel.addRow(row);
        }
    }

    // =========================================
    // SEARCH
    // =========================================

    private void search() {

        String keyword =
                txtSearch.getText().trim();

        sorter.setRowFilter(
                RowFilter.regexFilter(
                        "(?i)" + keyword
                )
        );
    }

    // =========================================
    // OPEN ADD
    // =========================================

    private void openAddForm() {

        ProductForm form =
                new ProductForm(null, this);

        form.setVisible(true);
    }

    // =========================================
    // OPEN EDIT
    // =========================================

    private void openEditForm() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select product first"
            );

            return;
        }

        int modelRow =
                table.convertRowIndexToModel(row);

        Product p = new Product();

        p.setId(
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                )
        );

        p.setName(
                tableModel.getValueAt(
                        modelRow,
                        1
                ).toString()
        );

        p.setPrice(
                Double.parseDouble(
                        tableModel.getValueAt(
                                modelRow,
                                2
                        ).toString()
                )
        );

        p.setQuantity(
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                3
                        ).toString()
                )
        );

        ProductForm form =
                new ProductForm(p, this);

        form.setVisible(true);
    }

    // =========================================
    // DELETE
    // =========================================

    private void deleteProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select product first"
            );

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete product?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm != JOptionPane.YES_OPTION) {

            return;
        }

        int modelRow =
                table.convertRowIndexToModel(row);

        int id =
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        boolean result =
                productService.delete(id);

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Delete success"
            );

            loadTable();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Delete failed"
            );
        }
    }

    // =========================================
    // ROLE
    // =========================================

    private void checkRole() {

        String role =
                UserSession.currentUser.getRole();

        if (role.equalsIgnoreCase("staff")) {

            btnDelete.setVisible(false);
        }
    }
}