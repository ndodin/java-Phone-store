package view.invoice;

import model.InvoiceDetail;

import service.InvoiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class InvoiceDetailDialog extends JDialog {

    private JTable table;

    private DefaultTableModel model;

    private InvoiceService invoiceService;

    public InvoiceDetailDialog(int invoiceId) {

        invoiceService =
                new InvoiceService();

        initComponents();

        loadData(invoiceId);

        initDialog();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        String[] columns = {

                "Product ID",

                "Quantity",

                "Price"
        };

        model =
                new DefaultTableModel(columns, 0);

        table =
                new JTable(model);

        add(new JScrollPane(table),
                BorderLayout.CENTER);
    }

    private void loadData(int invoiceId) {

        List<InvoiceDetail> list =
                invoiceService
                        .getDetailsByInvoiceId(invoiceId);

        for (InvoiceDetail d : list) {

            Object[] row = {

                    d.getProductId(),

                    d.getQuantity(),

                    d.getPrice()
            };

            model.addRow(row);
        }
    }

    private void initDialog() {

        setTitle("Invoice Detail");

        setSize(500, 400);

        setLocationRelativeTo(null);

        setModal(true);
    }
}