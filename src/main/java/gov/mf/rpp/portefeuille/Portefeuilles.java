package gov.mf.rpp.portefeuille;

import gov.mf.rpp.portefeuille.movement.create.CreateRequest;
import gov.mf.rpp.portefeuille.movement.rename.RenameRequest;
import gov.mf.rpp.portefeuille.movement.split.SplitRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.EnumSet;
import java.util.List;

/**
 * @author khaled
 */
@Path("portefeuilles")
public class Portefeuilles {

    @Inject
    PortefeuilleMovement portefeuilleMovement;

    @Path("create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @Valid
            @NotNull(message = "create request must not be null") CreateRequest request) {
        portefeuilleMovement.createPortefeuille(request);
        //TODO! return create 201
        return Response.ok().build();
    }

    @Path("names")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> relevantNames(
            @QueryParam("status")
            @DefaultValue("ACTIVE") List<PortefeuilleStatus> statuses) {
        return portefeuilleMovement.relevantPortefeuilleNames(EnumSet.copyOf(statuses));
    }

    @Path("rename")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CreateRequest rename(
            @Valid
            @NotNull(message = "rename request must not be null") RenameRequest request) {
        return portefeuilleMovement.renamePortefeuille(request);
    }
//    @Path("merge")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<CreateRequest> split(@Valid
//            @NotNull(message = "split request must not be null") SplitRequest request) {
//        return portefeuilleMovement.split(request);
//    }
}
