package view.invoice;

import model.Invoice;
import model.InvoiceDetail;
import service.InvoiceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class InvoiceDetailDialog extends JDialog {

    private Invoice invoice;
    private InvoiceService invoiceService;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblTotal;

    // ================= COLORS =================
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;

    private static final Color BORDER_C = new Color(222, 226, 230);

    private static final Color ACCENT = new Color(13, 110, 253);

    private static final Color TEXT_MAIN = new Color(33, 37, 41);

    private static final Color TEXT_SUB = new Color(108, 117, 125);

    // ================= FONT =================
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 13);

    public InvoiceDetailDialog(Invoice invoice) {

        this.invoice = invoice;

        invoiceService = new InvoiceService();

        setTitle("Invoice Detail");

        setSize(900, 650);

        setLocationRelativeTo(null);

        setModal(true);

        initComponents();

        loadData();
    }

    private void initComponents() {

        JPanel root = new JPanel(
                new BorderLayout(15, 15)
        );

        root.setBackground(BG);

        root.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        setContentPane(root);

        // ================= HEADER =================
        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setOpaque(false);

        JPanel leftHeader = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        0
                )
        );

        leftHeader.setOpaque(false);

        JButton btnBack =
                createButton(
                        "Back",
                        new Color(108, 117, 125)
                );

        btnBack.addActionListener(e -> dispose());

        JLabel lblTitle = new JLabel("Invoice Detail");

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lblTitle.setForeground(TEXT_MAIN);

        leftHeader.add(btnBack);

        leftHeader.add(lblTitle);

        header.add(leftHeader, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel centerPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        centerPanel.setOpaque(false);

        // ================= INFO PANEL =================
        JPanel infoCard = new JPanel(
                new GridLayout(3, 2, 20, 18)
        );

        infoCard.setBackground(CARD_BG);

        infoCard.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_C, 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        infoCard.add(
                createInfoItem(
                        "Invoice ID",
                        "#" + invoice.getId()
                )
        );

        infoCard.add(
                createInfoItem(
                        "Date",
                        String.valueOf(invoice.getDate())
                )
        );

        infoCard.add(
                createInfoItem(
                        "Customer",
                        invoice.getCustomerName()
                )
        );

        infoCard.add(
                createInfoItem(
                        "Created By",
                        invoice.getUsername()
                )
        );

        infoCard.add(
                createInfoItem(
                        "Status",
                        invoice.getStatus()
                )
        );

        centerPanel.add(infoCard, BorderLayout.NORTH);

        // ================= TABLE CARD =================
        JPanel tableCard = new JPanel(
                new BorderLayout(10, 10)
        );

        tableCard.setBackground(CARD_BG);

        tableCard.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_C, 1, true),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel lblItems =
                new JLabel("Invoice Items");

        lblItems.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblItems.setForeground(TEXT_MAIN);

        tableCard.add(lblItems, BorderLayout.NORTH);

        // ================= TABLE =================
        String[] columns = {
                "Product",
                "Quantity",
                "Price",
                "Subtotal"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {

                return false;
            }
        };

        table = new JTable(model);

        table.setFont(MAIN_FONT);

        table.setRowHeight(40);

        table.setSelectionBackground(
                new Color(207, 226, 255)
        );

        table.setSelectionForeground(Color.BLACK);

        table.setShowVerticalLines(false);

        table.setGridColor(
                new Color(235, 235, 235)
        );

        JTableHeader th = table.getTableHeader();

        th.setFont(BOLD_FONT);

        th.setPreferredSize(
                new Dimension(0, 38)
        );

        // ================= CENTER ALIGN =================
        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // ================= MONEY RENDERER =================
        DefaultTableCellRenderer moneyRenderer =
                new DefaultTableCellRenderer() {

                    private final DecimalFormat fmt =
                            new DecimalFormat("#,##0");

                    @Override
                    protected void setValue(Object value) {

                        setHorizontalAlignment(
                                SwingConstants.CENTER
                        );

                        if (value instanceof BigDecimal) {

                            BigDecimal money =
                                    (BigDecimal) value;

                            setText(
                                    fmt.format(money)
                                            + " $"
                            );

                        } else {

                            setText("");
                        }
                    }
                };

        int[] widths = {
                320,
                120,
                150,
                150
        };

        for (int i = 0; i < widths.length; i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);

            if (i != 0) {

                table.getColumnModel()
                        .getColumn(i)
                        .setCellRenderer(center);
            }
        }

        table.getColumnModel()
                .getColumn(2)
                .setCellRenderer(moneyRenderer);

        table.getColumnModel()
                .getColumn(3)
                .setCellRenderer(moneyRenderer);

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        tableCard.add(scroll, BorderLayout.CENTER);

        // ================= TOTAL PANEL =================
        JPanel totalPanel = new JPanel(
                new BorderLayout()
        );

        totalPanel.setBackground(
                new Color(248, 249, 250)
        );

        totalPanel.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        DecimalFormat fmt =
                new DecimalFormat("#,##0");

        lblTotal = new JLabel(
                "Total Amount: "
                        + fmt.format(invoice.getTotal())
                        + " $"
        );

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblTotal.setForeground(ACCENT);

        totalPanel.add(lblTotal, BorderLayout.WEST);

        tableCard.add(totalPanel, BorderLayout.SOUTH);

        centerPanel.add(tableCard, BorderLayout.CENTER);

        root.add(centerPanel, BorderLayout.CENTER);
    }

    // ================= LOAD DATA =================
    private void loadData() {

        model.setRowCount(0);

        List<InvoiceDetail> list =
                invoiceService.getDetails(
                        invoice.getId()
                );

        for (InvoiceDetail d : list) {

            Object[] row = {

                    d.getProductName(),

                    d.getQuantity(),

                    d.getPrice(),

                    d.getSubtotal()
            };

            model.addRow(row);
        }
    }

    // ================= INFO ITEM =================
    private JPanel createInfoItem(
            String label,
            String value
    ) {

        JPanel panel = new JPanel(
                new BorderLayout(0, 5)
        );

        panel.setOpaque(false);

        JLabel lblName = new JLabel(label);

        lblName.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblName.setForeground(TEXT_SUB);

        JLabel lblValue = new JLabel(
                value != null ? value : "N/A"
        );

        lblValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblValue.setForeground(TEXT_MAIN);

        panel.add(lblName, BorderLayout.NORTH);

        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    // ================= BUTTON =================
    private JButton createButton(
            String text,
            Color bg
    ) {

        JButton btn = new JButton(text);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setBackground(bg);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setPreferredSize(
                new Dimension(90, 35)
        );

        return btn;
    }
}