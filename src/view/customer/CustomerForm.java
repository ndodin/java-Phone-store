package view.customer;

import model.Customer;
import service.CustomerService;
import javax.swing.*;
import java.awt.*;

public class CustomerForm extends JDialog {
    private JTextField txtName, txtPhone;
    private JButton btnSave, btnCancel;
    private Customer customer; 
    private CustomerPanel parentPanel;
    private CustomerService customerService;

    public CustomerForm(Customer customer, CustomerPanel parentPanel) {
        this.customer = customer;
        this.parentPanel = parentPanel;
        this.customerService = new CustomerService();
        
        setTitle(customer == null ? "Add Customer" : "Edit Customer");
        setSize(400, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setModal(true);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        mainPanel.add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        mainPanel.add(txtName);

        mainPanel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        mainPanel.add(txtPhone);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String saveText = (customer == null) ? "Add" : "Update";
        btnSave = new JButton(saveText);
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        btnSave.setOpaque(true);
        btnSave.setContentAreaFilled(true);
        btnSave.setBorderPainted(false);
        btnSave.setPreferredSize(new Dimension(100, 35));

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(158, 158, 158));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true);
        btnCancel.setContentAreaFilled(true);
        btnCancel.setBorderPainted(false);
        btnCancel.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        if (customer != null) {
            txtName.setText(customer.getName());
            txtPhone.setText(customer.getPhone());
        }

        btnSave.addActionListener(e -> saveAction());
        btnCancel.addActionListener(e -> dispose());

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveAction() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (customer == null) {
            Customer c = new Customer();
            c.setName(name);
            c.setPhone(phone);
            if (customerService.insert(c)) {
                parentPanel.loadTable();
                dispose();
            }
        } else {
            customer.setName(name);
            customer.setPhone(phone);
            
            if (customerService.update(customer)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parentPanel.loadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không thể cập nhật vào Database!");
            }
        }
    }
}