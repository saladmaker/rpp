package gov.mf.rpp.portfeuille.rename;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import gov.mf.rpp.portfeuille.PortfeuilleQueries;

public class RenameRequestValidator implements ConstraintValidator<ValidRenameRequest, RenameRequest> {

    @Inject
    PortfeuilleQueries portfeuilleRepo;

    @Override
    public boolean isValid(RenameRequest request, ConstraintValidatorContext context) {
        // TODO the old name must be s
        return null != request
                && portfeuilleRepo.validRenameTarget(request.oldName())
                && portfeuilleRepo.validNewName(request.newName());
    }

}
