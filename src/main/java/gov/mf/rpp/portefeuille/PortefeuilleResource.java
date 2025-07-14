/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import java.util.List;

/**
 *
 * @author HP
 */
@Path("/portefeuilles")
public class PortefeuilleResource {
    
    private PortefeuilleMovement portefeuilleMovement;
    
    @Inject
    public PortefeuilleResource(PortefeuilleMovement portefeuilleMovement){
        this.portefeuilleMovement = portefeuilleMovement;
    }
    public List<Portefeuille> split(@Valid SplitRequest request){
        return portefeuilleMovement.split(request);
    }
    
}
