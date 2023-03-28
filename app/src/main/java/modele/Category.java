package modele;

public enum Category {
    ALIMENTATION_RESTAURATION("Alimentation & Restauration"),
    ACHAT_SHOPPING("Achat & Shopping"),
    LOISIRS_SORTIES("Loisirs & Sorties"),
    ABONNEMENT("Abonnement"),
    TRANSPORTS_AUTO("Transports & auto"),
    DIVERS("Divers"),
    IMPOTS("Impôts"),
    LOGEMENT("Logement"),
    SANTE("Santé");

    private String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
