package modele;

public class Categorie {

    private int CategorieId;
    private String nom;


    public Categorie() {
        this.CategorieId++;
    }

    public Categorie(int categorieId, String nom) {
        this.CategorieId = categorieId;
        this.nom = nom;
    }




    public int getCategorieId() {
        return CategorieId;
    }

    public void setCategorieId(int categorieId) {
        CategorieId = categorieId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
