/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.mf.rpp.portefeuille.movement.split;

import gov.mf.rpp.portefeuille.PortefeuilleMovement;
import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Validator;
import java.util.HashSet;

/**
 *
 * @author khaled
 */
public class SplitRequestValidator implements ConstraintValidator<ValidSplitRequest, SplitRequest>{

    @Inject
    PortefeuilleMovement portefeuilleMovement;
    
    @Inject
    Validator validator;
    
    /**
     * check that request is not null //SA automatic
     * check that there's no null part //SA automatic
     * check that every part is valid //@ValidCreateRequest automatic
     * check there's no duplicate among parts(code, name) //manual
     * check there's no part with (name or code) that's taken //automatic
     */
    @Override
    public boolean isValid(SplitRequest request, ConstraintValidatorContext context) {
        var parentPortefeuilleOpt = portefeuilleMovement.portefeuilleByName(request.name());
        
        if(parentPortefeuilleOpt.isEmpty()){
            return false;
        }
        if(portefeuilleMovement.validSplitMain(request.name(), request.mainName(), request.mainCode())){
            return false;
        }
        var names = request.parts().stream()
                .map(CreateRequest::name)
                .toList();
        if(names.size() > new HashSet<>(names).size()){
            return false;
        }
        var codes = request.parts().stream()
                .map(CreateRequest::code)
                .toList();
        if(codes.size() > new HashSet<>(codes).size()){
            return false;
        }
        
        
        return true;
        
    }
    
}
