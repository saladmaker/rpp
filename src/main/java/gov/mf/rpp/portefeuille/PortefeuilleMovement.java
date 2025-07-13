package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.create.ValidCreateRequest;
import gov.mf.rpp.portefeuille.movement.rename.RenameRequest;
import gov.mf.rpp.portefeuille.movement.rename.ValidRenameRequest;
import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import gov.mf.rpp.portefeuille.movement.split.ValidSplitRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author khaled
 */
@ApplicationScoped
@Transactional
public class PortefeuilleMovement {

    private final PortefeuilleRepo repo;

    @Inject
    public PortefeuilleMovement(PortefeuilleRepo repo) {
        this.repo = repo;
    }

    public boolean validNewPortefeuille(String name, String code) {
        return repo.validNewPortefeuille(name, code);
    }

    /**
     * Creates and persists a new {@link Portefeuille} entity with the provided
     * name and code. The portefeuille is initialized with
     * {@link PortefeuilleStatus#ACTIVE} status and inserted directly using a
     * {@link org.hibernate.StatelessSession}.
     * <p>
     * This method is typically used when registering a brand-new portefeuille
     * that does not yet exist in the system and should be considered active
     * immediately.
     *
     * @param request the creation request containing the portefeuille's name
     * and code
     * @return the newly created {@code Portefeuille} entity
     */
    public Portefeuille createPortefeuille(@Valid @ValidCreateRequest CreateRequest request) {
        var aNewPortefeuille = Portefeuille.of(request.name(), request.code(), request.status());
        return repo.createPortfeuille(aNewPortefeuille);

    }

    public Portefeuille renamePortefeuille(@Valid @ValidRenameRequest RenameRequest request) {
        var portefeuille = portefeuilleByName(request.oldName())
                .orElseThrow(() -> new IllegalArgumentException("Original portefeuille not found"));

        portefeuille.setStatus(PortefeuilleStatus.INACTIVE);
        repo.update(portefeuille);

        var newPortefeuille = Portefeuille.of(
                request.newName(),
                portefeuille.getCode(),
                PortefeuilleStatus.ACTIVE
        );

        newPortefeuille.setOriginatingEvent(LegalSourceType.RENAMING);
        newPortefeuille.addOriginatingSource(portefeuille);
        repo.createPortfeuille(newPortefeuille);
        return newPortefeuille;

    }

    /**
     * Performs a split operation on an existing {@link Portefeuille},
     * deactivating it and creating two or more new portefeuilles derived from
     * it.
     * <p>
     * Each new portefeuille is initialized using the data provided in the
     * {@link SplitRequest} and linked to the original portefeuille via the
     * {@code originatingPortefeuilles} relationship. The original
     * portefeuille's status is updated to {@link PortefeuilleStatus#EVOLVING}.
     * <p>
     * This method is transactional and ensures referential integrity during the
     * transformation.
     *
     * @param request the split request containing the name of the original
     * portefeuille and a list of new portefeuille creation requests
     * @return a list of newly created portefeuilles
     * @throws IllegalArgumentException if the original portefeuille is not
     * found or inactive
     */
    public List<Portefeuille> split(@ValidSplitRequest SplitRequest request) {
        Portefeuille parent = repo.portefeuilleByName(request.name()).orElseThrow();
        parent.setStatus(PortefeuilleStatus.EVOLVING);
        repo.update(parent);
        var mainRequest = new CreateRequest(request.mainName(), request.mainCode(), PortefeuilleStatus.ACTIVE);
        
        var main = ofSplit(mainRequest, parent);
        var parts = request.parts().stream()
                .map(p -> ofSplit(p, parent))
                .toList();
        return parts;

    }
    
    public boolean validSplitMain(String parentName, String mainName, String mainCode){
        return repo.validSplitMainPortefeuille(parentName, mainName, mainCode);
    }

    public Optional<Portefeuille> portefeuilleByName(@NotBlank String name) {
        return repo.portefeuilleByName(name);
    }

    public Optional<Portefeuille> portefeuilleByNameWithOrigins(@NotBlank String name) {

        return repo.portefeuilleByNameWithOrigins(name);
    }

    public List<Portefeuille> relevantPortefeuilles() {
        return repo.relevantPortefeuilles();
    }

    private Portefeuille ofSplit(CreateRequest request, Portefeuille parent) {
        Portefeuille portfeuille = Portefeuille.of(
                        request.name(),
                        request.code(),
                        PortefeuilleStatus.INCUBATING
        );
        
        portfeuille.setOriginatingEvent(LegalSourceType.SPLITTING);
        portfeuille.addOriginatingSource(parent);
        return repo.createPortfeuille(portfeuille);

    }
}
