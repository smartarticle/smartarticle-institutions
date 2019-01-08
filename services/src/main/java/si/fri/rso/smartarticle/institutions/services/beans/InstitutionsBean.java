package si.fri.rso.smartarticle.institutions.services.beans;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.smartarticle.institutions.models.dtos.Account;
import si.fri.rso.smartarticle.institutions.models.dtos.Article;
import si.fri.rso.smartarticle.institutions.models.dtos.Collection;
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

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        //baseUrl = "http://localhost:8081"; // only for demonstration
    }

    @Inject
    @DiscoverService("smartarticle-articles")
    private Provider<Optional<String>> articleBaseProvider;

    public List<Institution> getInstitutions() {
        if (appProperties.isExternalServicesEnabled()) {
            TypedQuery<Institution> query = em.createNamedQuery("Institution.getAll", Institution.class);
            return query.getResultList();
        }
        return null;
    }

    public List<Institution> getInstitutionsFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Institution.class, queryParameters);
    }

    public Institution getInstitution(Integer institutionId) {

        Institution institution = em.find(Institution.class, institutionId);

        if (institution == null) {
            throw new NotFoundException();
        }
        List<Account> accounts = institutionsBean.getAccounts(institutionId);
        institution.setAccounts(accounts);
        return institution;
    }

    public List<Article> getArticles(Integer institutionId) {
        List<Account> accounts = institutionsBean.getAccounts(institutionId);
        List<Article> newList = new ArrayList<>();
        for (Account account: accounts) {
            newList.addAll(institutionsBean.getArticle(account.getId()));
        }
        return newList;
    }


    public List<Collection> getCollections(Integer institutionId) {
        List<Account> accounts = institutionsBean.getAccounts(institutionId);
        List<Collection> newList = new ArrayList<>();
        for (Account account: accounts) {
            newList.addAll(institutionsBean.getCollection(account.getId()));
        }
        return newList;
    }

    public Institution getInstitutionData(Integer institutionId) {

        Institution institution = em.find(Institution.class, institutionId);

        if (institution == null) {
            throw new NotFoundException();
        }
        return institution;
    }

    public Institution createInstitution(Institution institution) {

        try {
            beginTx();
            em.persist(institution);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
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
                throw new InternalServerErrorException(e);
            }
        }
        return null;

    }


    public Institution putInstitution(String institutionId, Institution institution) {

        Institution c = em.find(Institution.class, institutionId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            institution.setId(c.getId());
            institution = em.merge(institution);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return institution;
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
                return null;
                //throw new InternalServerErrorException(e);
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
                return null;
                // throw new InternalServerErrorException(e);
            }
        }
        return null;

    }

    public boolean deleteInstitution(String institutionId) {

        Institution institution = em.find(Institution.class, institutionId);

        if (institution != null) {
            try {
                beginTx();
                em.remove(institution);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
