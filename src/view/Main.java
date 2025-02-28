import javax.swing.SwingUtilities;

import controller.Bibliotheque;

public class Main {
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
