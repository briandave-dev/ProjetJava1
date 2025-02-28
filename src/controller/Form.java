package controller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.*;

import model.Document;
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

public class Form {
      Bibliotheque bibliotheque;

      public Form(Bibliotheque bibliotheque){
        this.bibliotheque = bibliotheque;
      }

      JPanel createFormPanel() {
        StyleBiblio myStyle = new StyleBiblio();
        CreateStyledButton create = new CreateStyledButton();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(myStyle.getSecondaryColor());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(myStyle.getPrimaryColor()),
                "Ajouter un Document",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                myStyle.getTitleFont(),
                myStyle.getPrimaryColor()
        ));

        // Types de documents
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(myStyle.getSecondaryColor());
        JLabel typeLabel = new JLabel("Type de document: ");
        typeLabel.setFont(myStyle.getNormalFont());
        String[] types = {"Livre", "Magazine"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setFont(myStyle.getNormalFont());
        typePanel.add(typeLabel);
        typePanel.add(typeCombo);

        // Panneau pour les champs de formulaire
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        fieldsPanel.setBackground(myStyle.getSecondaryColor());

        // Titre
        JLabel titreLabel = new JLabel("Titre: ");
        titreLabel.setFont(myStyle.getNormalFont());
        JTextField titreField = new JTextField(20);
        titreField.setFont(myStyle.getNormalFont());
        fieldsPanel.add(titreLabel);
        fieldsPanel.add(titreField);

        // Auteur
        JLabel auteurLabel = new JLabel("Auteur: ");
        auteurLabel.setFont(myStyle.getNormalFont());
        JTextField auteurField = new JTextField(20);
        
        auteurField.setFont(myStyle.getNormalFont());
        fieldsPanel.add(auteurLabel);
        fieldsPanel.add(auteurField);

        // Année de publication
        JLabel anneeLabel = new JLabel("Année de publication: ");
        anneeLabel.setFont(myStyle.getNormalFont());
        JTextField anneeField = new JTextField(20);
        anneeField.setText("0");
        bibliotheque.NumericTextField(anneeField);
        anneeField.setFont(myStyle.getNormalFont());
        fieldsPanel.add(anneeLabel);
        fieldsPanel.add(anneeField);

        // Champ spécifique (nombre de pages ou mois de publication)
        JLabel specificLabel = new JLabel("Nombre de pages: ");
        specificLabel.setFont(myStyle.getNormalFont());
        JTextField specificField = new JTextField(20);
        specificField.setText("0");
        bibliotheque.NumericTextField(specificField);
        specificField.setFont(myStyle.getNormalFont());
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
        buttonPanel.setBackground(myStyle.getSecondaryColor());
        bibliotheque.addButton = create.createStyledButton("Ajouter Document", new ImageIcon());
        buttonPanel.add(bibliotheque.addButton);

        // Action pour le bouton d'ajout
        bibliotheque.addButton.addActionListener(e -> {
            try {
                String titre = titreField.getText().trim();
                String auteur = auteurField.getText().trim();
                int annee = Integer.parseInt(anneeField.getText().trim());
                int specific = Integer.parseInt(specificField.getText().trim());

                if (titre.isEmpty() || auteur.isEmpty()) {
                    bibliotheque.showMessage("Erreur", "Tous les champs sont obligatoires", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Document newDoc;
                if (typeCombo.getSelectedItem().equals("Livre")) {
                    
                    newDoc = new Livre(titre, auteur, annee, specific);
                } else { 
                	
                    if (specific<1 || specific > 12) {
                        bibliotheque.showMessage("Erreur", "Il n'y a que 12 mois grand", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newDoc = new Magazine(titre, auteur, annee, specific);
                   
                }

                bibliotheque.ajouterDocument(newDoc);
                
                // Animation d'ajout réussi
                bibliotheque.showAnimatedSuccess("Document ajouté avec succès!");
                
                // Réinitialiser les champs
                titreField.setText("");
                auteurField.setText("");
                anneeField.setText("0");
                specificField.setText("0");
            } catch (NumberFormatException ex) {
                bibliotheque.showMessage("Erreur", "L'année et les détails spécifiques doivent être des nombres entiers", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                bibliotheque.showMessage("Erreur", "Une erreur s'est produite: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
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

   
}
