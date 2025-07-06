package gov.mf.rpp.portfeuille.split;

import gov.mf.rpp.portfeuille.PortfeuilleRepo;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PortfeuilleSplitRequestValidator implements ConstraintValidator<PortfeuilleNameExistes, PortfeuilleSplitRequest> {

    @Inject
    PortfeuilleRepo portfeuilleRepo;

    @Override
    public boolean isValid(PortfeuilleSplitRequest request, ConstraintValidatorContext context) {
        var portfeuille = portfeuilleRepo.portfeuilleByName(request.portfeuilleName()).orElseThrow(IllegalStateException::new);
        return true;
    }

}
