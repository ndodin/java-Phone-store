package view.components;

import session.UserSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Header extends JPanel {
    public JButton btnLogout;

    public Header() {
        setPreferredSize(new Dimension(0, 60));
        setBackground(Color.WHITE);
        // Đường kẻ mảnh phía dưới header
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        setLayout(new BorderLayout());

        // LEFT: Title của chức năng
        JLabel lblPageTitle = new JLabel("Phone Store Management System");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPageTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        add(lblPageTitle, BorderLayout.WEST);

        // RIGHT: User Info & Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rightPanel.setOpaque(false);

        String username = UserSession.getInstance().getUser().getUsername();
        String role =
                UserSession.getInstance().getUser().getRole();

        JLabel lblUser = new JLabel("<html>User: <b>" + username + "</b> (" + role + ")</html>");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(new Color(100, 100, 100));

        btnLogout = new JButton("Logout");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        rightPanel.add(lblUser);
        rightPanel.add(btnLogout);
        add(rightPanel, BorderLayout.EAST);
    }
}