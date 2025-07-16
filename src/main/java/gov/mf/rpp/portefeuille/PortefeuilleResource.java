/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Portefeuille create(@Valid CreateRequest createRequest){
        return portefeuilleMovement.createPortefeuille(createRequest);
    }
    
    @GET
    public String getM(){
        return "dfs";
    }
    
}
