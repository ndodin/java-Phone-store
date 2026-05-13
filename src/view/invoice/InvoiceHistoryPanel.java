package view.invoice;

import model.Invoice;
import service.InvoiceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceHistoryPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private InvoiceService invoiceService;
    private List<Invoice> invoiceList;

    private JTextField txtSearchCustomer;
    private JFormattedTextField txtStartDate;
    private JFormattedTextField txtEndDate;

    public InvoiceHistoryPanel() {
        invoiceService = new InvoiceService();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Invoice History");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel mainCenterPanel = new JPanel(new BorderLayout(10, 10));

        // --- SEARCH PANEL ---
        JPanel searchPanel = new JPanel(new GridLayout(2, 3, 15, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                "Search", 0, 0, new Font("Arial", Font.BOLD, 14)));

        txtSearchCustomer = new JTextField();
        
        // Cố định form dd/mm/yyyy bằng MaskFormatter
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtStartDate = new JFormattedTextField(dateMask);
            txtEndDate = new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            txtStartDate = new JFormattedTextField();
            txtEndDate = new JFormattedTextField();
        }

        searchPanel.add(new JLabel("Customer:"));
        searchPanel.add(new JLabel("Start Date (dd/mm/yyyy):"));
        searchPanel.add(new JLabel("End Date (dd/mm/yyyy):"));
        searchPanel.add(txtSearchCustomer);
        searchPanel.add(txtStartDate);
        searchPanel.add(txtEndDate);

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.add(searchPanel, BorderLayout.CENTER);
        searchWrapper.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainCenterPanel.add(searchWrapper, BorderLayout.NORTH);

        // --- TABLE ---
        String[] columns = {"ID", "Customer", "Date", "Total", "Status", "Created By", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        
        // Khởi tạo bộ lọc (Sorter)
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.getColumn("Actions").setCellRenderer(new ActionButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);
        mainCenterPanel.add(scroll, BorderLayout.CENTER);
        add(mainCenterPanel, BorderLayout.CENTER);

        // --- EVENTS (Lọc thời gian thực) ---
        DocumentListener filterListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        };

        txtSearchCustomer.getDocument().addDocumentListener(filterListener);
        txtStartDate.getDocument().addDocumentListener(filterListener);
        txtEndDate.getDocument().addDocumentListener(filterListener);
    }

    private void applyFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // 1. Lọc theo tên khách hàng (Cột index 1)
        String name = txtSearchCustomer.getText().trim();
        if (!name.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + name, 1));
        }

        // 2. Lọc theo khoảng ngày (Cột index 2)
        String startStr = txtStartDate.getText().trim();
        String endStr = txtEndDate.getText().trim();

        filters.add(new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<? extends Object, ? extends Object> entry) {
                String tableDateStr = entry.getStringValue(2); // Dạng yyyy-MM-dd ...
                SimpleDateFormat tableFmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat inputFmt = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date rowDate = tableFmt.parse(tableDateStr);
                    
                    // Kiểm tra ngày bắt đầu
                    if (!startStr.contains("_")) {
                        Date startDate = inputFmt.parse(startStr);
                        if (rowDate.before(startDate)) return false;
                    }
                    
                    // Kiểm tra ngày kết thúc
                    if (!endStr.contains("_")) {
                        Date endDate = inputFmt.parse(endStr);
                        if (rowDate.after(endDate)) return false;
                    }
                } catch (Exception e) {
                    // Nếu ngày trong bảng không hợp lệ hoặc trống, vẫn giữ lại dòng đó
                    return true;
                }
                return true;
            }
        });

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void loadData() {
        model.setRowCount(0);
        invoiceList = invoiceService.getAll();
        for (Invoice i : invoiceList) {
            Object[] row = {
                    "#" + i.getId(),
                    i.getCustomerName(),
                    i.getDate(), 
                    "$" + i.getTotal(),
                    i.getStatus(),
                    i.getUsername(),
                    ""
            };
            model.addRow(row);
        }
    }

    public void viewInvoice(int viewRow) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        Invoice invoice = invoiceList.get(modelRow);
        InvoiceDetailDialog dialog = new InvoiceDetailDialog(invoice);
        dialog.setVisible(true);
    }

    public void cancelInvoice(int viewRow) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        Invoice invoice = invoiceList.get(modelRow);
        if (invoice.getStatus().equalsIgnoreCase("CANCELLED")) {
            JOptionPane.showMessageDialog(this, "Already cancelled");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel invoice?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (invoiceService.cancelInvoice(invoice)) {
                JOptionPane.showMessageDialog(this, "Cancelled");
                loadData();
            }
        }
    }

    // --- INNER CLASSES ---
    class ActionPanel extends JPanel {
        public JButton btnView = new JButton("View");
        public JButton btnCancel = new JButton("Cancel");

        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            btnView.setBackground(new Color(33, 150, 243));
            btnView.setForeground(Color.WHITE);
            btnView.setOpaque(true);             
            btnView.setBorderPainted(false);
            
            btnCancel.setBackground(new Color(244, 67, 54));
            btnCancel.setForeground(Color.WHITE);
            btnCancel.setOpaque(true);             
            btnCancel.setBorderPainted(false);
            
            add(btnView);
            add(btnCancel);
        }
    }

    class ActionButtonRenderer extends ActionPanel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private ActionPanel panel;
        private int currentRow;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new ActionPanel();
            panel.btnView.addActionListener(e -> {
                fireEditingStopped();
                viewInvoice(currentRow);
            });
            panel.btnCancel.addActionListener(e -> {
                fireEditingStopped();
                cancelInvoice(currentRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return ""; }
    }
}