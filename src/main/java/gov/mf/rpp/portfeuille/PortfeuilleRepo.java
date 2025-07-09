package gov.mf.rpp.portfeuille;

import gov.mf.rpp.portfeuille.rename.RenameRequest;
import gov.mf.rpp.portfeuille.rename.ValidRenameRequest;
import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

/**
 *
 * @author khaled
 */
@ApplicationScoped
public class PortfeuilleRepo {

    @Inject
    PortfeuilleQueries portfeuilleRepo;


    public Portfeuille renamePortfeuille(@Valid @ValidRenameRequest RenameRequest renameRequest) {
        return portfeuilleRepo.renamePortfeuille(renameRequest);
    }

    // TODO
    // validate that name exist Jakrata Custom Validation
    // validate that all names and code aren't already taken Jakarta Custom
    // Validation
    // set active to false for old portfeuille
    // for each portfeuilleRequest create new portfeuille
    // public default void split(@Valid @NotNull PortfeuilleSplitRequest portfeuilleSplitRequest) {
    // }
}
