import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


public  class HeaderPanel {
    private List<Document> collection;
    private JFrame mainFrame;
    private JTable documentsTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JButton addButton, refreshButton;
    private Color primaryColor = new Color(65, 105, 225); // Bleu royal
    private Color secondaryColor = new Color(240, 248, 255); // Alice Blue
    private Color accentColor = new Color(255, 99, 71); // Tomato
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);
    private boolean sortAscending = true;
    private String currentSortColumn = "";

    private void refreshTable() {
        tableModel.setRowCount(0);
        
        for (int i = 0; i < collection.size(); i++) {
            Document doc = collection.get(i);
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
            
            tableModel.addRow(new Object[]{type, titre, auteur, annee, details, disponibilite, i});
        }
    }

    private void sortCollectionAndRefresh() {
        if (currentSortColumn.equals("titre")) {
            // Tri par titre
            collection.sort((doc1, doc2) -> {
                int result = doc1.getTitre().compareToIgnoreCase(doc2.getTitre());
                return sortAscending ? result : -result;
            });
        } else if (currentSortColumn.equals("disponibilite")) {
            // Tri par disponibilité (disponibles en haut)
            collection.sort((doc1, doc2) -> {
                if (doc1.getDisponible() == doc2.getDisponible()) {
                    return 0;
                }
                return doc1.getDisponible() ? -1 : 1;  // Les disponibles en premier
            });
        }
        
        refreshTable();
    }

    StyledButton styledButton = new StyledButton();

        private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Gestion de la Bibliothèque", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("Rechercher: ");
        searchLabel.setFont(normalFont);
        searchLabel.setForeground(Color.WHITE);
        
        JTextField searchField = new JTextField(15);
        searchField.setFont(normalFont);
        
        JButton searchButton = styledButton.createStyledButton("Rechercher", null);
        searchButton.setBackground(accentColor);
        
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toLowerCase();
            if (searchText.isEmpty()) {
                refreshTable();  // Réinitialiser le tableau si la recherche est vide
                return;
            }

            // Filtrer les documents
            tableModel.setRowCount(0);
            for (int i = 0; i < collection.size(); i++) {
                Document doc = collection.get(i);

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

                    tableModel.addRow(new Object[]{type, titre, auteur, annee, details, disponibilite, i});
                }
            }
        });

        // Réinitialiser la recherche quand le texte change
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (searchField.getText().isEmpty()) {
                    refreshTable();
                }
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (searchField.getText().isEmpty()) {
                    refreshTable();
                }
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if (searchField.getText().isEmpty()) {
                    refreshTable();
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
            JButton sortTitreAsc = styledButton.createStyledButton("↑", null);
            sortTitreAsc.setToolTipText("Trier par titre (A-Z)");
            sortTitreAsc.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortTitreAsc.setMargin(new Insets(2, 6, 2, 6));

            JButton sortTitreDesc = styledButton.createStyledButton("↓", null);
            sortTitreDesc.setToolTipText("Trier par titre (Z-A)");
            sortTitreDesc.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortTitreDesc.setMargin(new Insets(2, 6, 2, 6));

            JLabel sortTitreLabel = new JLabel("Titre");
            sortTitreLabel.setFont(normalFont);
            sortTitreLabel.setForeground(Color.WHITE);

            // Tri par disponibilité
            JButton sortDispoButton = styledButton.createStyledButton("↕", null);
            sortDispoButton.setToolTipText("Trier par disponibilité");
            sortDispoButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortDispoButton.setMargin(new Insets(2, 6, 2, 6));

            JLabel sortDispoLabel = new JLabel("Disponibilité");
            sortDispoLabel.setFont(normalFont);
            sortDispoLabel.setForeground(Color.WHITE);

            // Action pour le tri par titre (croissant)
            sortTitreAsc.addActionListener(e -> {
                sortAscending = true;
                currentSortColumn = "titre";
                sortCollectionAndRefresh();
            });

            // Action pour le tri par titre (décroissant)
            sortTitreDesc.addActionListener(e -> {
                sortAscending = false;
                currentSortColumn = "titre";
                sortCollectionAndRefresh();
            });

            // Action pour le tri par disponibilité
            sortDispoButton.addActionListener(e -> {
                currentSortColumn = "disponibilite";
                sortCollectionAndRefresh();
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        refreshButton = styledButton.createStyledButton("Actualiser", new ImageIcon());
        refreshButton.addActionListener(e -> refreshTable());
        buttonPanel.add(refreshButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        return headerPanel;
    }

}