package gov.mf.rpp.portefeuille.movement.split;

import gov.mf.rpp.portefeuille.PortefeuilleRepo;
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
    PortefeuilleRepo repo;

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

        var parentPortefeuilleOpt = repo.portefeuilleByName(request.name());

        //valid target
        if (parentPortefeuilleOpt.isPresent()) {
            //valid main part
            if (repo.validSplitMainPart(request.name(), request.mainName(), request.mainCode())) {

                var names = request.parts().stream()
                        .map(Part::name)
                        .toList();
                var uniqueName = new HashSet<>(names);
                
                if (names.size() == uniqueName.size()) {
                    var codes = request.parts().stream()
                            .map(Part::code)
                            .toList();
                    var uniqueCodes = new HashSet<>(codes);
                    
                    if (codes.size() == uniqueCodes.size()) {
                        return repo.validParts(uniqueName, uniqueCodes);
                    }
                }
            }
        }

        return false;

    }

}
