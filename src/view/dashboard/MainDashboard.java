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
    private Sidebar sidebar;
    private Header header;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainDashboard() {
        initComponents();
        initFrame();
        checkRole();
    }
    
    
    private void initComponents() {
        setLayout(new BorderLayout());
        sidebar = new Sidebar();
        add(sidebar, BorderLayout.WEST);
        header = new Header();
        add(header, BorderLayout.NORTH);
        
        
        cardLayout = new CardLayout();
//        contentPanel = new JPanel(cardLayout);
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        
//        contentPanel.add(new ProductPanel(), "PRODUCT");
//        contentPanel.add(new CustomerPanel(), "CUSTOMER");
//        contentPanel.add(new InvoicePanel(), "INVOICE");
//        contentPanel.add(new InvoiceHistoryPanel(), "HISTORY");
        
        sidebar.btnDashboard.addActionListener(e -> showPanel("DASHBOARD"));
        sidebar.btnProduct.addActionListener(e -> showPanel("PRODUCT"));
        sidebar.btnCustomer.addActionListener(e -> showPanel("CUSTOMER"));
        sidebar.btnInvoice.addActionListener(e -> showPanel("INVOICE"));
        sidebar.btnHistory.addActionListener(e -> showPanel("HISTORY"));

//        sidebar.btnProduct.addActionListener(e -> {
//            cardLayout.show(contentPanel, "PRODUCT");
//        });
//        sidebar.btnCustomer.addActionListener(e -> {
//            cardLayout.show(contentPanel, "CUSTOMER");
//        });
//
//        sidebar.btnInvoice.addActionListener(e -> {
//            cardLayout.show(contentPanel, "INVOICE");
//        });
//
//        sidebar.btnHistory.addActionListener(e -> {
//            cardLayout.show(contentPanel, "HISTORY");
//        });

        header.btnLogout.addActionListener(e -> logout());
        showPanel("DASHBOARD");
    }
    
    
    private void showPanel(String name) {
        contentPanel.removeAll();
        JPanel selectedPanel = null;
        switch (name) {
            case "DASHBOARD":
                selectedPanel = new DashBoard();
                break;
            case "PRODUCT":
                selectedPanel = new ProductPanel();
                break;
            case "CUSTOMER":
                selectedPanel = new CustomerPanel();
                break;
            case "INVOICE":
                selectedPanel = new InvoicePanel();
                break;
            case "HISTORY":
                selectedPanel = new InvoiceHistoryPanel();
                break;
        }
        
        if (selectedPanel != null) {
            contentPanel.add(selectedPanel, BorderLayout.CENTER);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    
    
    
    private void checkRole() {
    	String role = UserSession.getInstance().getUser().getRole();
        if (role.equalsIgnoreCase("staff")) {
            sidebar.btnProduct.setEnabled(false);
        }
    }

    private void logout() {
        int confirm =
                JOptionPane.showConfirmDialog( this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }
    

    private void initFrame() {
        setTitle("Phone Store Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}