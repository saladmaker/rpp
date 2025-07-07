package gov.mf.rpp.portfeuille.rename;

import gov.mf.rpp.portfeuille.PortfeuilleRepo;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RenameRequestValidator implements ConstraintValidator<ValidRenameRequest, RenameRequest> {

    @Inject
    PortfeuilleRepo portfeuilleRepo;

    @Override
    public boolean isValid(RenameRequest request, ConstraintValidatorContext context) {
        // TODO the old name must be s
        return portfeuilleRepo.validPortfeuilleName(request.oldName()) &&
                portfeuilleRepo.validPortfeuilleNewName(request.newName());
    }

}
