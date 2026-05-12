package view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Sidebar extends JPanel {
    public JButton btnDashboard, btnProduct, btnCustomer, btnInvoice, btnHistory;

    public Sidebar() {
        setPreferredSize(new Dimension(200, 0));
        setBackground(new Color(248, 249, 250)); // Màu nền xám trắng rất nhạt
        // Border bên phải để ngăn cách với nội dung chính
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        // TITLE
        JLabel lblTitle = new JLabel("Phone Store");
        lblTitle.setPreferredSize(new Dimension(200, 60));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        add(lblTitle);

        // BUTTONS
        btnDashboard = createMenuButton("Dashboard", false); // Dashboard đang được chọn
        btnProduct = createMenuButton("Product", true);
        btnCustomer = createMenuButton("Customer", false);
        btnInvoice = createMenuButton("Invoice", false);
        btnHistory = createMenuButton("History", false);

        add(btnDashboard);
        add(btnProduct);
        add(btnCustomer);
        add(btnInvoice);
        add(btnHistory);
    }

    private JButton createMenuButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(190, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));

        if (isActive) {
            btn.setBackground(new Color(230, 244, 255)); // Màu xanh nhạt khi active
            btn.setForeground(new Color(0, 102, 204));   // Chữ xanh
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(51, 51, 51));
        }

        // Tạo hiệu ứng hover cơ bản
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(!isActive) btn.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(!isActive) btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }
}