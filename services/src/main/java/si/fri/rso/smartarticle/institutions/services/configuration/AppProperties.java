package si.fri.rso.smartarticle.institutions.services.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("app-properties")
public class AppProperties {

    @ConfigValue(value = "institute-services.enabled", watch = true)
    private boolean instituteServicesEnabled;

    @ConfigValue(value = "institute-account-services.enabled", watch = true)
    private boolean instituteAccountServicesEnabled;

    @ConfigValue(value = "institute-article-services.enabled", watch = true)
    private boolean instituteArticleServicesEnabled;

    @ConfigValue(value = "institute-collection-services.enabled", watch = true)
    private boolean instituteCollectionServicesEnabled;

    @ConfigValue(watch = true)
    private boolean healthy;

    public boolean isInstituteServicesEnabled() {
        return instituteServicesEnabled;
    }

    public void setInstituteServicesEnabled(boolean instituteServicesEnabled) {
        this.instituteServicesEnabled = instituteServicesEnabled;
    }

    public boolean isInstituteAccountServicesEnabled() {
        return instituteAccountServicesEnabled;
    }

    public void setInstituteAccountServicesEnabled(boolean instituteAccountServicesEnabled) {
        this.instituteAccountServicesEnabled = instituteAccountServicesEnabled;
    }

    public boolean isInstituteArticleServicesEnabled() {
        return instituteArticleServicesEnabled;
    }

    public void setInstituteArticleServicesEnabled(boolean instituteArticleServicesEnabled) {
        this.instituteArticleServicesEnabled = instituteArticleServicesEnabled;
    }

    public boolean isInstituteCollectionServicesEnabled() {
        return instituteCollectionServicesEnabled;
    }

    public void setInstituteCollectionServicesEnabled(boolean instituteCollectionServicesEnabled) {
        this.instituteCollectionServicesEnabled = instituteCollectionServicesEnabled;
    }
}
