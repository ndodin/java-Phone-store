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
    setLayout(new BorderLayout(0, 20));
    setBackground(new Color(245, 245, 245));
    setBorder(new EmptyBorder(20, 20, 20, 20));
    
    // TOP
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
    cardContainer.add(createStatCard("Today Revenue", String.format("$"+"%.2f", revenue), new Color(255, 152, 0)));
    
    topWrapper.add(cardContainer, BorderLayout.CENTER);

    add(topWrapper, BorderLayout.NORTH);
    
 // center
 JPanel systemInfoPanel = new JPanel(new BorderLayout(0, 10));
 systemInfoPanel.setBackground(Color.WHITE);
 systemInfoPanel.setBorder(BorderFactory.createCompoundBorder(
         new LineBorder(new Color(220, 220, 220), 1),
         new EmptyBorder(15, 15, 15, 15)
 ));

 JLabel lblInfoTitle = new JLabel("System Information");
 lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
 systemInfoPanel.add(lblInfoTitle, BorderLayout.NORTH);

 // Panel chứa các dòng text thông tin
 JPanel infoContent = new JPanel();
 infoContent.setLayout(new BoxLayout(infoContent, BoxLayout.Y_AXIS));
 infoContent.setOpaque(false);

 int lowStockCount = new ProductService().getLowStockCount(10); // cảnh báo nếu số lượng tồn kho <=10

 infoContent.add(createLabelInfo("System Status: ", "Running", new Color(76, 175, 80)));
 infoContent.add(Box.createVerticalStrut(10));
 infoContent.add(Box.createVerticalStrut(10));
 infoContent.add(createLabelInfo("Low Stock Items: ", String.valueOf(lowStockCount), 
                lowStockCount > 0 ? Color.RED : Color.GRAY));

 systemInfoPanel.add(infoContent, BorderLayout.CENTER);

 
 add(systemInfoPanel, BorderLayout.CENTER);
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
  
  private JPanel createLabelInfo(String label, String value, Color valueColor) {
	    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    p.setOpaque(false);
	    JLabel lblHeader = new JLabel(label);
	    lblHeader.setFont(new Font("Segoe UI", Font.PLAIN, 15));
	    
	    JLabel lblValue = new JLabel(value);
	    lblValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    lblValue.setForeground(valueColor);
	    
	    p.add(lblHeader);
	    p.add(lblValue);
	    return p;
	}
  
  
}
