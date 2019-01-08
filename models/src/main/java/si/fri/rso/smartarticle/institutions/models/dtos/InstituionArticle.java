package si.fri.rso.smartarticle.institutions.models.dtos;

import si.fri.rso.smartarticle.institutions.models.entities.Institution;

import javax.persistence.Transient;
import java.util.List;

public class InstituionArticle {
    @Transient
    private Institution institution;

    @Transient
    private List<Article> articles;

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
