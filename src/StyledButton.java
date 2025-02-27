import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.events.MouseEvent;

public class StyledButton {
    private JButton addButton, refreshButton;
    private Color primaryColor = new Color(65, 105, 225); // Bleu royal
    private Color secondaryColor = new Color(240, 248, 255); // Alice Blue
    private Color accentColor = new Color(255, 99, 71); // Tomato
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);
    private boolean sortAscending = true;
    private String currentSortColumn = "";

    


        public JButton createStyledButton(String text, Icon icon) {
        JButton button = new JButton(text);
        button.setFont(normalFont);
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(primaryColor.darker());
            }
            
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }
}