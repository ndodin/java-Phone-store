package view.invoice;

import model.Invoice;
import model.InvoiceDetail;
import service.InvoiceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoiceDetailDialog extends JDialog {

    private Invoice invoice;
    private InvoiceService invoiceService;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    public InvoiceDetailDialog(Invoice invoice) {
        this.invoice = invoice;
        invoiceService = new InvoiceService();

        setTitle("Invoice Detail");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setModal(true);
       
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainContent.setBackground(Color.WHITE);
        setContentPane(mainContent);

        // =========================
        // TOP HEADER
        // =========================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topPanel.setBackground(Color.WHITE);
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());
        
        JLabel lblTitle = new JLabel("Invoice Detail");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        topPanel.add(btnBack);
        topPanel.add(lblTitle);
        mainContent.add(topPanel, BorderLayout.NORTH);

        // =========================
        // CENTER CONTENT
        // =========================
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);

        // 1. Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 20, 15));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Invoice Information", 0, 0, new Font("Arial", Font.BOLD, 16)));
        
        infoPanel.add(createLabeledValue("Invoice ID:", "#" + invoice.getId()));
        infoPanel.add(createLabeledValue("Date:", String.valueOf(invoice.getDate())));
        infoPanel.add(createLabeledValue("Customer:", invoice.getCustomerName()));
        infoPanel.add(createLabeledValue("Created By:", invoice.getUsername()));
        infoPanel.add(createLabeledValue("Status:", invoice.getStatus()));

        centerPanel.add(infoPanel, BorderLayout.NORTH);

        // 2. Items Panel
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Items", 0, 0, new Font("Arial", Font.BOLD, 16)));

        String[] columns = {"Product", "Quantity", "Price", "Subtotal"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(35);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        itemsPanel.add(scroll, BorderLayout.CENTER);
        
        // Total label
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(new Color(245, 245, 245));
        totalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        lblTotal = new JLabel("Total Amount: $" + invoice.getTotal());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(lblTotal);
        
        itemsPanel.add(totalPanel, BorderLayout.SOUTH);
        
        centerPanel.add(itemsPanel, BorderLayout.CENTER);
        mainContent.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createLabeledValue(String labelText, String valueText) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lblName = new JLabel(labelText);
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel lblVal = new JLabel(valueText != null ? valueText : "N/A");
        lblVal.setForeground(Color.DARK_GRAY);
        p.add(lblName, BorderLayout.NORTH);
        p.add(lblVal, BorderLayout.CENTER);
        return p;
    }

    private void loadData() {
        List<InvoiceDetail> list = invoiceService.getDetails(invoice.getId());
        for (InvoiceDetail d : list) {
            Object[] row = {
                    d.getProductName(),
                    d.getQuantity(),
                    "$" + d.getPrice(),
                    "$" + d.getSubtotal()
            };
            model.addRow(row);
        }
    }
}