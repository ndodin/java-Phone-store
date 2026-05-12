package view.dashboard;

import view.components.Header;
import view.components.Sidebar;
import view.customer.CustomerPanel;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private Sidebar sidebar;
    private Header header;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainDashboard() {
        initComponents();
        setTitle("Phone Store Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Layout chính của Frame
        setLayout(new BorderLayout());

        // 1. Sidebar nằm bên trái (Cố định toàn bộ chiều cao)
        sidebar = new Sidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Vùng bên phải (Header + Nội dung)
        JPanel rightWrapper = new JPanel(new BorderLayout());
        
        header = new Header();
        rightWrapper.add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Thêm các Panel vào CardLayout
        contentPanel.add(new CustomerPanel(), "CUSTOMER");
        // contentPanel.add(new ProductPanel(), "PRODUCT"); // Bỏ comment khi có file

        rightWrapper.add(contentPanel, BorderLayout.CENTER);
        
        // Thêm vùng bên phải vào CENTER của Frame chính
        add(rightWrapper, BorderLayout.CENTER);

        // Navigation Event
        sidebar.btnCustomer.addActionListener(e -> cardLayout.show(contentPanel, "CUSTOMER"));
    }
}