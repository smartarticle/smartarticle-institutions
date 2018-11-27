package si.fri.rso.smartarticle.institutions.services.beans;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.smartarticle.institutions.models.entities.Institution;
import si.fri.rso.smartarticle.institutions.services.configuration.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.Optional;


@RequestScoped
public class InstitutionsBean {

    private Logger log = Logger.getLogger(InstitutionsBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private InstitutionsBean institutionsBean;

    @PostConstruct
    private void init() {}

    @Inject
    @DiscoverService("smartarticle-users")
    private Optional<String> baseUrl;

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
