package utilitaires;

import java.util.HashMap;
import java.util.Map;

public enum Enum_Categories {
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

    Enum_Categories(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Cette fonction initialise une Map de catégories avec les différentes valeurs d'énumération d'Enum_Categories et leur index correspondant.
     * Les catégories sont utilisées pour classifier les dépenses et les revenus.
     * Les différentes catégories disponibles sont alimentation/restauration, achat/shopping, loisirs/sorties, abonnement, transports/auto, divers, impôts, logement, santé.
     */
    public static Map<String, Integer> categories;
    static {
        categories = new HashMap<>();
        categories.put(Enum_Categories.ALIMENTATION_RESTAURATION.getLabel(), 0);
        categories.put(Enum_Categories.ACHAT_SHOPPING.getLabel(), 1);
        categories.put(Enum_Categories.LOISIRS_SORTIES.getLabel(), 2);
        categories.put(Enum_Categories.ABONNEMENT.getLabel(), 3);
        categories.put(Enum_Categories.TRANSPORTS_AUTO.getLabel(), 4);
        categories.put(Enum_Categories.DIVERS.getLabel(), 5);
        categories.put(Enum_Categories.IMPOTS.getLabel(), 6);
        categories.put(Enum_Categories.LOGEMENT.getLabel(), 7);
        categories.put(Enum_Categories.SANTE.getLabel(), 8);
    }
}
