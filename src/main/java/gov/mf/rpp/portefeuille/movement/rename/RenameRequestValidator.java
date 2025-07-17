package gov.mf.rpp.portefeuille.movement.rename;

import gov.mf.rpp.portefeuille.PortefeuilleRepo;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RenameRequestValidator implements ConstraintValidator<ValidRenameRequest, RenameRequest> {

    @Inject
    PortefeuilleRepo portefeuilleRepoe;

    @Override
    public boolean isValid(RenameRequest request, ConstraintValidatorContext context) {
        // TODO the old name must be s
        return portefeuilleRepoe.activePortefeuille(request.oldName())
                && portefeuilleRepoe.validRename(request.newName());
        
        
    }

}
