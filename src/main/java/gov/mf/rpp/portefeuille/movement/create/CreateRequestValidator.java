package gov.mf.rpp.portefeuille.movement.create;

import gov.mf.rpp.portefeuille.PortefeuilleMovement;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * @author khaled
 */
public class CreateRequestValidator implements ConstraintValidator<ValidCreateRequest, CreateRequest>{

    @Inject
    PortefeuilleMovement portefeuilleMovement;
    
    @Override
    public boolean isValid(CreateRequest requestNew, ConstraintValidatorContext context) {
        if(null == requestNew){
            return true;
        }
        return portefeuilleMovement.validNewPortefeuille(requestNew.name(), requestNew.code());
    }
    
}
