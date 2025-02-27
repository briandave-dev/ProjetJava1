import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bibliotheque {
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


    Bibliotheque() {
        collection = new ArrayList<Document>();
        initializeUI();
    }

    void ajouterDocument(Document doc) {
        collection.add(doc);
        refreshTable();
    }

    void modifierDocument(int index, Document doc) {
        if (index >= 0 && index < collection.size()) {
            collection.set(index, doc);
            refreshTable();
        }
    }

    void supprimerDocument(int index) {
        if (index >= 0 && index < collection.size()) {
            collection.remove(index);
            refreshTable();
        }
    }

    void afficherDocuments() {
        for (Document document : collection) {
            document.afficherInfos();
        }
    }

    private void initializeUI() {
        // // Configurer le look and feel
        // try {
        //     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // Création du frame principal
        mainFrame = new JFrame("Gestion d'une Bibliothèque");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 700);
        mainFrame.setLocationRelativeTo(null);

        // Panel principal avec BorderLayout
        mainPanel = new JPanel(new BorderLayout(1880, 10));
        mainPanel.setBackground(secondaryColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table des documents
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Formulaire d'ajout
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.SOUTH);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

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
        
        JButton searchButton = createStyledButton("Rechercher", null);
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
            JButton sortTitreAsc = createStyledButton("↑", null);
            sortTitreAsc.setToolTipText("Trier par titre (A-Z)");
            sortTitreAsc.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortTitreAsc.setMargin(new Insets(2, 6, 2, 6));

            JButton sortTitreDesc = createStyledButton("↓", null);
            sortTitreDesc.setToolTipText("Trier par titre (Z-A)");
            sortTitreDesc.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sortTitreDesc.setMargin(new Insets(2, 6, 2, 6));

            JLabel sortTitreLabel = new JLabel("Titre");
            sortTitreLabel.setFont(normalFont);
            sortTitreLabel.setForeground(Color.WHITE);

            // Tri par disponibilité
            JButton sortDispoButton = createStyledButton("↕", null);
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        JButton loadButton = createStyledButton("Charger un fichier", null);
        loadButton.addActionListener(e -> loadFromFile());

        JButton saveButton = createStyledButton("Sauvegarder", null);
        saveButton.addActionListener(e -> saveToFile());

        refreshButton = createStyledButton("Actualiser", new ImageIcon());
        refreshButton.addActionListener(e -> refreshTable());

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton); 
        buttonPanel.add(refreshButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        return headerPanel;
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

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 50));
        tablePanel.setBackground(secondaryColor);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor),
                "Liste des Documents",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                titleFont,
                primaryColor
        ));

        // Colonnes du tableau
        String[] columns = {"Type", "Titre", "Auteur", "Année", "Détails", "Disponibilité", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne des actions est éditable
            }
        };

        // Création du tableau
        documentsTable = new JTable(tableModel);
        documentsTable.setFont(normalFont);
        documentsTable.setRowHeight(35);
        documentsTable.setShowGrid(true);
        documentsTable.setGridColor(new Color(230, 230, 230));
        documentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        documentsTable.getTableHeader().setBackground(primaryColor);
        documentsTable.getTableHeader().setForeground(Color.WHITE);
        documentsTable.setSelectionBackground(new Color(173, 216, 230));

        // Ajout du renderer et editor pour la colonne des actions
        documentsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonsRenderer());
        documentsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonsEditor(new JCheckBox()));

        // Ajout du tableau à un JScrollPanel
        JScrollPane scrollPane = new JScrollPane(documentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(secondaryColor);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor),
                "Ajouter un Document",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                titleFont,
                primaryColor
        ));

        // Types de documents
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(secondaryColor);
        JLabel typeLabel = new JLabel("Type de document: ");
        typeLabel.setFont(normalFont);
        String[] types = {"Livre", "Magazine"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setFont(normalFont);
        typePanel.add(typeLabel);
        typePanel.add(typeCombo);

        // Panneau pour les champs de formulaire
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        fieldsPanel.setBackground(secondaryColor);

        // Titre
        JLabel titreLabel = new JLabel("Titre: ");
        titreLabel.setFont(normalFont);
        JTextField titreField = new JTextField(20);
        titreField.setFont(normalFont);
        fieldsPanel.add(titreLabel);
        fieldsPanel.add(titreField);

        // Auteur
        JLabel auteurLabel = new JLabel("Auteur: ");
        auteurLabel.setFont(normalFont);
        JTextField auteurField = new JTextField(20);
        auteurField.setFont(normalFont);
        fieldsPanel.add(auteurLabel);
        fieldsPanel.add(auteurField);

        // Année de publication
        JLabel anneeLabel = new JLabel("Année de publication: ");
        anneeLabel.setFont(normalFont);
        JTextField anneeField = new JTextField(20);
        anneeField.setFont(normalFont);
        fieldsPanel.add(anneeLabel);
        fieldsPanel.add(anneeField);

        // Champ spécifique (nombre de pages ou mois de publication)
        JLabel specificLabel = new JLabel("Nombre de pages: ");
        specificLabel.setFont(normalFont);
        JTextField specificField = new JTextField(20);
        specificField.setFont(normalFont);
        fieldsPanel.add(specificLabel);
        fieldsPanel.add(specificField);

        // Mise à jour du label du champ spécifique selon le type sélectionné
        typeCombo.addActionListener(e -> {
            if (typeCombo.getSelectedItem().equals("Livre")) {
                specificLabel.setText("Nombre de pages: ");
            } else {
                specificLabel.setText("Mois de publication: ");
            }
        });

        // Bouton d'ajout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(secondaryColor);
        addButton = createStyledButton("Ajouter Document", new ImageIcon());
        buttonPanel.add(addButton);

        // Action pour le bouton d'ajout
        addButton.addActionListener(e -> {
            try {
                String titre = titreField.getText().trim();
                String auteur = auteurField.getText().trim();
                int annee = Integer.parseInt(anneeField.getText().trim());
                int specific = Integer.parseInt(specificField.getText().trim());

                if (titre.isEmpty() || auteur.isEmpty()) {
                    showMessage("Erreur", "Tous les champs sont obligatoires", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Document newDoc;
                if (typeCombo.getSelectedItem().equals("Livre")) {
                    newDoc = new Livre(titre, auteur, annee, specific);
                } else {
                    newDoc = new Magazine(titre, auteur, annee, specific);
                }

                ajouterDocument(newDoc);
                
                // Animation d'ajout réussi
                showAnimatedSuccess("Document ajouté avec succès!");
                
                // Réinitialiser les champs
                titreField.setText("");
                auteurField.setText("");
                anneeField.setText("");
                specificField.setText("");
            } catch (NumberFormatException ex) {
                showMessage("Erreur", "L'année et les détails spécifiques doivent être des nombres entiers", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                showMessage("Erreur", "Une erreur s'est produite: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });

        // Assemblage des panneaux
        formPanel.add(typePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(buttonPanel);

        return formPanel;
    }

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

    private JButton createStyledButton(String text, Icon icon) {
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
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(primaryColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }
    
    private void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(mainFrame, message, title, messageType);
    }
    
    private void showAnimatedSuccess(String message) {
        JDialog dialog = new JDialog(mainFrame, "Succès", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 2));
        
        JLabel iconLabel = new JLabel(new ImageIcon()); // Idéalement, ajoutez une icône de succès
        iconLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageLabel.setForeground(new Color(0, 128, 0));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.CENTER);
        
        dialog.add(panel, BorderLayout.CENTER);
        
        // Fermer automatiquement après 1.5 secondes
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
                dialog.dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        dialog.setVisible(true);
    }
    
    // Renderer pour les boutons dans la table
    class ButtonsRenderer extends JPanel implements TableCellRenderer {
        private JButton empruntButton, rendreButton, modifierButton, supprimerButton;
        
        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            
            empruntButton = new JButton("Emprunter");
            empruntButton.setFont(smallFont);
            empruntButton.setBackground(primaryColor);
            empruntButton.setForeground(Color.WHITE);
            empruntButton.setFocusPainted(false);
            empruntButton.setBorder(new LineBorder(primaryColor, 1));
            
            rendreButton = new JButton("Rendre");
            rendreButton.setFont(smallFont);
            rendreButton.setBackground(accentColor);
            rendreButton.setForeground(Color.WHITE);
            rendreButton.setFocusPainted(false);
            rendreButton.setBorder(new LineBorder(accentColor, 1));
            
            modifierButton = new JButton("Modifier");
            modifierButton.setFont(smallFont);
            modifierButton.setBackground(new Color(50, 205, 50)); // Vert Lime
            modifierButton.setForeground(Color.WHITE);
            modifierButton.setFocusPainted(false);
            modifierButton.setBorder(new LineBorder(new Color(50, 205, 50), 1));
            
            supprimerButton = new JButton("Supprimer");
            supprimerButton.setFont(smallFont);
            supprimerButton.setBackground(new Color(220, 20, 60)); // Rouge Crimson
            supprimerButton.setForeground(Color.WHITE);
            supprimerButton.setFocusPainted(false);
            supprimerButton.setBorder(new LineBorder(new Color(220, 20, 60), 1));
            
            add(empruntButton);
            add(rendreButton);
            add(modifierButton);
            add(supprimerButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                     boolean isSelected, boolean hasFocus, 
                                                     int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            
            if (value != null) {
                int index = (Integer) value;
                Document doc = collection.get(index);
                empruntButton.setEnabled(doc.getDisponible());
                rendreButton.setEnabled(!doc.getDisponible());
            }
            
            return this;
        }
    }
    
    // Editor pour les boutons dans la table
    class ButtonsEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton empruntButton, rendreButton, modifierButton, supprimerButton;
        private int index = -1;
        
        public ButtonsEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            empruntButton = new JButton("Emprunter");
            empruntButton.setFont(smallFont);
            empruntButton.setBackground(primaryColor);
            empruntButton.setForeground(Color.WHITE);
            empruntButton.setFocusPainted(false);
            empruntButton.setBorder(new LineBorder(primaryColor, 1));
            
            rendreButton = new JButton("Rendre");
            rendreButton.setFont(smallFont);
            rendreButton.setBackground(accentColor);
            rendreButton.setForeground(Color.WHITE);
            rendreButton.setFocusPainted(false);
            rendreButton.setBorder(new LineBorder(accentColor, 1));
            
            modifierButton = new JButton("Modifier");
            modifierButton.setFont(smallFont);
            modifierButton.setBackground(new Color(50, 205, 50)); // Vert Lime
            modifierButton.setForeground(Color.WHITE);
            modifierButton.setFocusPainted(false);
            modifierButton.setBorder(new LineBorder(new Color(50, 205, 50), 1));
            
            supprimerButton = new JButton("Supprimer");
            supprimerButton.setFont(smallFont);
            supprimerButton.setBackground(new Color(220, 20, 60)); // Rouge Crimson
            supprimerButton.setForeground(Color.WHITE);
            supprimerButton.setFocusPainted(false);
            supprimerButton.setBorder(new LineBorder(new Color(220, 20, 60), 1));
            
            empruntButton.addActionListener(e -> {
                if (index >= 0 && index < collection.size()) {
                    Document doc = collection.get(index);
                    if (doc.getDisponible()) {
                        doc.emprunterDocument();
                        showAnimatedSuccess("Document emprunté avec succès!");
                    } else {
                        showMessage("Erreur", "Le document est déjà emprunté", JOptionPane.ERROR_MESSAGE);
                    }
                    fireEditingStopped();
                    refreshTable();
                }
            });
            
            rendreButton.addActionListener(e -> {
                if (index >= 0 && index < collection.size()) {
                    Document doc = collection.get(index);
                    if (!doc.getDisponible()) {
                        doc.rendreDocument();
                        showAnimatedSuccess("Document rendu avec succès!");
                    } else {
                        showMessage("Erreur", "Le document est déjà disponible", JOptionPane.ERROR_MESSAGE);
                    }
                    fireEditingStopped();
                    refreshTable();
                }
            });
            
            modifierButton.addActionListener(e -> {
                if (index >= 0 && index < collection.size()) {
                    Document doc = collection.get(index);
                    showModifyDialog(index, doc);
                    fireEditingStopped();
                }
            });
            
            supprimerButton.addActionListener(e -> {
                if (index >= 0 && index < collection.size()) {
                    supprimerDocument(index);
                    showAnimatedSuccess("Document supprimé avec succès!");
                    fireEditingStopped();
                    refreshTable();
                }
            });
            
            panel.add(empruntButton);
            panel.add(rendreButton);
            panel.add(modifierButton);
            panel.add(supprimerButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                                                   boolean isSelected, int row, int column) {
            if (value != null) {
                index = (Integer) value;
                if (index >= 0 && index < collection.size()) {
                    Document doc = collection.get(index);
                    empruntButton.setEnabled(doc.getDisponible());
                    rendreButton.setEnabled(!doc.getDisponible());
                }
            }
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return index;
        }
    }
    private void showModifyDialog(int index, Document doc) {
        JDialog modifyDialog = new JDialog(mainFrame, "Modifier Document", true);
        modifyDialog.setLayout(new BorderLayout());
        modifyDialog.setSize(500, 400);
        modifyDialog.setLocationRelativeTo(mainFrame);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(secondaryColor);
        
        // Type du document (non modifiable)
        JLabel typeLabel = new JLabel("Type: ");
        typeLabel.setFont(normalFont);
        JLabel typeValue = new JLabel(doc instanceof Livre ? "Livre" : "Magazine");
        typeValue.setFont(normalFont);
        formPanel.add(typeLabel);
        formPanel.add(typeValue);
        
        // Titre
        JLabel titreLabel = new JLabel("Titre: ");
        titreLabel.setFont(normalFont);
        JTextField titreField = new JTextField(doc.getTitre(), 20);
        titreField.setFont(normalFont);
        formPanel.add(titreLabel);
        formPanel.add(titreField);
        
        // Auteur
        JLabel auteurLabel = new JLabel("Auteur: ");
        auteurLabel.setFont(normalFont);
        JTextField auteurField = new JTextField(doc.getAuteur(), 20);
        auteurField.setFont(normalFont);
        formPanel.add(auteurLabel);
        formPanel.add(auteurField);
        
        // Année de publication
        JLabel anneeLabel = new JLabel("Année de publication: ");
        anneeLabel.setFont(normalFont);
        JTextField anneeField = new JTextField(String.valueOf(doc.getAnneePublication()), 20);
        anneeField.setFont(normalFont);
        formPanel.add(anneeLabel);
        formPanel.add(anneeField);
        
        // Champ spécifique (nombre de pages ou mois de publication)
        JLabel specificLabel;
        JTextField specificField;
        
        if (doc instanceof Livre) {
            Livre livre = (Livre) doc;
            specificLabel = new JLabel("Nombre de pages: ");
            specificField = new JTextField(String.valueOf(livre.getNombrePages()), 20);
        } else {
            Magazine magazine = (Magazine) doc;
            specificLabel = new JLabel("Mois de publication: ");
            specificField = new JTextField(String.valueOf(magazine.getMoisPublication()), 20);
        }
        
        specificLabel.setFont(normalFont);
        specificField.setFont(normalFont);
        formPanel.add(specificLabel);
        formPanel.add(specificField);
        
        // Disponibilité
        JLabel dispLabel = new JLabel("Disponible: ");
        dispLabel.setFont(normalFont);
        JCheckBox dispCheckBox = new JCheckBox();
        dispCheckBox.setSelected(doc.getDisponible());
        dispCheckBox.setBackground(secondaryColor);
        formPanel.add(dispLabel);
        formPanel.add(dispCheckBox);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(secondaryColor);
        
        JButton cancelButton = createStyledButton("Annuler", null);
        cancelButton.setBackground(new Color(120, 120, 120));
        cancelButton.addActionListener(ev -> modifyDialog.dispose());
        
        JButton saveButton = createStyledButton("Enregistrer", null);
        saveButton.addActionListener(ev -> {
            try {
                String titre = titreField.getText().trim();
                String auteur = auteurField.getText().trim();
                int annee = Integer.parseInt(anneeField.getText().trim());
                int specific = Integer.parseInt(specificField.getText().trim());
                
                if (titre.isEmpty() || auteur.isEmpty()) {
                    showMessage("Erreur", "Tous les champs sont obligatoires", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Document updatedDoc;
                if (doc instanceof Livre) {
                    updatedDoc = new Livre(titre, auteur, annee, specific);
                } else {
                    updatedDoc = new Magazine(titre, auteur, annee, specific);
                }
                
                // Set disponibilité
                if (dispCheckBox.isSelected()) {
                    updatedDoc.rendreDocument();
                } else {
                    updatedDoc.emprunterDocument();
                }
                
                modifierDocument(index, updatedDoc);
                showAnimatedSuccess("Document modifié avec succès!");
                modifyDialog.dispose();
                refreshTable();
            } catch (NumberFormatException ex) {
                showMessage("Erreur", "L'année et les détails spécifiques doivent être des nombres entiers", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                showMessage("Erreur", "Une erreur s'est produite: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        modifyDialog.add(formPanel, BorderLayout.CENTER);
        modifyDialog.add(buttonPanel, BorderLayout.SOUTH);
        modifyDialog.setVisible(true);
    }
    
    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger les documents");
        
        if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                collection.clear();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 5) {
                        String type = parts[0];
                        String titre = parts[1];
                        String auteur = parts[2];
                        int annee = Integer.parseInt(parts[3]);
                        int specific = Integer.parseInt(parts[4]);
                        boolean disponible = Boolean.parseBoolean(parts[5]);
                        
                        Document doc;
                        if (type.equals("Livre")) {
                            doc = new Livre(titre, auteur, annee, specific);
                        } else {
                            doc = new Magazine(titre, auteur, annee, specific);
                        }
                        
                        if (!disponible) {
                            doc.emprunterDocument();
                        }
                        
                        collection.add(doc);
                    }
                }
                refreshTable();
                showAnimatedSuccess("Documents chargés avec succès!");
            } catch (IOException ex) {
                showMessage("Erreur", "Erreur lors de la lecture du fichier: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                showMessage("Erreur", "Format de fichier incorrect", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarder les documents");
        
        if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Document doc : collection) {
                    String type = (doc instanceof Livre) ? "Livre" : "Magazine";
                    String specific = (doc instanceof Livre) ? 
                        String.valueOf(((Livre)doc).getNombrePages()) :
                        String.valueOf(((Magazine)doc).getMoisPublication());
                        
                    writer.write(String.format("%s;%s;%s;%d;%s;%b%n",
                        type,
                        doc.getTitre(),
                        doc.getAuteur(),
                        doc.getAnneePublication(),
                        specific,
                        doc.getDisponible()
                    ));
                }
                showAnimatedSuccess("Documents sauvegardés avec succès!");
            } catch (IOException ex) {
                showMessage("Erreur", "Erreur lors de la sauvegarde: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Bibliotheque mesDoc = new Bibliotheque();
            
            // Ajouter quelques documents de test
            // mesDoc.ajouterDocument(new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", 1943, 96));
            // mesDoc.ajouterDocument(new Magazine("National Geographic", "National Geographic Society", 2023, 3));
            // mesDoc.ajouterDocument(new Livre("L'Étranger", "Albert Camus", 1942, 159));
            // mesDoc.ajouterDocument(new Magazine("Science & Vie", "Mondadori France", 2022, 6));
        });
    }
}