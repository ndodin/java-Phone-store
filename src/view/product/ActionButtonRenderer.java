package view.product;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import session.UserSession;

public class ActionButtonRenderer extends JPanel
        implements TableCellRenderer {
    private static final Color ACCENT = new Color(13, 110, 253);
    private static final Color DANGER =  new Color(244, 67, 54);
    private String userRole =
            UserSession.getInstance()
                    .getUser()
                    .getRole();
    public ActionButtonRenderer() {
        setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        5,
                        5
                )
        );
        setOpaque(true);
        add(createButton("Edit", ACCENT));
        if ("admin".equalsIgnoreCase(userRole)) {
            add(createButton("Delete", DANGER));
        }
    }
    private JButton createButton(
            String text,
            Color bg
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
        btn.setPreferredSize(
                new Dimension(80, 28)
        );
        return btn;
    }
    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {
        setBackground(
                isSelected
                        ? table.getSelectionBackground()
                        : Color.WHITE
        );
        return this;
    }
}