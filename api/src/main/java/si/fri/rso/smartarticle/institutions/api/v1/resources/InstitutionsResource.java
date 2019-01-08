package si.fri.rso.smartarticle.institutions.api.v1.resources;

import si.fri.rso.smartarticle.institutions.models.dtos.Article;
import si.fri.rso.smartarticle.institutions.models.dtos.Collection;
import si.fri.rso.smartarticle.institutions.models.dtos.InstituionArticle;
import si.fri.rso.smartarticle.institutions.models.dtos.InstitutionCollection;
import si.fri.rso.smartarticle.institutions.models.entities.Institution;
import si.fri.rso.smartarticle.institutions.services.beans.InstitutionsBean;
import si.fri.rso.smartarticle.institutions.services.configuration.AppProperties;

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

    @Inject
    private AppProperties appProperties;

    @GET
    public Response getInstitutions() {
        if (appProperties.isInstituteServicesEnabled()) {
            List<Institution> institutions = institutionsBean.getInstitutions();

            return Response.ok(institutions).build();
        }
        else{
            return Response.ok().build();
        }
    }

    @GET
    @Path("/filtered")
    public Response getInstitutionsFiltered() {
        if (appProperties.isInstituteServicesEnabled()) {
            List<Institution> institutions;

            institutions = institutionsBean.getInstitutionsFilter(uriInfo);

            return Response.status(Response.Status.OK).entity(institutions).build();
        }
        else{
            return Response.ok().build();
        }
    }

    @GET
    @Path("/{institutionId}")
    public Response getInstitution(@PathParam("institutionId") Integer institutionId) {
        if (appProperties.isInstituteServicesEnabled()) {
            Institution institution = institutionsBean.getInstitution(institutionId);

            if (institution == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.status(Response.Status.OK).entity(institution).build();
        }
        else{
            return Response.ok().build();
        }
    }


    @GET
    @Path("/info/{institutionId}")
    public Response getInstitutionData(@PathParam("institutionId") Integer institutionId) {
        if (appProperties.isInstituteServicesEnabled()) {
            Institution institution = institutionsBean.getInstitutionData(institutionId);

            if (institution == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.status(Response.Status.OK).entity(institution).build();
        }
        else{
            return Response.ok().build();
        }
    }

    @GET
    @Path("/articles/{institutionId}")
    public Response getInstitutionArticles(@PathParam("institutionId") Integer institutionId) {
        if (appProperties.isInstituteServicesEnabled()) {
            InstituionArticle articles = institutionsBean.getArticles(institutionId);

            return Response.ok(articles).build();
        }
        else{
            return Response.ok().build();
        }
    }

    @GET
    @Path("/collections/{institutionId}")
    public Response getInstitutionCollections(@PathParam("institutionId") Integer institutionId) {
        if (appProperties.isInstituteServicesEnabled()) {
            InstitutionCollection collections = institutionsBean.getCollections(institutionId);

            return Response.ok(collections).build();
        }
        else{
            return Response.ok().build();
        }
    }
}
