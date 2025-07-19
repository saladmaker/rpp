package gov.mf.rpp.portefeuille.movement.merge;

import gov.mf.rpp.portefeuille.PortefeuilleRepo;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 *
 * @author khaled
 */
public class MergeRequestValidator implements ConstraintValidator<ValidMergeRequest, MergeRequest> {

    @Inject
    PortefeuilleRepo repo;

    @Override
    public boolean isValid(MergeRequest request, ConstraintValidatorContext context) {
        //only unique names
        var uniqueTargetNames = Set.copyOf(request.targetNames());
        var uniqueTargetNamesSize = uniqueTargetNames.size();

        //test that all target portefeuille do in fact exist and are active 
        //by virtue if they have equal sizes than they contain the same names
        if (uniqueTargetNamesSize == repo.activePortefeuilleNamesIn(uniqueTargetNames).size()) {

            //test that name/code are not taken by other portefeuille that's out of the scope of
            //this merging operation
            return repo.validMergingTarget(request.name(), request.code(), uniqueTargetNames);
        }
        return false;
    }

}
