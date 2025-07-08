package gov.mf.rpp.portfeuille;
/**
 * PortfeuillesStatus represents a state of a portfeuille in the system
 * due to regulatory and technical reasons we only remove portfeuille logically
 * we want to maintain the historical relationships between parent and children portfeuilles
 * 
 */
public enum PortfeuilleStatus {
    /**
     * a portfeuille that is active and relevant
     */
    ACTIVE,
    /**
     * a portfeuille that is only kept for historical reasons
    */
    INACTIVE,
    /**
     * a portfeuille that is the result of splitting or merging but it's not relevant yet 
    */
    INCUBATING,
    /**
     * the portfeuille that is the source of splitting or merging but it's still relevant
     */
    EVOLVING;
}
