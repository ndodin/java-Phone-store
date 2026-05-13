package view.product;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import view.product.*;

public class ActionButtonEditor extends DefaultCellEditor implements ActionListener {

    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private ProductController controller;

    private static final Color EDIT_BG = new Color(49, 130, 206);
    private static final Color DELETE_BG = new Color(229, 62, 62);
    private static final Color FG = Color.WHITE;

    public ActionButtonEditor(ProductController controller) {
        super(new JCheckBox());
        this.controller = controller;
        setClickCountToStart(1);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        panel.setBackground(new Color(22, 25, 37));

        btnEdit = makeButton("✏ Edit", EDIT_BG, "EDIT");
        btnDelete = makeButton("✕ Delete", DELETE_BG, "DELETE");

        panel.add(btnEdit);
        panel.add(btnDelete);
    }

    private JButton makeButton(String text, Color bg, String command) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(FG);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 28));
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
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        panel.setBackground(new Color(207, 226, 255)); // bỏ màu tối
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
