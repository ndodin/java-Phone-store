package view.product;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import session.UserSession;

public class ActionButtonEditor
        extends DefaultCellEditor
        implements ActionListener {

    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private ProductController controller;
    private static final Color ACCENT = new Color(13, 110, 253);
    private static final Color DANGER = new Color(244, 67, 54);
    private String userRole =
            UserSession.getInstance()
                    .getUser()
                    .getRole();

    public ActionButtonEditor(
            ProductController controller
    ) {

        super(new JCheckBox());
        this.controller = controller;
        setClickCountToStart(1);
        panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        5,
                        5
                )
        );
        panel.setOpaque(true);
        btnEdit =
                createButton(
                        "Edit",
                        ACCENT,
                        "EDIT"
                );
        panel.add(btnEdit);
        if ("admin".equalsIgnoreCase(userRole)) {
            btnDelete =
                    createButton(
                            "Delete",
                            DANGER,
                            "DELETE"
                    );

            panel.add(btnDelete);
        }
    }
    private JButton createButton(
            String text,
            Color bg,
            String command
    ) {
        JButton btn = new JButton(text);
        btn.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setUI(
                new javax.swing.plaf.basic.BasicButtonUI()
        );
        btn.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
        btn.setPreferredSize(
                new Dimension(80, 28)
        );
        btn.setActionCommand(command);
        btn.addActionListener(this);
        return btn;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        controller.actionPerformed(e);
    }
    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column
    ) {
        panel.setBackground(
                table.getSelectionBackground()
        );
        return panel;
    }
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}