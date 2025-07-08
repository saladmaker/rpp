package gov.mf.rpp.portfeuille;
/**
 * legal source type for a given portfeuille
 * {@link #CREATION} a Portfeuille that has no parent
 * {@link #RENAMING} a Portfeuille that has a parent {@link gov.mf.rpp.portfeuille.Portfeuille} Portfeuille with a different name
 * {@link #SPLITTING} a Portfeuille that's the result of splitting a parent {@link gov.mf.rpp.portfeuille.Portfeuille} portfeuille
 * {@link #MERGING} a Portfeuille that's the result of merging {@link gov.mf.rpp.portfeuille.Portfeuille} portfeuilles
 */
public enum LegalSourceType {
    CREATION,
    RENAMING,
    SPLITTING,
    MERGING;
}
