class Livre extends Document {
    private int nbrePages;

    Livre(String titre, String auteur, int anneePublication, int nbrePages) {
        super(titre, auteur, anneePublication);
        this.nbrePages = nbrePages;
    }

    public int getNombrePages(){
        return nbrePages;
    }

    @Override
    public void afficherInfos() {
        System.out.println("Le titre du Livre est : " + super.getTitre() + "\nL'auteur est : " + super.getAuteur()  );
        System.out.println("L'annee de publication est : " + super.getAnneePublication());
        System.out.println("le nombre de page est "+ this.nbrePages);
        if(super.getDisponible() == true)
            System.out.println("Le livre est disponible. \n");
        else System.out.println("Le livre est indisponible. \n");
    }
}