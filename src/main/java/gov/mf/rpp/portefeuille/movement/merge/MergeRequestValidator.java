package gov.mf.rpp.portefeuille.movement.merge;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * @author khaled
 */
public class MergeRequestValidator implements ConstraintValidator<ValidMergeRequest, MergeRequest>{

    /**
     * check if new name/code aren't taken(rule for merging portefeuilles)
     * check that there are no duplicates in target names
     * check if all target portefeuille names do in fact exist and in an active state
     */
    @Override
    public boolean isValid(MergeRequest value, ConstraintValidatorContext context) {
        return true;
    }
    
}
