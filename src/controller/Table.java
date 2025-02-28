package controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Document;

import java.awt.*;
import java.util.List;



public class Table  {
    private Bibliotheque bibliotheque;

    public Table(Bibliotheque bibliotheque){
        this.bibliotheque = bibliotheque;
    }

    public JPanel createTablePanel(List<Document> col) {
        StyleBiblio myStyle = new StyleBiblio();
        JPanel tablePanel = new JPanel(new BorderLayout(0, 50));
        tablePanel.setBackground(myStyle.getSecondaryColor());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(myStyle.getPrimaryColor()),
                "Liste des Documents",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                myStyle.getTitleFont(),
                myStyle.getPrimaryColor()
        ));

        // Colonnes du tableau
        String[] columns = {"Type", "Titre", "Auteur", "Année", "Détails", "Disponibilité", "Actions"};
        bibliotheque.tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        bibliotheque.documentsTable = new JTable(bibliotheque.tableModel);
        bibliotheque.documentsTable.setFont(myStyle.getNormalFont());
        bibliotheque.documentsTable.setRowHeight(40); // Augmentation de la hauteur des lignes
        bibliotheque.documentsTable.setShowGrid(true);
        bibliotheque.documentsTable.setGridColor(new Color(230, 230, 230));
        bibliotheque.documentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        bibliotheque.documentsTable.getTableHeader().setBackground(myStyle.getPrimaryColor());
        bibliotheque.documentsTable.getTableHeader().setForeground(Color.WHITE);
        bibliotheque.documentsTable.setSelectionBackground(new Color(173, 216, 230));

        // Ajuster la largeur des colonnes
        bibliotheque.documentsTable.getColumnModel().getColumn(4).setPreferredWidth(50); // Année
        bibliotheque.documentsTable.getColumnModel().getColumn(6).setPreferredWidth(200); // Actions

        // Ajout du renderer et editor pour la colonne des actions
        bibliotheque.documentsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonsRenderer(col));
        bibliotheque.documentsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonsEditor(new JCheckBox(), bibliotheque));

        JScrollPane scrollPane = new JScrollPane(bibliotheque.documentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

   
}
