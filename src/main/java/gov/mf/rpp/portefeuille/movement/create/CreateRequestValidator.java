package gov.mf.rpp.portefeuille.movement.create;

import gov.mf.rpp.portefeuille.PortefeuilleMovement;
import gov.mf.rpp.portefeuille.PortefeuilleRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * @author khaled
 */
@ApplicationScoped
public class CreateRequestValidator implements ConstraintValidator<ValidCreateRequest, CreateRequest>{

    @Inject
    PortefeuilleRepo portefeuilleRepo;
    
    @Override
    public boolean isValid(CreateRequest requestNew, ConstraintValidatorContext context) {
        return portefeuilleRepo.validNewPortefeuille(requestNew.name(), requestNew.code());
    }
    
}
