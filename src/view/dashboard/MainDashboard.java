package view.dashboard;

import session.UserSession;

import view.components.Header;
import view.components.Sidebar;
import view.customer.CustomerPanel;
import view.invoice.InvoiceHistoryPanel;
import view.invoice.InvoicePanel;
import view.product.ProductPanel;

import view.login.LoginForm;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    // =========================================
    // COMPONENTS
    // =========================================

    private Sidebar sidebar;

    private Header header;

    private JPanel contentPanel;

    private CardLayout cardLayout;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public MainDashboard() {

        initComponents();

        initFrame();

        checkRole();
    }

    // =========================================
    // INIT COMPONENTS
    // =========================================

    private void initComponents() {

        setLayout(new BorderLayout());

        // =====================================
        // SIDEBAR
        // =====================================

        sidebar = new Sidebar();

        add(sidebar, BorderLayout.WEST);

        // =====================================
        // HEADER
        // =====================================

        header = new Header();

        add(header, BorderLayout.NORTH);

        // =====================================
        // CONTENT
        // =====================================

        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        // =====================================
        // ADD PANELS
        // =====================================

        contentPanel.add(
                new ProductPanel(),
                "PRODUCT"
        );

        contentPanel.add(
                new CustomerPanel(),
                "CUSTOMER"
        );

        contentPanel.add(
                new InvoicePanel(),
                "INVOICE"
        );

        contentPanel.add(
                new InvoiceHistoryPanel(),
                "HISTORY"
        );

        add(contentPanel, BorderLayout.CENTER);

        // =====================================
      // EVENTS
        // =====================================

        sidebar.btnProduct.addActionListener(e -> {

            cardLayout.show(
                    contentPanel,
                    "PRODUCT"
            );
        });

        sidebar.btnCustomer.addActionListener(e -> {

            cardLayout.show(
                    contentPanel,
                    "CUSTOMER"
            );
        });

        sidebar.btnInvoice.addActionListener(e -> {

            cardLayout.show(
                    contentPanel,
                    "INVOICE"
            );
        });

        sidebar.btnHistory.addActionListener(e -> {

            cardLayout.show(
                    contentPanel,
                    "HISTORY"
            );
        });

        header.btnLogout.addActionListener(e -> {

            logout();
       });
    }

    // =========================================
    // ROLE
    // =========================================

    private void checkRole() {

    	String role =
    	        UserSession.getInstance().getUser().getRole();

        // STAFF

        if (role.equalsIgnoreCase("staff")) {

            // staff không được quản lý product

            sidebar.btnProduct.setEnabled(false);
        }
    }

    // =========================================
    // LOGOUT
    // =========================================

    private void logout() {

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Logout?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm == JOptionPane.YES_OPTION) {

            dispose();

            new LoginForm().setVisible(true);
        }
    }

    // =========================================
    // FRAME
    // =========================================

    private void initFrame() {

        setTitle("Phone Store Management");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
    }
}