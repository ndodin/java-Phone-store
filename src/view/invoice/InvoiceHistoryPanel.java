package view.invoice;

import model.Invoice;

import service.InvoiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class InvoiceHistoryPanel extends JPanel {

    private JTable table;

    private DefaultTableModel model;

    private JButton btnView;

    private JButton btnCancel;

    private InvoiceService invoiceService;

    public InvoiceHistoryPanel() {

        invoiceService =
                new InvoiceService();

        initComponents();

        loadData();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle =
                new JLabel("INVOICE HISTORY");

        lblTitle.setFont(
                new Font("Arial",
                        Font.BOLD,
                        28)
        );

        add(lblTitle, BorderLayout.NORTH);

        // TABLE

        String[] columns = {

                "ID",

                "Customer",

                "Date",

                "Total",

                "Status"
        };

        model =
                new DefaultTableModel(columns, 0);

        table =
                new JTable(model);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // BUTTONS

        JPanel bottomPanel = new JPanel();

        btnView =
                new JButton("VIEW DETAIL");

        btnCancel =
                new JButton("CANCEL INVOICE");

        bottomPanel.add(btnView);

        bottomPanel.add(btnCancel);

        add(bottomPanel, BorderLayout.SOUTH);

        // EVENTS

        btnView.addActionListener(
                e -> viewDetail()
        );

        btnCancel.addActionListener(
                e -> cancelInvoice()
        );
    }

    // =========================================
    // LOAD
    // =========================================

    private void loadData() {

        model.setRowCount(0);

        List<Invoice> list =
                invoiceService.getAll();

        for (Invoice i : list) {

            Object[] row = {

                    i.getId(),

                    i.getCustomerId(),

                    i.getDate(),

                    i.getTotal(),

                    i.getStatus()
            };

            model.addRow(row);
        }
    }

    // =========================================
    // VIEW DETAIL
    // =========================================

    private void viewDetail() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select invoice first"
            );

            return;
        }

        int id =
                Integer.parseInt(
                        model.getValueAt(row, 0)
                                .toString()
                );

        InvoiceDetailDialog dialog =
                new InvoiceDetailDialog(id);

        dialog.setVisible(true);
    }

    // =========================================
    // CANCEL
    // =========================================

    private void cancelInvoice() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select invoice first"
            );

            return;
        }

        int id =
                Integer.parseInt(
                        model.getValueAt(row, 0)
                                .toString()
                );

        boolean result =
                invoiceService.cancelInvoice(id);

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cancelled"
            );

            loadData();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Cancel failed"
            );
        }
    }
}