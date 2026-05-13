package view.customer;

import model.Customer;
import service.CustomerService;

import javax.swing.*;
import java.awt.*;

public class CustomerForm extends JDialog {

    private JTextField txtName;
    private JTextField txtPhone;
    private JButton btnSave;
    private Customer customer;
    private CustomerService customerService;
    private CustomerPanel parentPanel;

    public CustomerForm(Customer customer, CustomerPanel parentPanel) {
        this.customer = customer;
        this.parentPanel = parentPanel;
        customerService = new CustomerService();
        initComponents();
        fillData();
        initDialog();
    }

    private void initComponents() {
        setLayout(new GridLayout(8, 1, 10, 10));
        add(new JLabel("Customer Name"));
        txtName = new JTextField();
        add(txtName);
        add(new JLabel("Phone"));
        txtPhone = new JTextField();
        add(txtPhone);
        btnSave = new JButton("SAVE");
        add(btnSave);
        btnSave.addActionListener(e -> saveCustomer());
    }

    // =========================================
    // FILL DATA
    // =========================================

    private void fillData() {
        if (customer == null) {
            return;
        }
        txtName.setText(customer.getName());
        txtPhone.setText(customer.getPhone());
    }

    // =========================================
    // SAVE
    // =========================================

    private void saveCustomer() {
        try {
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            // ADD
            if (customer == null) {
            	Customer c = new Customer();
                c.setName(name);
                c.setPhone(phone);
                boolean result = customerService.insert(c);
                if (result) {
                    JOptionPane.showMessageDialog( this,"Add success"
                    );

                    parentPanel.loadTable();

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Add failed"
                    );
                }
            }

            // UPDATE

            else {

            	customer.setName(name);

            	customer.setPhone(phone);

                boolean result =
                        customerService.update(customer);

                if (result) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Update success"
                    );

                    parentPanel.loadTable();

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Update failed"
                    );
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid data"
            );
        }
    }

    private void initDialog() {

        setTitle(
        		customer == null
                        ? "Add Customer"
                        : "Edit Customer"
        );

        setSize(400, 350);

        setLocationRelativeTo(null);

        setModal(true);
    }
}