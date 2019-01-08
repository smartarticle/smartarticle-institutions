package si.fri.rso.smartarticle.institutions.services.beans;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.smartarticle.institutions.models.dtos.*;
import si.fri.rso.smartarticle.institutions.models.entities.Institution;
import si.fri.rso.smartarticle.institutions.services.configuration.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;


@RequestScoped
public class InstitutionsBean {

    private Logger log = Logger.getLogger(InstitutionsBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private InstitutionsBean institutionsBean;

    private Client httpClient;

    @Inject
    @DiscoverService("smartarticle-accounts")
    private Provider<Optional<String>> accountBaseProvider;

    @Inject
    @DiscoverService("smartarticle-collections")
    private Provider<Optional<String>> collectionBaseProvider;

    @Inject
    @DiscoverService("smartarticle-articles")
    private Provider<Optional<String>> articleBaseProvider;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<Institution> getInstitutions() {
        TypedQuery<Institution> query = em.createNamedQuery("Institution.getAll", Institution.class);
        return query.getResultList();
    }

    public List<Institution> getInstitutionsFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Institution.class, queryParameters);
    }

    public Institution getInstitution(Integer institutionId) {

        Institution institution = em.find(Institution.class, institutionId);

        if (institution == null) {
            appProperties.setHealthy(false);
            throw new NotFoundException();
        }
        if (appProperties.isInstituteAccountServicesEnabled()){
            List<Account> accounts = institutionsBean.getAccounts(institutionId);
            institution.setAccounts(accounts);
        }
        return institution;
    }

    public InstituionArticle getArticles(Integer institutionId) {
        if (appProperties.isInstituteAccountServicesEnabled() && appProperties.isInstituteArticleServicesEnabled()) {
            Institution institution = institutionsBean.getInstitutionData(institutionId);
            List<Account> accounts = institutionsBean.getAccounts(institutionId);
            List<Article> newList = new ArrayList<>();
            for (Account account : accounts) {
                newList.addAll(institutionsBean.getArticle(account.getId()));
            }
            InstituionArticle instituionArticle = new InstituionArticle();
            instituionArticle.setInstitution(institution);
            instituionArticle.setArticles(newList);
            return instituionArticle;
        }
        return null;
    }


    public InstitutionCollection getCollections(Integer institutionId) {
        if (appProperties.isInstituteAccountServicesEnabled() && appProperties.isInstituteCollectionServicesEnabled()) {
            Institution institution = institutionsBean.getInstitutionData(institutionId);
            List<Account> accounts = institutionsBean.getAccounts(institutionId);
            List<Collection> newList = new ArrayList<>();
            for (Account account : accounts) {
                newList.addAll(institutionsBean.getCollection(account.getId()));
            }
            InstitutionCollection instituionCollection = new InstitutionCollection();
            instituionCollection.setInstitution(institution);
            instituionCollection.setCollections(newList);
            return instituionCollection;
        }
        return null;
    }

    public Institution getInstitutionData(Integer institutionId) {

        Institution institution = em.find(Institution.class, institutionId);

        if (institution == null) {
            appProperties.setHealthy(false);
            throw new NotFoundException();
        }
        return institution;
    }

    public List<Account> getAccounts(Integer institutionId) {
        Optional<String> baseUrl = accountBaseProvider.get();
        if (baseUrl.isPresent()) {
            try {
                String link = baseUrl.get();
                return httpClient
                        .target(link + "/v1/accounts?where=instituteId:EQ:" + institutionId)
                        .request().get(new GenericType<List<Account>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                appProperties.setHealthy(false);
                throw new InternalServerErrorException(e);
            }
        }
        return null;

    }


    public List<Article> getArticle(Integer accountId) {
        Optional<String> baseUrl = articleBaseProvider.get();
        if (baseUrl.isPresent()) {
            try {
                String link = baseUrl.get();
                return httpClient
                        .target(link + "/v1/articles?where=accountId:EQ:" + accountId)
                        .request().get(new GenericType<List<Article>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                appProperties.setHealthy(false);
                throw new InternalServerErrorException(e);
            }
        }
        return null;

    }

    public List<Collection> getCollection(Integer accountId) {
        Optional<String> baseUrl = collectionBaseProvider.get();
        if (baseUrl.isPresent()) {
            try {
                String link = baseUrl.get();
                return httpClient
                        .target(link + "/v1/collections?where=accountId:EQ:" + accountId)
                        .request().get(new GenericType<List<Collection>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                appProperties.setHealthy(false);
                throw new InternalServerErrorException(e);
            }
        }
        return null;

    }
}
