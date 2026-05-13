package view.login;

import model.User;
import service.AuthService;
import session.UserSession;
import view.dashboard.MainDashboard;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AuthService authService;

    public LoginForm() {
        authService = new AuthService();
        initComponents();
        initFrame();
    }

    private void initComponents() {
    // Màu sắc
    Color bgColor = new Color(242, 242, 242);
    Color borderColor = new Color(210, 210, 210);
    Color btnColor = new Color(33, 150, 243);
    Color labelColor = new Color(100, 100, 100);

    // PANEL CHÍNH (Nền xám nhạt toàn màn hình)
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(bgColor);

    // LOGIN CARD (Khung trắng ở giữa)
    JPanel loginPanel = new JPanel(new GridBagLayout());
    loginPanel.setBackground(Color.WHITE);
    loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
    ));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL; // Giãn rộng theo chiều ngang
    gbc.insets = new Insets(5, 0, 5, 0); // Khoảng cách giữa các dòng
    gbc.gridx = 0;

    // 1. Title "Phone Store Management"
    JLabel lblTitle = new JLabel("Phone Store Management", SwingConstants.CENTER);
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
    gbc.gridy = 0;
    loginPanel.add(lblTitle, gbc);

    // 2. Subtitle "Login to continue"
    JLabel lblSubTitle = new JLabel("Login to continue", SwingConstants.CENTER);
    lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
    lblSubTitle.setForeground(labelColor);
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 40, 0); // Khoảng trống lớn phía dưới subtitle
    loginPanel.add(lblSubTitle, gbc);

    // 3. Username Label
    JLabel lblUsername = new JLabel("Username:");
    lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 18));
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 5, 0);
    loginPanel.add(lblUsername, gbc);

    // 4. Username Field
    txtUsername = new JTextField();
    txtUsername.setPreferredSize(new Dimension(350, 40));
    txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    gbc.gridy = 3;
    loginPanel.add(txtUsername, gbc);

    // 5. Password Label
    JLabel lblPassword = new JLabel("Password:");
    lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 18));
    gbc.gridy = 4;
    gbc.insets = new Insets(20, 0, 5, 0); // Cách ô trên một khoảng
    loginPanel.add(lblPassword, gbc);

    // 6. Password Field
    txtPassword = new JPasswordField();
    txtPassword.setPreferredSize(new Dimension(350, 40));
    txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    gbc.gridy = 5;
    gbc.insets = new Insets(0, 0, 0, 0);
    loginPanel.add(txtPassword, gbc);

    // 7. Login Button
    btnLogin = new JButton("Login");
    btnLogin.setBackground(btnColor);
    btnLogin.setForeground(Color.WHITE);
    btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
    btnLogin.setFocusPainted(false);
    btnLogin.setBorderPainted(false);
    btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Để nút Login nhỏ lại và nằm bên trái
    JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    btnWrapper.setOpaque(false);
    btnLogin.setPreferredSize(new Dimension(120, 45));
    btnWrapper.add(btnLogin);
    
    gbc.gridy = 6;
    gbc.insets = new Insets(30, 0, 30, 0);
    loginPanel.add(btnWrapper, gbc);

    // 8. Demo Text
    JLabel lblDemo = new JLabel("Demo: Use \"admin\" or \"staff\" as username", SwingConstants.CENTER);
    lblDemo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    lblDemo.setForeground(labelColor);
    gbc.gridy = 7;
    gbc.insets = new Insets(0, 0, 0, 0);
    loginPanel.add(lblDemo, gbc);

    // Thêm vào mainPanel để căn giữa toàn bộ Card Login
    mainPanel.add(loginPanel);
    add(mainPanel);

    // EVENTS
    btnLogin.addActionListener(e -> login());
    txtPassword.addActionListener(e -> login());
}

    private void initFrame() {
        setTitle("Phone Store Management - Login");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        User user = authService.login(username, password);
        if (user != null) {
        	UserSession.getInstance().setUser(user);
            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    public static void main(String[] args) {
        // Thiết lập giao diện hệ thống cho mượt hơn
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}