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

@Entity
public class Portfeuille {

    public static final String NAMED_PORTFEUILLE_FOR_LATEST_LEGAL_CHANGE = "selectNamedPortfeuilleForLatestLegalChange";
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "portfeuille_code", nullable = false)
    private String code;

    @Column(name = "portfeuille_name", nullable = false)
    private String name;

    @Column(name = "portfeuille_status")
    private boolean active = true;


    @Column(name = "portfeuille_orig_event", nullable = false)
    @Enumerated(EnumType.STRING)
    private LegalSourceType originatingEvent = LegalSourceType.CREATION;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(name = "portfeuille_target_id"),
        inverseJoinColumns = @JoinColumn(name = "portfeuille_source_id")
     )
    private Set<Portfeuille> orginatingPortfeuilles = new HashSet<>();

    public static  Portfeuille of(String name, String code) {
        var AnewPortfeuillle = new Portfeuille();
        AnewPortfeuillle.setName(name);
        AnewPortfeuillle.setCode(code);
        return AnewPortfeuillle;
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
    

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LegalSourceType getOriginatingEvent() {
        return originatingEvent;
    }

    public void setOriginatingEvent(LegalSourceType originatingEvent) {
        this.originatingEvent = originatingEvent;
    }
    public void addOriginatingSource(Portfeuille source){
        orginatingPortfeuilles.add(source);
    }
    public void removeOriginatingSource(Portfeuille source){
        orginatingPortfeuilles.remove(source);
    }

}
