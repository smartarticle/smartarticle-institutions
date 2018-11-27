package si.fri.rso.smartarticle.institutions.api.v1.resources;

import si.fri.rso.smartarticle.institutions.models.entities.Institution;
import si.fri.rso.smartarticle.institutions.services.beans.InstitutionsBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@RequestScoped
@Path("/institutions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InstitutionsResource {

    @Inject
    private InstitutionsBean institutionsBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getInstitutions() {

        List<Institution> institutions = institutionsBean.getInstitutions();

        return Response.ok(institutions).build();
    }

    @GET
    @Path("/filtered")
    public Response getInstitutionsFiltered() {

        List<Institution> institutions;

        institutions = institutionsBean.getInstitutionsFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(institutions).build();
    }

    @GET
    @Path("/{institutionId}")
    public Response getInstitution(@PathParam("institutionId") Integer institutionId) {

        Institution institution = institutionsBean.getInstitution(institutionId);

        if (institution == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(institution).build();
    }
}
