package gov.mf.rpp.portefeuille;

/**
 * Represents the type of legal source that affects a {@code Portefeuille} (ministry or institution).
 * This is used to track how a {@code Portefeuille} has been established or changed over time
 * through various legal actions.
 */
public enum LegalSourceType {

    /**
     * Indicates that the {@code Portefeuille} was created as a new entity.
     */
    CREATION,

    /**
     * Indicates that the {@code Portefeuille} was renamed from an existing one.
     */
    RENAMING,

    /**
     * Indicates that the {@code Portefeuille} was formed by splitting an existing one into multiple new ones.
     */
    SPLITTING,

    /**
     * Indicates that the {@code Portefeuille} was formed by merging two or more existing ones.
     */
    MERGING;
}

