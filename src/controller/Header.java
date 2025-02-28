package controller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Document;
import model.Livre;
import model.Magazine;

import java.awt.*;


public class Header  {
    Bibliotheque bibliotheque;
    public Header (Bibliotheque bibliotheque){
        this.bibliotheque = bibliotheque;
    }

     JPanel createHeaderPanel() {
        StyleBiblio myStyle = new StyleBiblio();
        CreateStyledButton create = new CreateStyledButton();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(myStyle.getPrimaryColor());
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Gestion de la Bibliothèque", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("Rechercher: ");
        searchLabel.setFont(myStyle.getNormalFont());
        searchLabel.setForeground(Color.WHITE);
        
        JTextField searchField = new JTextField(15);
        searchField.setFont(myStyle.getNormalFont());
        
        JButton searchButton = create.createStyledButton("Rechercher", null);
        searchButton.setBackground(myStyle.getAccentColor());
        
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toLowerCase();
            if (searchText.isEmpty()) {
                bibliotheque.refreshTable();  // Réinitialiser le tableau si la recherche est vide
                return;
            }

            // Filtrer les documents
            bibliotheque.tableModel.setRowCount(0);
            for (int i = 0; i < bibliotheque.collection.size(); i++) {
                Document doc = bibliotheque.collection.get(i);

                // Chercher dans le titre, l'auteur et le type
                if (doc.getTitre().toLowerCase().contains(searchText) || 
                    doc.getAuteur().toLowerCase().contains(searchText) ||
                    (doc instanceof Livre && "livre".contains(searchText)) ||
                    (doc instanceof Magazine && "magazine".contains(searchText))) {
                    
                    String type = (doc instanceof Livre) ? "Livre" : "Magazine";
                    String titre = doc.getTitre();
                    String auteur = doc.getAuteur();
                    int annee = doc.getAnneePublication();
                    String details;
                    
                    if (doc instanceof Livre) {
                        Livre livre = (Livre) doc;
                        details = "Pages: " + livre.getNombrePages();
                    } else {
                        Magazine magazine = (Magazine) doc;
                        details = "Mois: " + magazine.getMoisPublication();
                    }

                    String disponibilite = doc.getDisponible() ? "Disponible" : "Emprunté";

                    bibliotheque.tableModel.addRow(new Object[]{type, titre, auteur, annee, details, disponibilite, i});
                }
            }
        });

        // Réinitialiser la recherche quand le texte change
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (searchField.getText().isEmpty()) {
                    bibliotheque.refreshTable();
                }
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (searchField.getText().isEmpty()) {
                    bibliotheque.refreshTable();
                }
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if (searchField.getText().isEmpty()) {
                    bibliotheque.refreshTable();
                }
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(searchPanel, BorderLayout.WEST);

        // Ajout du panel de tri
            JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            sortPanel.setOpaque(false);

            // Tri par titre
            JButton sortTitreAsc = create.createStyledButton("↑", null);
            sortTitreAsc.setToolTipText("Trier par titre (A-Z)");
            sortTitreAsc.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortTitreAsc.setMargin(new Insets(2, 6, 2, 6));

            JButton sortTitreDesc = create.createStyledButton("↓", null);
            sortTitreDesc.setToolTipText("Trier par titre (Z-A)");
            sortTitreDesc.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortTitreDesc.setMargin(new Insets(2, 6, 2, 6));

            JLabel sortTitreLabel = new JLabel("Titre");
            sortTitreLabel.setFont(myStyle.getNormalFont());
            sortTitreLabel.setForeground(Color.WHITE);

            // Tri par disponibilité
            JButton sortDispoButton = create.createStyledButton("↕", null);
            sortDispoButton.setToolTipText("Trier par disponibilité");
            sortDispoButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortDispoButton.setMargin(new Insets(2, 6, 2, 6));

            JLabel sortDispoLabel = new JLabel("Disponibilité");
            sortDispoLabel.setFont(myStyle.getNormalFont());
            sortDispoLabel.setForeground(Color.WHITE);

            // Action pour le tri par titre (croissant)
            sortTitreAsc.addActionListener(e -> {
                bibliotheque.sortAscending = true;
                bibliotheque.currentSortColumn = "titre";
                bibliotheque.sortCollectionAndRefresh();
            });

            // Action pour le tri par titre (décroissant)
            sortTitreDesc.addActionListener(e -> {
                bibliotheque.sortAscending = false;
                bibliotheque.currentSortColumn = "titre";
                bibliotheque.sortCollectionAndRefresh();
            });

            // Action pour le tri par disponibilité
            sortDispoButton.addActionListener(e -> {
                bibliotheque.currentSortColumn = "disponibilite";
                bibliotheque.sortCollectionAndRefresh();
            });

            sortPanel.add(sortTitreLabel);
            sortPanel.add(sortTitreAsc);
            sortPanel.add(sortTitreDesc);
            sortPanel.add(Box.createHorizontalStrut(10));
            sortPanel.add(sortDispoLabel);
            sortPanel.add(sortDispoButton);

            // Ajout des panels à l'en-tête
            JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
            leftPanel.setOpaque(false);
            leftPanel.add(searchPanel);
            leftPanel.add(sortPanel);

            headerPanel.add(leftPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        JButton loadButton = create.createStyledButton("Charger un fichier", null);
        loadButton.addActionListener(e -> bibliotheque.loadFromFile());

        JButton saveButton = create.createStyledButton("Sauvegarder", null);
        saveButton.addActionListener(e -> bibliotheque.saveToFile());

        bibliotheque.refreshButton = create.createStyledButton("Actualiser", new ImageIcon());
        bibliotheque.refreshButton.addActionListener(e -> bibliotheque.refreshTable());

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton); 
        buttonPanel.add(bibliotheque.refreshButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        return headerPanel;
    }

    
}
