package gov.mf.rpp.portefeuille;

/**
 * {@code PortefeuilleStatus} represents the lifecycle state of a {@code Portefeuille} 
 * (ministry or institution) within the system.
 * <p>
 * Due to regulatory and technical constraints, portefeuilles are never physically deleted 
 * from the system. Instead, they are logically deactivated to preserve historical integrity.
 * <p>
 * This design ensures that the system retains the full lineage and traceability of 
 * structural changes such as mergers, splits, and renamings across different time periods.
 */
public enum PortefeuilleStatus {

    /**
     * Indicates a currently active and operational portefeuille.
     * This portefeuille is in use and considered relevant in the current legal and organizational context.
     */
    ACTIVE,

    /**
     * Indicates a portefeuille that is no longer active but retained for historical reference.
     * It may have been decommissioned, merged, or renamed, but its record is preserved 
     * to maintain the audit trail and historical consistency.
     */
    INACTIVE,

    /**
     * Represents a newly created portefeuille that is the result of a legal transformation 
     * (e.g., merger or split) but is not yet active or fully integrated into the system.
     * It is in a transitional state and may become active in the future.
     */
    INCUBATING,

    /**
     * Represents a portefeuille undergoing structural change, such as being split or merged,
     * while remaining active and relevant during the transformation process.
     * It serves as a source or parent in the legal evolution of portefeuilles.
     */
    EVOLVING;
}
