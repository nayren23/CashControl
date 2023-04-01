package modele;

//Utiliser pour l'arrayAdaptater pour avoir un id non visible par l'user dans la class AffichageDetaillerDepenseActivity
public class DepenseElement {

    private Depense depense;
    private int id;

    public DepenseElement(Depense depense, int id) {
        this.depense = depense;
        this.id = id;
    }

    public Depense getDepense() {
        return depense;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return  depense.getDate()
                + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" //pour un affichge propre de la liste
                + depense.getMontant() + " €"
                + "\n" + depense.getDescriptionDepense()
                +"\n";
    }
}
