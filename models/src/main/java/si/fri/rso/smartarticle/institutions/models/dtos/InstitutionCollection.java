package si.fri.rso.smartarticle.institutions.models.dtos;

import si.fri.rso.smartarticle.institutions.models.entities.Institution;

import javax.persistence.Transient;
import java.util.List;

public class InstitutionCollection {
    @Transient
    private Institution institution;

    @Transient
    private List<Collection> collections;

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
}
