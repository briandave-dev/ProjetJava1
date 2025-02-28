package controller;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import model.Document;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


class ButtonsRenderer extends JPanel implements TableCellRenderer {
    private JButton empruntButton, rendreButton, modifierButton, supprimerButton;
    StyleBiblio myStyle = new StyleBiblio();
    List<Document> collection;
    public ButtonsRenderer(List<Document> col) {
        collection = new ArrayList<>();
        collection = col;
        setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
        
        // Création des boutons avec des dimensions fixes et padding réduit
        Dimension buttonSize = new Dimension(80, 25);
        
        empruntButton = createActionButton("Emprunter", myStyle.getPrimaryColor(), buttonSize);
        rendreButton = createActionButton("Rendre", myStyle.getAccentColor(), buttonSize);
        modifierButton = createActionButton("Modifier", new Color(50, 205, 50), buttonSize);
        supprimerButton = createActionButton("Supprimer", new Color(220, 20, 60), buttonSize);
        
        add(empruntButton);
        add(rendreButton);
        add(modifierButton);
        add(supprimerButton);

        setBackground(Color.WHITE);
    }
    
    private JButton createActionButton(String text, Color bgColor, Dimension size) {
        JButton button = new JButton(text);
        button.setFont(myStyle.getSmallFont());
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        return button;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
                                                 boolean isSelected, boolean hasFocus, 
                                                 int row, int column) {
        if (value != null) {
            int index = (Integer) value;
            Document doc = collection.get(index);
            empruntButton.setEnabled(doc.getDisponible());
            rendreButton.setEnabled(!doc.getDisponible());
        }
        return this;
    }
}

