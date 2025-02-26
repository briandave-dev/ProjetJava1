// import javax.swing.*;
// import javax.swing.border.EmptyBorder;
// import javax.swing.table.DefaultTableModel;
// import javax.swing.table.TableCellRenderer;
// import javax.swing.table.TableCellEditor;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.ArrayList;
// import java.util.List;
// import javax.swing.border.LineBorder;
// import java.util.concurrent.TimeUnit;

// public class Bibliotheque {
//     private List<Document> collection;
//     private JFrame mainFrame;
//     private JTable documentsTable;
//     private DefaultTableModel tableModel;
//     private JPanel mainPanel;
//     private JButton addButton, refreshButton;
//     private Color primaryColor = new Color(65, 105, 225); // Bleu royal
//     private Color secondaryColor = new Color(240, 248, 255); // Alice Blue
//     private Color accentColor = new Color(255, 99, 71); // Tomato
//     private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
//     private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
//     private Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);

//     Bibliotheque() {
//         collection = new ArrayList<Document>();
//         initializeUI();
//     }

//     void ajouterDocument(Document doc) {
//         collection.add(doc);
//         refreshTable();
//     }

//     void afficherDocuments() {
//         for (Document document : collection) {
//             document.afficherInfos();
//         }
//     }

//     private void initializeUI() {
//         // Configurer le look and feel
//         try {
//             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         // Création du frame principal
//         mainFrame = new JFrame("Gestion d'une Bibliothèque");
//         mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         mainFrame.setSize(900, 700);
//         mainFrame.setLocationRelativeTo(null);

//         // Panel principal avec BorderLayout
//         mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBackground(secondaryColor);
//         mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

//         // Header
//         JPanel headerPanel = createHeaderPanel();
//         mainPanel.add(headerPanel, BorderLayout.NORTH);

//         // Table des documents
//         JPanel tablePanel = createTablePanel();
//         mainPanel.add(tablePanel, BorderLayout.CENTER);

//         // Formulaire d'ajout
//         JPanel formPanel = createFormPanel();
//         mainPanel.add(formPanel, BorderLayout.SOUTH);

//         mainFrame.add(mainPanel);
//         mainFrame.setVisible(true);
//     }

//     private JPanel createHeaderPanel() {
//         JPanel headerPanel = new JPanel(new BorderLayout());
//         headerPanel.setBackground(primaryColor);
//         headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

//         JLabel titleLabel = new JLabel("Gestion de la Bibliothèque", JLabel.CENTER);
//         titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
//         titleLabel.setForeground(Color.WHITE);
//         headerPanel.add(titleLabel, BorderLayout.CENTER);

//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         buttonPanel.setOpaque(false);

//         refreshButton = createStyledButton("Actualiser", new ImageIcon());
//         refreshButton.addActionListener(e -> refreshTable());
//         buttonPanel.add(refreshButton);

//         headerPanel.add(buttonPanel, BorderLayout.EAST);
//         return headerPanel;
//     }

//     private JPanel createTablePanel() {
//         JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
//         tablePanel.setBackground(secondaryColor);
//         tablePanel.setBorder(BorderFactory.createTitledBorder(
//                 BorderFactory.createLineBorder(primaryColor),
//                 "Liste des Documents",
//                 javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
//                 javax.swing.border.TitledBorder.DEFAULT_POSITION,
//                 titleFont,
//                 primaryColor
//         ));

//         // Colonnes du tableau
//         String[] columns = {"Type", "Titre", "Auteur", "Année", "Détails", "Disponibilité", "Actions"};
//         tableModel = new DefaultTableModel(columns, 0) {
//             @Override
//             public boolean isCellEditable(int row, int column) {
//                 return column == 6; // Seule la colonne des actions est éditable
//             }
//         };

//         // Création du tableau
//         documentsTable = new JTable(tableModel);
//         documentsTable.setFont(normalFont);
//         documentsTable.setRowHeight(35);
//         documentsTable.setShowGrid(true);
//         documentsTable.setGridColor(new Color(230, 230, 230));
//         documentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
//         documentsTable.getTableHeader().setBackground(primaryColor);
//         documentsTable.getTableHeader().setForeground(Color.BLACK);
//         documentsTable.setSelectionBackground(new Color(173, 216, 230));

//         // Ajout du renderer et editor pour la colonne des actions
//         documentsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonsRenderer());
//         documentsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonsEditor(new JCheckBox()));

//         // Ajout du tableau à un JScrollPanel
//         JScrollPane scrollPane = new JScrollPane(documentsTable);
//         scrollPane.setBorder(BorderFactory.createEmptyBorder());
//         tablePanel.add(scrollPane, BorderLayout.CENTER);

//         return tablePanel;
//     }

//     private JPanel createFormPanel() {
//         JPanel formPanel = new JPanel();
//         formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
//         formPanel.setBackground(secondaryColor);
//         formPanel.setBorder(BorderFactory.createTitledBorder(
//                 BorderFactory.createLineBorder(primaryColor),
//                 "Ajouter un Document",
//                 javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
//                 javax.swing.border.TitledBorder.DEFAULT_POSITION,
//                 titleFont,
//                 primaryColor
//         ));

//         // Types de documents
//         JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         typePanel.setBackground(secondaryColor);
//         JLabel typeLabel = new JLabel("Type de document: ");
//         typeLabel.setFont(normalFont);
//         String[] types = {"Livre", "Magazine"};
//         JComboBox<String> typeCombo = new JComboBox<>(types);
//         typeCombo.setFont(normalFont);
//         typePanel.add(typeLabel);
//         typePanel.add(typeCombo);

//         // Panneau pour les champs de formulaire
//         JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
//         fieldsPanel.setBackground(secondaryColor);

//         // Titre
//         JLabel titreLabel = new JLabel("Titre: ");
//         titreLabel.setFont(normalFont);
//         JTextField titreField = new JTextField(20);
//         titreField.setFont(normalFont);
//         fieldsPanel.add(titreLabel);
//         fieldsPanel.add(titreField);

//         // Auteur
//         JLabel auteurLabel = new JLabel("Auteur: ");
//         auteurLabel.setFont(normalFont);
//         JTextField auteurField = new JTextField(20);
//         auteurField.setFont(normalFont);
//         fieldsPanel.add(auteurLabel);
//         fieldsPanel.add(auteurField);

//         // Année de publication
//         JLabel anneeLabel = new JLabel("Année de publication: ");
//         anneeLabel.setFont(normalFont);
//         JTextField anneeField = new JTextField(20);
//         anneeField.setFont(normalFont);
//         fieldsPanel.add(anneeLabel);
//         fieldsPanel.add(anneeField);

//         // Champ spécifique (nombre de pages ou mois de publication)
//         JLabel specificLabel = new JLabel("Nombre de pages: ");
//         specificLabel.setFont(normalFont);
//         JTextField specificField = new JTextField(20);
//         specificField.setFont(normalFont);
//         fieldsPanel.add(specificLabel);
//         fieldsPanel.add(specificField);

//         // Mise à jour du label du champ spécifique selon le type sélectionné
//         typeCombo.addActionListener(e -> {
//             if (typeCombo.getSelectedItem().equals("Livre")) {
//                 specificLabel.setText("Nombre de pages: ");
//             } else {
//                 specificLabel.setText("Mois de publication: ");
//             }
//         });

//         // Bouton d'ajout
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         buttonPanel.setBackground(secondaryColor);
//         addButton = createStyledButton("Ajouter Document", new ImageIcon());
//         buttonPanel.add(addButton);

//         // Action pour le bouton d'ajout
//         addButton.addActionListener(e -> {
//             try {
//                 String titre = titreField.getText().trim();
//                 String auteur = auteurField.getText().trim();
//                 int annee = Integer.parseInt(anneeField.getText().trim());
//                 int specific = Integer.parseInt(specificField.getText().trim());

//                 if (titre.isEmpty() || auteur.isEmpty()) {
//                     showMessage("Erreur", "Tous les champs sont obligatoires", JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 Document newDoc;
//                 if (typeCombo.getSelectedItem().equals("Livre")) {
//                     newDoc = new Livre(titre, auteur, annee, specific);
//                 } else {
//                     newDoc = new Magazine(titre, auteur, annee, specific);
//                 }

//                 ajouterDocument(newDoc);
                
//                 // Animation d'ajout réussi
//                 showAnimatedSuccess("Document ajouté avec succès!");
                
//                 // Réinitialiser les champs
//                 titreField.setText("");
//                 auteurField.setText("");
//                 anneeField.setText("");
//                 specificField.setText("");
//             } catch (NumberFormatException ex) {
//                 showMessage("Erreur", "L'année et les détails spécifiques doivent être des nombres entiers", JOptionPane.ERROR_MESSAGE);
//             } catch (Exception ex) {
//                 showMessage("Erreur", "Une erreur s'est produite: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
//             }
//         });

//         // Assemblage des panneaux
//         formPanel.add(typePanel);
//         formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//         formPanel.add(fieldsPanel);
//         formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//         formPanel.add(buttonPanel);

//         return formPanel;
//     }

//     private void refreshTable() {
//         tableModel.setRowCount(0);
        
//         for (int i = 0; i < collection.size(); i++) {
//             Document doc = collection.get(i);
//             String type = (doc instanceof Livre) ? "Livre" : "Magazine";
//             String titre = doc.getTitre();
//             String auteur = doc.getAuteur();
//             int annee = doc.getAnneePublication();
//             String details;
            
//             if (doc instanceof Livre) {
//                 // Accès aux détails spécifiques via afficherInfos pour contourner le problème d'accès privé
//                 Livre livre = (Livre) doc;
//                 // Extraction du nombre de pages à partir de la sortie système (solution temporaire)
//                 details = "Détails du livre";
//             } else {
//                 Magazine magazine = (Magazine) doc;
//                 // Extraction du mois de publication à partir de la sortie système (solution temporaire)
//                 details = "Détails du magazine";
//             }
            
//             String disponibilite = doc.getDisponible() ? "Disponible" : "Emprunté";
            
//             tableModel.addRow(new Object[]{type, titre, auteur, annee, details, disponibilite, i});
//         }
//     }

//     private JButton createStyledButton(String text, Icon icon) {
//         JButton button = new JButton(text);
//         button.setFont(normalFont);
//         button.setBackground(primaryColor);
//         button.setForeground(Color.BLACK);
//         button.setFocusPainted(false);
//         button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
//         if (icon != null) {
//             button.setIcon(icon);
//         }
        
//         button.addMouseListener(new MouseAdapter() {
//             @Override
//             public void mouseEntered(MouseEvent e) {
//                 button.setBackground(primaryColor.darker());
//             }
            
//             @Override
//             public void mouseExited(MouseEvent e) {
//                 button.setBackground(primaryColor);
//             }
//         });
        
//         return button;
//     }
    
//     private void showMessage(String title, String message, int messageType) {
//         JOptionPane.showMessageDialog(mainFrame, message, title, messageType);
//     }
    
//     private void showAnimatedSuccess(String message) {
//         JDialog dialog = new JDialog(mainFrame, "Succès", true);
//         dialog.setLayout(new BorderLayout());
//         dialog.setSize(300, 120);
//         dialog.setLocationRelativeTo(mainFrame);
//         dialog.setUndecorated(true);
//         dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 2));
        
//         JLabel iconLabel = new JLabel(new ImageIcon()); // Idéalement, ajoutez une icône de succès
//         iconLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
//         JLabel messageLabel = new JLabel(message, JLabel.CENTER);
//         messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
//         messageLabel.setForeground(new Color(0, 128, 0));
        
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setBackground(Color.BLACK);
//         panel.add(iconLabel, BorderLayout.WEST);
//         panel.add(messageLabel, BorderLayout.CENTER);
        
//         dialog.add(panel, BorderLayout.CENTER);
        
//         // Fermer automatiquement après 1.5 secondes
//         new Thread(() -> {
//             try {
//                 TimeUnit.MILLISECONDS.sleep(1500);
//                 dialog.dispose();
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }).start();
        
//         dialog.setVisible(true);
//     }
    
//     // Renderer pour les boutons dans la table
//     class ButtonsRenderer extends JPanel implements TableCellRenderer {
//         private JButton empruntButton, rendreButton;
        
//         public ButtonsRenderer() {
//             setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
//             setOpaque(true);
            
//             empruntButton = new JButton("Emprunter");
//             empruntButton.setFont(smallFont);
//             empruntButton.setBackground(primaryColor);
//             empruntButton.setForeground(Color.WHITE);
//             empruntButton.setFocusPainted(false);
//             empruntButton.setBorder(new LineBorder(primaryColor, 1));
            
//             rendreButton = new JButton("Rendre");
//             rendreButton.setFont(smallFont);
//             rendreButton.setBackground(accentColor);
//             rendreButton.setForeground(Color.WHITE);
//             rendreButton.setFocusPainted(false);
//             rendreButton.setBorder(new LineBorder(accentColor, 1));
            
//             add(empruntButton);
//             add(rendreButton);
//         }
        
//         @Override
//         public Component getTableCellRendererComponent(JTable table, Object value, 
//                                                      boolean isSelected, boolean hasFocus, 
//                                                      int row, int column) {
//             setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            
//             if (value != null) {
//                 int index = (Integer) value;
//                 Document doc = collection.get(index);
//                 empruntButton.setEnabled(doc.getDisponible());
//                 rendreButton.setEnabled(!doc.getDisponible());
//             }
            
//             return this;
//         }
//     }
    
//     // Editor pour les boutons dans la table
//     class ButtonsEditor extends DefaultCellEditor {
//         private JPanel panel;
//         private JButton empruntButton, rendreButton;
//         private int index = -1;
        
//         public ButtonsEditor(JCheckBox checkBox) {
//             super(checkBox);
            
//             panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
//             empruntButton = new JButton("Emprunter");
//             empruntButton.setFont(smallFont);
//             empruntButton.setBackground(primaryColor);
//             empruntButton.setForeground(Color.BLACK);
//             empruntButton.setFocusPainted(false);
//             empruntButton.setBorder(new LineBorder(primaryColor, 1));
            
//             rendreButton = new JButton("Rendre");
//             rendreButton.setFont(smallFont);
//             rendreButton.setBackground(accentColor);
//             rendreButton.setForeground(Color.BLACK);
//             rendreButton.setFocusPainted(false);
//             rendreButton.setBorder(new LineBorder(accentColor, 1));
            
//             empruntButton.addActionListener(e -> {
//                 if (index >= 0 && index < collection.size()) {
//                     Document doc = collection.get(index);
//                     if (doc.getDisponible()) {
//                         doc.emprunterDocument();
//                         showAnimatedSuccess("Document emprunté avec succès!");
//                     } else {
//                         showMessage("Erreur", "Le document est déjà emprunté", JOptionPane.ERROR_MESSAGE);
//                     }
//                     fireEditingStopped();
//                     refreshTable();
//                 }
//             });
            
//             rendreButton.addActionListener(e -> {
//                 if (index >= 0 && index < collection.size()) {
//                     Document doc = collection.get(index);
//                     if (!doc.getDisponible()) {
//                         doc.rendreDocument();
//                         showAnimatedSuccess("Document rendu avec succès!");
//                     } else {
//                         showMessage("Erreur", "Le document est déjà disponible", JOptionPane.ERROR_MESSAGE);
//                     }
//                     fireEditingStopped();
//                     refreshTable();
//                 }
//             });
            
//             panel.add(empruntButton);
//             panel.add(rendreButton);
//         }
        
//         @Override
//         public Component getTableCellEditorComponent(JTable table, Object value, 
//                                                    boolean isSelected, int row, int column) {
//             if (value != null) {
//                 index = (Integer) value;
//                 if (index >= 0 && index < collection.size()) {
//                     Document doc = collection.get(index);
//                     empruntButton.setEnabled(doc.getDisponible());
//                     rendreButton.setEnabled(!doc.getDisponible());
//                 }
//             }
            
//             return panel;
//         }
        
//         @Override
//         public Object getCellEditorValue() {
//             return index;
//         }
//     }
    
//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             Bibliotheque mesDoc = new Bibliotheque();
            
//             // Ajouter quelques documents de test
//             mesDoc.ajouterDocument(new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", 1943, 96));
//             mesDoc.ajouterDocument(new Magazine("National Geographic", "National Geographic Society", 2023, 3));
//             mesDoc.ajouterDocument(new Livre("L'Étranger", "Albert Camus", 1942, 159));
//             mesDoc.ajouterDocument(new Magazine("Science & Vie", "Mondadori France", 2022, 6));
//         });
//     }
// }

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import java.util.concurrent.TimeUnit;

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
        // Configurer le look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Création du frame principal
        mainFrame = new JFrame("Gestion d'une Bibliothèque");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 700);
        mainFrame.setLocationRelativeTo(null);

        // Panel principal avec BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        refreshButton = createStyledButton("Actualiser", new ImageIcon());
        refreshButton.addActionListener(e -> refreshTable());
        buttonPanel.add(refreshButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
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
        documentsTable.getTableHeader().setForeground(Color.BLACK);
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
        button.setForeground(Color.BLACK);
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
        panel.setBackground(Color.BLACK);
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
            empruntButton.setForeground(Color.BLACK);
            empruntButton.setFocusPainted(false);
            empruntButton.setBorder(new LineBorder(primaryColor, 1));
            
            rendreButton = new JButton("Rendre");
            rendreButton.setFont(smallFont);
            rendreButton.setBackground(accentColor);
            rendreButton.setForeground(Color.BLACK);
            rendreButton.setFocusPainted(false);
            rendreButton.setBorder(new LineBorder(accentColor, 1));
            
            modifierButton = new JButton("Modifier");
            modifierButton.setFont(smallFont);
            modifierButton.setBackground(new Color(50, 205, 50)); // Vert Lime
            modifierButton.setForeground(Color.BLACK);
            modifierButton.setFocusPainted(false);
            modifierButton.setBorder(new LineBorder(new Color(50, 205, 50), 1));
            
            supprimerButton = new JButton("Supprimer");
            supprimerButton.setFont(smallFont);
            supprimerButton.setBackground(new Color(220, 20, 60)); // Rouge Crimson
            supprimerButton.setForeground(Color.BLACK);
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
                    modifierDocument(index, doc);
                    showAnimatedSuccess("Document modifié avec succès!");
                    fireEditingStopped();
                    refreshTable();
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Bibliotheque mesDoc = new Bibliotheque();
            
            // Ajouter quelques documents de test
            mesDoc.ajouterDocument(new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", 1943, 96));
            mesDoc.ajouterDocument(new Magazine("National Geographic", "National Geographic Society", 2023, 3));
            mesDoc.ajouterDocument(new Livre("L'Étranger", "Albert Camus", 1942, 159));
            mesDoc.ajouterDocument(new Magazine("Science & Vie", "Mondadori France", 2022, 6));
        });
    }
}