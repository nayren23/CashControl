package modele;

import java.util.HashMap;
import java.util.Map;

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

    // Définition de la variable categories après la liste des énumérations
    public static Map<String, Integer> categories;
    static {
        categories = new HashMap<>();
        categories.put(Category.ALIMENTATION_RESTAURATION.getLabel(), 0);
        categories.put(Category.ACHAT_SHOPPING.getLabel(), 1);
        categories.put(Category.LOISIRS_SORTIES.getLabel(), 2);
        categories.put(Category.ABONNEMENT.getLabel(), 3);
        categories.put(Category.TRANSPORTS_AUTO.getLabel(), 4);
        categories.put(Category.DIVERS.getLabel(), 5);
        categories.put(Category.IMPOTS.getLabel(), 6);
        categories.put(Category.LOGEMENT.getLabel(), 7);
        categories.put(Category.SANTE.getLabel(), 8);
    }

}
