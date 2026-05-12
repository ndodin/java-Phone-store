package service;

import dao.InvoiceDAO;
import model.Invoice;
import model.InvoiceDetail;

import java.util.List;

public class InvoiceService {

    private InvoiceDAO invoiceDAO;

    public InvoiceService() {

        invoiceDAO = new InvoiceDAO();
    }

    // =========================================
    // CALCULATE TOTAL
    // =========================================

    public double calculateTotal(
            List<InvoiceDetail> details
    ) {

        double total = 0;

        for (InvoiceDetail d : details) {

            total += d.getPrice() * d.getQuantity();
        }

        return total;
    }

    // =========================================
    // CHECKOUT
    // =========================================

    public boolean checkout(
            Invoice invoice,
            List<InvoiceDetail> details
    ) {

        // VALIDATE

        if (invoice == null) {

            return false;
        }

        if (details == null || details.isEmpty()) {

            return false;
        }

        // CALCULATE TOTAL

        double total =
                calculateTotal(details);

        invoice.setTotal(total);

        invoice.setStatus("COMPLETED");

        return invoiceDAO.checkout(invoice, details);
    }

    // =========================================
    // GET ALL
    // =========================================

    public List<Invoice> getAll() {

        return invoiceDAO.getAll();
    }

    // =========================================
    // CANCEL
    // =========================================

    public boolean cancelInvoice(int invoiceId) {

        if (invoiceId <= 0) {

            return false;
        }

        return invoiceDAO.cancelInvoice(invoiceId);
    }

    // =========================================
    // DETAILS
    // =========================================

    public List<InvoiceDetail> getDetailsByInvoiceId(
            int invoiceId
    ) {

        return invoiceDAO.getDetailsByInvoiceId(invoiceId);
    }
}