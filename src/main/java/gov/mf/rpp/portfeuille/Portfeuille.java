package gov.mf.rpp.portfeuille;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a {@code Portefeuille}, which corresponds to a government ministry, institution, 
 * or administrative unit. This entity tracks its legal and structural state over time,
 * including renamings, mergers, and splits.
 * <p>
 * Portefeuilles are never physically deleted from the system. Instead, their lifecycle status 
 * is maintained using {@link PortfeuilleStatus}, enabling full historical tracking and traceability.
 * <p>
 * The {@link LegalSourceType} indicates how the Portefeuille originated (e.g., via creation, 
 * splitting, or merging).
 * <p>
 * Relationships to source portefeuilles are maintained to support accurate lineage 
 * (e.g., a portefeuille formed by merging two others).
 */
@Entity
public class Portfeuille {

    /**
     * Unique identifier for the portefeuille.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Official code assigned to the portefeuille. This code may remain stable or change 
     * across restructurings, depending on legal transformations.
     */
    @Column(name = "portfeuille_code", nullable = false)
    private String code;

    /**
     * Human-readable name of the portefeuille.
     */
    @Column(name = "portfeuille_name", nullable = false)
    private String name;

    /**
     * Current status of the portefeuille in the system (active, inactive, evolving, etc.).
     */
    @Column(name = "portfeuille_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PortfeuilleStatus status = PortfeuilleStatus.ACTIVE;

    /**
     * Describes the legal event that gave rise to this portefeuille (e.g., creation, renaming, etc.).
     */
    @Column(name = "portfeuille_orig_event", nullable = false)
    @Enumerated(EnumType.STRING)
    private LegalSourceType originatingEvent = LegalSourceType.CREATION;

    /**
     * A set of portefeuilles that are the legal origin of this one.
     * This is relevant for cases of merging or splitting, where multiple ancestors may exist.
     * 
     * <p><b>Example:</b> If portefeuille A and B were merged into portefeuille C,
     * A and B would be elements in C's {@code originatingPortfeuilles} set.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(name = "portfeuille_target_id"),
        inverseJoinColumns = @JoinColumn(name = "portfeuille_source_id")
    )
    private Set<Portfeuille> orginatingPortfeuilles = new HashSet<>();

    /**
     * Factory method to create a new {@code Portfeuille} with a name and code.
     * 
     * @param name the name of the new portefeuille
     * @param code the official code for the new portefeuille
     * @param status the status for portfeuille
     * @return a new {@code Portfeuille} instance
     */
    public static Portfeuille of(String name, String code, PortfeuilleStatus status) {
        var newPortfeuille = new Portfeuille();
        newPortfeuille.setName(name);
        newPortfeuille.setCode(code);
        newPortfeuille.setStatus(status);
        return newPortfeuille;
    }

    public Portfeuille() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public PortfeuilleStatus getStatus() {
        return status;
    }

    public void setStatus(PortfeuilleStatus portfeuilleStatus) {
        this.status = portfeuilleStatus;
    }

    public LegalSourceType getOriginatingEvent() {
        return originatingEvent;
    }

    public void setOriginatingEvent(LegalSourceType originatingEvent) {
        this.originatingEvent = originatingEvent;
    }

    /**
     * Adds a source portefeuille to this portefeuille's origin set.
     * Useful for tracking parent entities in splits or merges.
     * 
     * @param source the portefeuille that contributed to the formation of this one
     */
    public void addOriginatingSource(Portfeuille source) {
        orginatingPortfeuilles.add(source);
    }

    /**
     * Removes a source portefeuille from this portefeuille's origin set.
     * 
     * @param source the portefeuille to remove from the origin set
     */
    public void removeOriginatingSource(Portfeuille source) {
        orginatingPortfeuilles.remove(source);
    }
}
