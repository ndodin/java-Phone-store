package view.invoice;

import model.Invoice;
import service.InvoiceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
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

    // ================= COLORS =================
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_C = new Color(222, 226, 230);

    private static final Color ACCENT = new Color(13, 110, 253);
    private static final Color SUCCESS = new Color(25, 135, 84);
    private static final Color DANGER = new Color(244, 67, 54);

    private static final Color TEXT_MAIN = new Color(33, 37, 41);
    private static final Color TEXT_SUB = new Color(108, 117, 125);

    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 13);

    public InvoiceHistoryPanel() {
        invoiceService = new InvoiceService();

        setLayout(new BorderLayout(15, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        loadData();
    }

    private void initComponents() {

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Invoice History");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_MAIN);

        header.add(lblTitle, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        // ================= SEARCH PANEL =================
        JPanel searchCard = new JPanel(new GridLayout(2, 3, 15, 8));
        searchCard.setBackground(CARD_BG);
        searchCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_C, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblCustomer = createLabel("Customer");
        JLabel lblStart = createLabel("Start Date");
        JLabel lblEnd = createLabel("End Date");

        txtSearchCustomer = createTextField();

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');

            txtStartDate = new JFormattedTextField(dateMask);
            txtEndDate = new JFormattedTextField(dateMask);

            styleFormattedField(txtStartDate);
            styleFormattedField(txtEndDate);

        } catch (ParseException e) {
            txtStartDate = new JFormattedTextField();
            txtEndDate = new JFormattedTextField();
        }

        searchCard.add(lblCustomer);
        searchCard.add(lblStart);
        searchCard.add(lblEnd);

        searchCard.add(txtSearchCustomer);
        searchCard.add(txtStartDate);
        searchCard.add(txtEndDate);

        centerPanel.add(searchCard, BorderLayout.NORTH);

        // ================= TABLE =================
        String[] columns = {
                "ID",
                "Customer",
                "Date",
                "Total",
                "Status",
                "Created By",
                "Actions"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(model);

        table.setFont(MAIN_FONT);
        table.setRowHeight(42);
        table.setSelectionBackground(new Color(207, 226, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 235, 235));

        JTableHeader th = table.getTableHeader();
        th.setFont(BOLD_FONT);
        th.setPreferredSize(new Dimension(0, 38));
        th.setBackground(Color.WHITE);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // CENTER ALIGN
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        // MONEY FORMAT
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setHorizontalAlignment(SwingConstants.CENTER);

                if (value != null) {
                    setText(value.toString() + " $");
                } else {
                    setText("");
                }
            }
        };

        // STATUS FORMAT
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {

                setHorizontalAlignment(SwingConstants.CENTER);

                if (value == null) {
                    setText("");
                    return;
                }

                String status = value.toString();

                setText(status);

                if (status.equalsIgnoreCase("CANCELLED")) {
                    setForeground(DANGER);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    setForeground(SUCCESS);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
            }
        };

        int[] widths = {70, 180, 140, 120, 120, 140, 180};

        for (int i = 0; i < widths.length; i++) {

            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

            if (i != 1 && i != 6) {
                table.getColumnModel().getColumn(i).setCellRenderer(center);
            }
        }

        table.getColumnModel().getColumn(3).setCellRenderer(moneyRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);

        table.getColumn("Actions")
                .setCellRenderer(new ActionButtonRenderer());

        table.getColumn("Actions")
                .setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_C, 1, true),
                BorderFactory.createEmptyBorder()
        ));

        centerPanel.add(scroll, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ================= FILTER EVENTS =================
        DocumentListener filterListener = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }

            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }

            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        };

        txtSearchCustomer.getDocument().addDocumentListener(filterListener);
        txtStartDate.getDocument().addDocumentListener(filterListener);
        txtEndDate.getDocument().addDocumentListener(filterListener);
    }

    // ================= FILTER =================
    private void applyFilters() {

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        String name = txtSearchCustomer.getText().trim();

        if (!name.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + name, 1));
        }

        String startStr = txtStartDate.getText().trim();
        String endStr = txtEndDate.getText().trim();

        filters.add(new RowFilter<Object, Object>() {

            @Override
            public boolean include(Entry<? extends Object, ? extends Object> entry) {

                String tableDateStr = entry.getStringValue(2);

                SimpleDateFormat tableFmt =
                        new SimpleDateFormat("yyyy-MM-dd");

                SimpleDateFormat inputFmt =
                        new SimpleDateFormat("dd/MM/yyyy");

                try {

                    Date rowDate = tableFmt.parse(tableDateStr);

                    if (!startStr.contains("_")) {

                        Date startDate = inputFmt.parse(startStr);

                        if (rowDate.before(startDate)) {
                            return false;
                        }
                    }

                    if (!endStr.contains("_")) {

                        Date endDate = inputFmt.parse(endStr);

                        if (rowDate.after(endDate)) {
                            return false;
                        }
                    }

                } catch (Exception e) {
                    return true;
                }

                return true;
            }
        });

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    // ================= LOAD DATA =================
    private void loadData() {

        model.setRowCount(0);

        invoiceList = invoiceService.getAll();

        for (Invoice i : invoiceList) {

            Object[] row = {
                    "#" + i.getId(),
                    i.getCustomerName(),
                    i.getDate(),
                    i.getTotal(),
                    i.getStatus(),
                    i.getUsername(),
                    ""
            };

            model.addRow(row);
        }
    }

    // ================= VIEW =================
    public void viewInvoice(int viewRow) {

        int modelRow = table.convertRowIndexToModel(viewRow);

        Invoice invoice = invoiceList.get(modelRow);

        InvoiceDetailDialog dialog =
                new InvoiceDetailDialog(invoice);

        dialog.setVisible(true);
    }

    // ================= CANCEL =================
    public void cancelInvoice(int viewRow) {

        int modelRow = table.convertRowIndexToModel(viewRow);

        Invoice invoice = invoiceList.get(modelRow);

        if (invoice.getStatus()
                .equalsIgnoreCase("CANCELLED")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invoice already cancelled"
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Cancel this invoice?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (invoiceService.cancelInvoice(invoice)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cancelled successfully"
                );

                loadData();
            }
        }
    }

    // ================= UI HELPERS =================
    private JLabel createLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_SUB);

        return lbl;
    }

    private JTextField createTextField() {

        JTextField txt = new JTextField();

        txt.setFont(MAIN_FONT);

        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_C, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        txt.setPreferredSize(new Dimension(0, 40));

        return txt;
    }

    private void styleFormattedField(JFormattedTextField txt) {

        txt.setFont(MAIN_FONT);

        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_C, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        txt.setPreferredSize(new Dimension(0, 40));
    }

    // ================= ACTION PANEL =================
    class ActionPanel extends JPanel {

        public JButton btnView;
        public JButton btnCancel;

        public ActionPanel() {

            setLayout(new FlowLayout(
                    FlowLayout.CENTER,
                    5,
                    4
            ));

            setOpaque(true);

            btnView = createActionButton(
                    "View",
                    ACCENT
            );

            btnCancel = createActionButton(
                    "Cancel",
                    DANGER
            );

            add(btnView);
            add(btnCancel);
        }
    }

    // ================= RENDERER =================
    class ActionButtonRenderer
            extends ActionPanel
            implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            setBackground(
                    isSelected
                            ? table.getSelectionBackground()
                            : Color.WHITE
            );

            return this;
        }
    }

    // ================= EDITOR =================
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
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {

            currentRow = row;

            panel.setBackground(table.getSelectionBackground());

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // ================= BUTTON STYLE =================
    private JButton createActionButton(
            String text,
            Color bg
    ) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setPreferredSize(new Dimension(75, 30));

        return btn;
    }
}