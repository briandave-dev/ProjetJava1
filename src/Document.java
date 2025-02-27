public abstract class Document {
    private String titre, auteur;
    private int anneePublication;
    private boolean disponible;
    

    Document(String titre, String auteur, int anneePublication){
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = true;
    }

    public abstract void afficherInfos();

    public int getAnneePublication(){
        return this.anneePublication;
    }

    public String getTitre(){
        return this.titre;
    }

    public boolean getDisponible(){
        return this.disponible;
    }

    public String getAuteur(){
        return this.auteur;
    }

    public void emprunterDocument(){
        if(this.disponible == false){
            System.out.println("Le livre : " + this.titre + " n'est pas disponible");
            return;
        }
        this.disponible = false;
        System.out.println("Le livre : " + this.titre + " a ete emprunte avec succes");
    }

    public void rendreDocument(){
        if(this.disponible == true){
            System.out.println("Le livre : " + this.titre + " n'a pas ete emprunte");
            return;
        }
        this.disponible = true;
        System.out.println("Le livre : " + this.titre + " a ete remis avec succes");
    }

}
