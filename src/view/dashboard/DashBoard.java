package view.dashboard;

import model.Invoice;
import service.CustomerService;
import service.InvoiceService;
import service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashBoard extends JPanel {
  public DashBoard() {
    initComponents();
  }
  
  private void initComponents() {
    setLayout(new BorderLayout(0, 20)); // Khoảng cách giữa phần trên và phần dưới
    setBackground(new Color(245, 245, 245));
    setBorder(new EmptyBorder(20, 20, 20, 20));
    
    // --- PHẦN TOP: Gồm Tiêu đề và các Thẻ Thống kê ---
    JPanel topWrapper = new JPanel(new BorderLayout(0, 15));
    topWrapper.setOpaque(false);
    
    JLabel lblTitle = new JLabel("Dashboard Overview");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
    topWrapper.add(lblTitle, BorderLayout.NORTH);
    
    JPanel cardContainer = new JPanel(new GridLayout(1, 3, 20, 0));
    cardContainer.setOpaque(false);
    cardContainer.add(createStatCard("Total Products", String.valueOf(new ProductService().getTotalProduct()), new Color(33, 150, 243)));
    cardContainer.add(createStatCard("Total Customers", String.valueOf(new CustomerService().getTotalCustomers()), new Color(76, 175, 80)));
    double revenue = new InvoiceService().getTodayRevenue();
    cardContainer.add(createStatCard("Today Revenue", String.format("%,.0f VNĐ", revenue), new Color(255, 152, 0)));
    
    topWrapper.add(cardContainer, BorderLayout.CENTER);
    
    // Add topWrapper vào NORTH của Dashboard
    add(topWrapper, BorderLayout.NORTH);
    
    // --- PHẦN CENTER: Bảng hóa đơn gần đây (Đồng bộ format với History) ---
    JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
    bottomPanel.setBackground(Color.WHITE);
    bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
    ));
    
    JLabel lblTableTitle = new JLabel("Recent Invoices");
    lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
    bottomPanel.add(lblTableTitle, BorderLayout.NORTH);

    String[] columns = {"ID", "Customer", "Date", "Total", "Status", "Created By"};

    DefaultTableModel model = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    
    JTable table = new JTable(model);
    table.setRowHeight(35);
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    table.getTableHeader().setBackground(new Color(245, 245, 245));
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.setSelectionBackground(new Color(232, 242, 254));
    table.setShowGrid(false);
    table.setIntercellSpacing(new Dimension(0, 0));
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setBackground(Color.WHITE);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    bottomPanel.add(scrollPane, BorderLayout.CENTER);
    
    add(bottomPanel, BorderLayout.CENTER);

    loadRecentInvoices(model);
  }
  
  private JPanel createStatCard(String title, String value, Color valueColor) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
    ));
    
    JLabel lblTitle = new JLabel(title);
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
    lblTitle.setForeground(new Color(50, 50, 50));
    lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    JLabel lblValue = new JLabel(value);
    lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
    lblValue.setForeground(valueColor);
    lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    lblValue.setBorder(new EmptyBorder(10, 0, 0, 0));
    
    card.add(lblTitle);
    card.add(lblValue);
    
    return card;
  }
  
  private void loadRecentInvoices(DefaultTableModel model) {
    model.setRowCount(0);
    List<Invoice> list = new service.InvoiceService().getTop(10);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    for (Invoice i : list) {
      String formattedDate = (i.getDate() != null) ? sdf.format(i.getDate()) : "";
      String formattedTotal = String.format("%,.0f VNĐ", i.getTotal());
      
      Object[] row = {
              "HD_" + i.getId(),
              i.getCustomerName(),
              formattedDate,
              formattedTotal,
              i.getStatus(),
              i.getUsername()
      };
      model.addRow(row);
    }
  }
}
