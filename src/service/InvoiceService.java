package service;

import java.math.BigDecimal;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.ProductDAO;
import model.Invoice;
import model.InvoiceDetail;
import util.DBConnection;

import java.sql.Connection;
import java.util.List;

public class InvoiceService {
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO detailDAO;
    private ProductDAO productDAO;
    public InvoiceService() {
        invoiceDAO = new InvoiceDAO();
        detailDAO = new InvoiceDetailDAO();
        productDAO = new ProductDAO();
    }   
    public double getTodayRevenue() {
        return invoiceDAO.getTodayRevenue();
    }
    // CALCULATE TOTAL
    public BigDecimal calculateTotal(List<InvoiceDetail> cart) {
    BigDecimal total = BigDecimal.ZERO;
    for (InvoiceDetail d : cart) {
        total = total.add(d.getSubtotal());
    }
    return total;
}
    // CHECKOUT
    public boolean checkout(Invoice invoice, List<InvoiceDetail> cart) {
        Connection conn = null;
        try {
            if (cart == null || cart.isEmpty()) {
                return false;
            }
            BigDecimal total = calculateTotal(cart);
            invoice.setTotal(total);
            invoice.setStatus(
                    "COMPLETED"
            );
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            int invoiceId = invoiceDAO.insert(conn, invoice);
            if (invoiceId == -1) {
                conn.rollback();
                return false;
            }
            // INSERT DETAILS
            for (InvoiceDetail d : cart) {
                d.setInvoiceId(invoiceId);
                boolean detailResult = detailDAO.insert(conn, d);
                if (!detailResult) {
                    conn.rollback();
                    return false;
                }
                // UPDATE STOCK
                boolean stockResult = productDAO.updateStock(d.getProductId(), d.getQuantity());
                if (!stockResult) {
                    conn.rollback();
                    return false;
                }
            }
            // COMMIT
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }
    // GET ALL
    public List<Invoice> getAll() {
        return invoiceDAO.getAll();
    }
    public List<Invoice> getTop(int n) {
        return invoiceDAO.getRecent(n);
    }  
    // GET DETAILS
    public List<InvoiceDetail> getDetails(
            int invoiceId
    ) {
        return detailDAO.getByInvoiceId(
                invoiceId
        );
    }
    // CANCEL INVOICE
    public boolean cancelInvoice(
            Invoice invoice
    ) {
        try {
            // PREVENT RE-CANCEL
            if (invoice.getStatus()
                    .equalsIgnoreCase(
                            "CANCELLED"
                    )) {

                return false;
            }
            // RESTORE STOCK
            List<InvoiceDetail> details =
                    detailDAO.getByInvoiceId(
                            invoice.getId()
                    );
            for (InvoiceDetail d : details) {

                productDAO.restoreStock(
                        d.getProductId(),
                        d.getQuantity()
                );
            }
            // UPDATE STATUS
            return invoiceDAO.cancelInvoice(
                    invoice.getId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}