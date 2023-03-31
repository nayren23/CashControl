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
        return "Montant : " + depense.getMontant() + " €"
                + "\nDate: " + depense.getDate()
                + "\nDescription: " + depense.getDescriptionDepense()
                +"\n";
    }
}
