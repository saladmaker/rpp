package gov.mf.rpp.portefeuille;

import java.util.HashSet;
import java.util.Objects;
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
import jakarta.persistence.ManyToMany;

/**
 * Represents a {@code Portefeuille}, which corresponds to a government ministry, institution, 
 * or administrative unit. This entity captures its legal and structural evolution over time,
 * including renamings, mergers, and splits.
 * 
 * <p>Portefeuilles are never physically deleted from the system. Instead, their lifecycle status 
 * is maintained using {@link PortefeuilleStatus}, allowing full historical tracking and traceability.</p>
 * 
 * <p>The {@link LegalSourceType} describes the legal event that led to the creation or transformation 
 * of this portefeuille (e.g., through creation, merging, or splitting).</p>
 * 
 * <p>Relationships to origin portefeuilles are maintained to preserve lineage and trace the history 
 * of structural transformations (e.g., when a portefeuille is formed by merging others).</p>
 */
@Entity
public class Portefeuille {

    /**
     * Unique identifier for the portefeuille.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Official code assigned to the portefeuille. This code may remain stable or change 
     * during restructurings, depending on the nature of the legal transformation.
     */
    @Column(name = "portefeuille_code", nullable = false)
    private String code;

    /**
     * Human-readable name of the portefeuille.
     */
    @Column(name = "portefeuille_name", nullable = false)
    private String name;

    /**
     * Current status of the portefeuille in the system (e.g., active, inactive, evolving).
     */
    @Column(name = "portefeuille_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PortefeuilleStatus status = PortefeuilleStatus.ACTIVE;

    /**
     * Legal event that caused the creation or transformation of this portefeuille 
     * (e.g., creation, renaming, splitting).
     */
    @Column(name = "portefeuille_orig_event", nullable = false)
    @Enumerated(EnumType.STRING)
    private LegalSourceType originatingEvent = LegalSourceType.CREATION;

    /**
     * The set of portefeuilles that are the legal origin of this portefeuille.
     * This is relevant in cases of merging or splitting, where multiple parent portefeuilles may exist.
     * 
     * <p><b>Example:</b> If portefeuilles A and B were merged to form portefeuille C,
     * then A and B would be included in C’s {@code originatingPortefeuilles} set.</p>
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(name = "portefeuille_target_id"),
        inverseJoinColumns = @JoinColumn(name = "portefeuille_source_id")
    )
    private Set<Portefeuille> originatingPortefeuilles = new HashSet<>();

    /**
     * Factory method to create a new {@code Portefeuille} with the specified name, code, and status.
     *
     * @param name   the name of the new portefeuille
     * @param code   the official code of the new portefeuille
     * @param status the initial status of the portefeuille
     * @return a new {@code Portefeuille} instance
     */
    public static Portefeuille of(String name, String code, PortefeuilleStatus status) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(status, "status must not be null");

        var newPortefeuille = new Portefeuille();
        newPortefeuille.setName(name);
        newPortefeuille.setCode(code);
        newPortefeuille.setStatus(status);
        return newPortefeuille;
    }

    public Portefeuille() {}

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

    public PortefeuilleStatus getStatus() {
        return status;
    }

    public void setStatus(PortefeuilleStatus status) {
        this.status = status;
    }

    public LegalSourceType getOriginatingEvent() {
        return originatingEvent;
    }

    public void setOriginatingEvent(LegalSourceType originatingEvent) {
        this.originatingEvent = originatingEvent;
    }

    public Set<Portefeuille> getOriginatingPortefeuilles() {
        return originatingPortefeuilles;
    }

    /**
     * Adds a source portefeuille to this portefeuille’s origin set.
     * Useful for recording parent entities in merge or split events.
     *
     * @param source the portefeuille that contributed to the formation of this one
     */
    public void addOriginatingSource(Portefeuille source) {
        originatingPortefeuilles.add(source);
    }

    /**
     * Removes a source portefeuille from this portefeuille’s origin set.
     *
     * @param source the portefeuille to remove
     */
    public void removeOriginatingSource(Portefeuille source) {
        originatingPortefeuilles.remove(source);
    }
}
