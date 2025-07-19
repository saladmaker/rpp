package gov.mf.rpp.portefeuille;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.EnumSet;
import java.util.List;

/**
 * @author khaled
 */
@Path("portefeuilles")
public class Portefeuilles {

    @Inject
    PortefeuilleMovement portefeuilleMovement;

    @Path("names")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> relevantNames(
            @QueryParam("status")
            @NotNull(message = "statuses must not be null")
            @NotEmpty(message = "statuses must not be empty")
            List<@NotNull(message = "status must not be null") PortefeuilleStatus> statuses) {
        return portefeuilleMovement.relevantPortefeuilleNames(EnumSet.copyOf(statuses));
    }


}
