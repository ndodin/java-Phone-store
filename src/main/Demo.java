package main;

import java.awt.*;
import javax.swing.*;
import view.product.ProductPanel;

public class Demo extends JFrame {

    private static final Color BG = new Color(15, 17, 26);

    public Demo(String title) {
        super(title);
        buildUI();
    }

    private void buildUI() {
        ProductPanel productPane = new ProductPanel();

        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(productPane, BorderLayout.CENTER);

        setTitle("Product Management System");
        setSize(820, 520);
        setMinimumSize(new Dimension(700, 420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setVisible(true);
    }
}
