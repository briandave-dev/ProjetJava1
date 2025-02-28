package model;
public class Magazine extends Document {
    private int moisPublication;

    public Magazine(String titre, String auteur, int anneePublication, int moisPublication) {
        super(titre, auteur, anneePublication);
        this.moisPublication = moisPublication;
    }

    public int getMoisPublication(){
        return this.moisPublication;
    }

    @Override
    public void afficherInfos() {
        System.out.println("Le titre du Magazine est : " + super.getTitre() + "\nL'auteur est : " + super.getAuteur()  );
        System.out.println("L'annee de publication est : " + super.getAnneePublication());
        System.out.println("Le mois de publication est "+ this.moisPublication);
        if(super.getDisponible() == true)
            System.out.println("Le magazine est disponible. \n");
        else System.out.println("Le magazine est indisponible. \n");
    }
    
}
