package controller;
import javax.swing.*;

import model.Document;

import java.awt.*;
import java.awt.event.*;

class ButtonsEditor extends DefaultCellEditor {
    protected JPanel panel;
    private JButton empruntButton, rendreButton, modifierButton, supprimerButton;
    private int index = -1;
    private boolean isPushed;
    StyleBiblio myStyle = new StyleBiblio();
    Bibliotheque biblio;
    
    public ButtonsEditor(JCheckBox checkBox, Bibliotheque biblio) {
        super(checkBox);
        this.biblio = biblio;
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        panel.setBackground(Color.WHITE);
        
        // Création des boutons avec des dimensions fixes et padding réduit
        Dimension buttonSize = new Dimension(80, 25);
        
        empruntButton = createActionButton("Emprunter", myStyle.getPrimaryColor(), buttonSize);
        rendreButton = createActionButton("Rendre", myStyle.getAccentColor(), buttonSize);
        modifierButton = createActionButton("Modifier", new Color(50, 205, 50), buttonSize);
        supprimerButton = createActionButton("Supprimer", new Color(220, 20, 60), buttonSize);
        
        // Configuration des actions des boutons
        setupButtonActions();
        
        panel.add(empruntButton);
        panel.add(rendreButton);
        panel.add(modifierButton);
        panel.add(supprimerButton);
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

    private void setupButtonActions() {
        ActionListener actionListener = e -> {
            JButton button = (JButton)e.getSource();
            if (index >= 0 && index < biblio.collection.size()) {
                Document doc = biblio.collection.get(index);
                if (button == empruntButton && doc.getDisponible()) {
                    doc.emprunterDocument();
                    biblio.refreshTable();
                    biblio.showAnimatedSuccess("Document emprunté avec succès!");
                } else if (button == rendreButton && !doc.getDisponible()) {
                    doc.rendreDocument();
                    biblio.refreshTable();
                    biblio.showAnimatedSuccess("Document rendu avec succès!");
                } else if (button == modifierButton) {
                    biblio.showModifyDialog(index, doc);
                } else if (button == supprimerButton) {
                    handleDelete(index);
                }
            }
            fireEditingStopped();
        };

        empruntButton.addActionListener(actionListener);
        rendreButton.addActionListener(actionListener);
        modifierButton.addActionListener(actionListener);
        supprimerButton.addActionListener(actionListener);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row, int column) {
        if (value != null) {
            index = (Integer) value;
            if (index >= 0 && index < biblio.collection.size()) {
                Document doc = biblio.collection.get(index);
                empruntButton.setEnabled(doc.getDisponible());
                rendreButton.setEnabled(!doc.getDisponible());
            }
        }
        isPushed = true;
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return index;
    }
    
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
    
    private void handleDelete(int index) {
        if (index >= 0 && index < biblio.collection.size()) {
            Document doc = biblio.collection.get(index);
            int response = JOptionPane.showConfirmDialog(
                biblio.mainFrame,
                "Êtes-vous sûr de vouloir supprimer le document \"" + doc.getTitre() + "\" ?\n" +
                "Cette action est irréversible.",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (response == JOptionPane.YES_OPTION) {
                biblio.supprimerDocument(index);
                // Réinitialiser l'index après la suppression
                this.index = -1;
                biblio.showAnimatedSuccess("Document supprimé avec succès!");
                fireEditingStopped();
            }
        }
    }
}
