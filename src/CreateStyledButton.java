import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
public class CreateStyledButton {
     JButton createStyledButton(String text, Icon icon) {
        StyleBiblio myStyle = new StyleBiblio();
        JButton button = new JButton(text);
        button.setFont(myStyle.getNormalFont());
        button.setBackground(myStyle.getPrimaryColor());
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(myStyle.getPrimaryColor().darker());
            }
            
            
            public void mouseExited(MouseEvent e) {
                button.setBackground(myStyle.getPrimaryColor());
            }
        });
        
        return button;
    }
    
    
}
