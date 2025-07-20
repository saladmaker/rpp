package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.create.ValidCreateRequest;
import gov.mf.rpp.portefeuille.movement.rename.RenameRequest;
import gov.mf.rpp.portefeuille.movement.rename.ValidRenameRequest;
import gov.mf.rpp.portefeuille.movement.split.Part;
import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import gov.mf.rpp.portefeuille.movement.split.ValidSplitRequest;
import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

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
    public Portefeuille createPortefeuille(@ValidCreateRequest CreateRequest request) {

        var aNewPortefeuille = Portefeuille.of(request.name(), request.code(), request.status());
        return repo.createPortfeuille(aNewPortefeuille);

    }

    public CreateRequest renamePortefeuille(@ValidRenameRequest RenameRequest request) {
        var portefeuille = portefeuilleByName(request.oldName())
                .orElseThrow();

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
        return new CreateRequest(request.newName(), newPortefeuille.getCode(), PortefeuilleStatus.ACTIVE);

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
        var mainRequest = new Part(request.mainName(), request.mainCode());

        var main = ofSplit(mainRequest, parent);
        var parts = request.parts().stream()
                .map(p -> ofSplit(p, parent))
                .toList();
        return parts;

    }

    public Optional<Portefeuille> portefeuilleByName(String name) {
        return repo.portefeuilleByName(name);
    }

    public Optional<Portefeuille> portefeuilleByNameWithOrigins(@NotBlank String name) {

        return repo.portefeuilleByNameWithOrigins(name);
    }

    public List<Portefeuille> relevantPortefeuilles(EnumSet<PortefeuilleStatus> statuses) {
        return repo.relevantPortefeuilles(statuses);
    }

    public List<String> relevantPortefeuilleNames(EnumSet<PortefeuilleStatus> statuses){
        return repo.relevantPortefeuilleNames(statuses);
    }
    private Portefeuille ofSplit(Part request, Portefeuille parent) {
        Portefeuille portefeuille = Portefeuille.ofSplit(
                request.name(),
                request.code()
        );
        portefeuille.addOriginatingSource(parent);
        return repo.createPortfeuille(portefeuille);

    }
}
