package view.product;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class ActionButtonRenderer implements TableCellRenderer {
    
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    
    private static final Color EDIT_BG = new Color(49, 130, 206);
    private static final Color DELETE_BG = new Color(229, 62, 62);
    private static final Color FG = Color.WHITE;
    private static final Color ROW_ODD = new Color(22, 25, 37);
    private static final Color ROW_EVEN = new Color(26, 32, 46);
    
    public ActionButtonRenderer() {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnEdit = makeButton("Edit", EDIT_BG);
        btnDelete = makeButton("Delete", DELETE_BG);
        panel.add(btnEdit);
        panel.add(btnDelete);
    }
    
    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
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
        btn.setPreferredSize(new Dimension(80, 28));
        return btn;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        panel.setBackground(isSelected ? new Color(207, 226, 255) : Color.WHITE); // bỏ màu tối
        return panel;
    }
}
