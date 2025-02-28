package controller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.*;

import model.Document ;
import model.Livre;
import model.Magazine;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bibliotheque {
    protected List<Document> collection;
    protected JFrame mainFrame;
    protected JTable documentsTable;
    protected DefaultTableModel tableModel;
    public JPanel mainPanel;
    protected JButton addButton, refreshButton;
    private Color primaryColor = new Color(65, 105, 225); // Bleu royal
    private Color secondaryColor = new Color(240, 248, 255); // Alice Blue
    private Color accentColor = new Color(255, 99, 71); // Tomato
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);
    protected boolean sortAscending = true;
    protected String currentSortColumn = "";

    //modularite
    CreateStyledButton createBtn = new CreateStyledButton();
    Header myHeader;
    Table myTable;
    Form myForm;

    public Bibliotheque() {
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
            // Ajoutez cette ligne pour forcer la mise à jour des éditeurs
            documentsTable.getColumnModel().getColumn(6).getCellEditor().cancelCellEditing();
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
        createBtn = new CreateStyledButton();
         myHeader = new Header(this);
         myTable = new Table(this);
         myForm = new Form(this);
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
        JPanel headerPanel = myHeader.createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central pour contenir le formulaire et la table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(secondaryColor);

        // Formulaire d'ajout (maintenant en haut)
        JPanel formPanel = myForm.createFormPanel();
        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Table des documents (maintenant en bas)
        JPanel tablePanel = myTable.createTablePanel(collection);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        // Ajouter un écouteur pour la fermeture de la fenêtre
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(mainFrame, 
                    "Voulez-vous sauvegarder votre travail ?", 
                    "Sauvegarde", 
                    JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    saveToFile(); // Appeler la méthode pour sauvegarder
                }
                mainFrame.dispose(); // Fermer l'application
            }
        });
    }

     protected void sortCollectionAndRefresh() {
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

    protected void refreshTable() {
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
        
        // Ajoutez ces lignes pour réinitialiser l'éditeur
        if (documentsTable != null && documentsTable.getColumnModel().getColumnCount() > 6) {
            TableCellEditor editor = documentsTable.getColumnModel().getColumn(6).getCellEditor();
            if (editor != null) {
                editor.cancelCellEditing();
            }
        }
    }

    protected void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(mainFrame, message, title, messageType);
    }
    
    protected void showAnimatedSuccess(String message) {
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
    
    // Editor pour les boutons dans la table
    class ButtonsEditor extends DefaultCellEditor {
        protected JPanel panel;
        private JButton empruntButton, rendreButton, modifierButton, supprimerButton;
        private int index = -1;
        private boolean isPushed;
        
        public ButtonsEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
            panel.setBackground(Color.WHITE);
            
            // Création des boutons avec des dimensions fixes et padding réduit
            Dimension buttonSize = new Dimension(80, 25);
            
            empruntButton = createActionButton("Emprunter", primaryColor, buttonSize);
            rendreButton = createActionButton("Rendre", accentColor, buttonSize);
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
            button.setFont(smallFont);
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
                if (index >= 0 && index < collection.size()) {
                    Document doc = collection.get(index);
                    if (button == empruntButton && doc.getDisponible()) {
                        doc.emprunterDocument();
                        refreshTable();
                        showAnimatedSuccess("Document emprunté avec succès!");
                    } else if (button == rendreButton && !doc.getDisponible()) {
                        doc.rendreDocument();
                        refreshTable();
                        showAnimatedSuccess("Document rendu avec succès!");
                    } else if (button == modifierButton) {
                        showModifyDialog(index, doc);
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
                if (index >= 0 && index < collection.size()) {
                    Document doc = collection.get(index);
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
            if (index >= 0 && index < collection.size()) {
                Document doc = collection.get(index);
                int response = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Êtes-vous sûr de vouloir supprimer le document \"" + doc.getTitre() + "\" ?\n" +
                    "Cette action est irréversible.",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    supprimerDocument(index);
                    // Réinitialiser l'index après la suppression
                    this.index = -1;
                    showAnimatedSuccess("Document supprimé avec succès!");
                    fireEditingStopped();
                }
            }
        }
    }
    public void showModifyDialog(int index, Document doc) {
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
        NumericTextField(anneeField);
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
       NumericTextField(specificField);
        specificLabel.setFont(normalFont);
        specificField.setFont(normalFont);
        formPanel.add(specificLabel);
        formPanel.add(specificField);
        
        
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(secondaryColor);
        
        JButton cancelButton = createBtn.createStyledButton("Annuler", null);
        cancelButton.setBackground(new Color(120, 120, 120));
        cancelButton.addActionListener(ev -> modifyDialog.dispose());
        
        JButton saveButton = createBtn.createStyledButton("Enregistrer", null);
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
                    if(specific<1 || specific > 12) {
                        showMessage("Erreur", "Il n'y a que 12 mois grand", JOptionPane.ERROR_MESSAGE);    
                        return;
                    }
                    updatedDoc = new Magazine(titre, auteur, annee, specific);
                }
                
                // Set disponibilité
                if (doc.getDisponible()) {
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
    
    protected void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger les documents");
        
        if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                // collection.clear();
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
                        
                        ajouterDocument(doc);
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

    protected void saveToFile() {
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
    
    protected void NumericTextField(JTextField textField){
        // Appliquer le filtre pour n'accepter que les nombres
        ((PlainDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                
                // Vérifier si la chaîne ne contient que des chiffres
                if (string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                
                // Vérifier si le texte ne contient que des chiffres
                if (text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
    }