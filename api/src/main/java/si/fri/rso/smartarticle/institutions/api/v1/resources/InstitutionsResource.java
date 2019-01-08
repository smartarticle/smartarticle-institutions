package si.fri.rso.smartarticle.institutions.api.v1.resources;

import si.fri.rso.smartarticle.institutions.models.dtos.Article;
import si.fri.rso.smartarticle.institutions.models.dtos.Collection;
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


    @GET
    @Path("/info/{institutionId}")
    public Response getInstitutionData(@PathParam("institutionId") Integer institutionId) {

        Institution institution = institutionsBean.getInstitutionData(institutionId);

        if (institution == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(institution).build();
    }

    @GET
    @Path("/articles/{institutionId}")
    public Response getInstitutionArticles(@PathParam("institutionId") Integer institutionId) {

        List<Article> articles = institutionsBean.getArticles(institutionId);

        return Response.ok(articles).build();
    }

    @GET
    @Path("/collections/{institutionId}")
    public Response getInstitutionCollections(@PathParam("institutionId") Integer institutionId) {

        List<Collection> articles = institutionsBean.getCollections(institutionId);

        return Response.ok(articles).build();
    }
}
