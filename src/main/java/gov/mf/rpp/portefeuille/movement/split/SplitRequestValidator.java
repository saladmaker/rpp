package gov.mf.rpp.portefeuille.movement.split;

import gov.mf.rpp.portefeuille.PortefeuilleMovement;
import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;

/**
 *
 * @author khaled
 */
public class SplitRequestValidator implements ConstraintValidator<ValidSplitRequest, SplitRequest> {

    @Inject
    PortefeuilleMovement portefeuilleMovement;

    /**
     * check that request is not null //SA automatic check that there's no null
     * part //SA automatic check that every part is valid //@ValidCreateRequest
     * automatic check there's no duplicate among parts(code, name) //manual
     * check there's no part with (name or code) that's taken //automatic
     *
     * @param request
     */
    @Override
    public boolean isValid(SplitRequest request, ConstraintValidatorContext context) {
        if (null == request 
                || (null == request.name())
                || (null == request.mainCode())
                || (null == request.mainName())
                || (null == request.parts())) {
            return true;
        }
        var parentPortefeuilleOpt = portefeuilleMovement.portefeuilleByName(request.name());

        if (parentPortefeuilleOpt.isPresent()) {
            if (portefeuilleMovement.validSplitMain(request.name(), request.mainName(), request.mainCode())) {
                return false;
            }
            var names = request.parts().stream()
                    .map(CreateRequest::name)
                    .toList();
            if (names.size() > new HashSet<>(names).size()) {
                return false;
            }
            var codes = request.parts().stream()
                    .map(CreateRequest::code)
                    .toList();
            if (codes.size() == new HashSet<>(codes).size()) {
                return true;
            }
        }

        return false;

    }

}
