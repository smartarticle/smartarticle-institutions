package si.fri.rso.smartarticle.institutions.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "institution")
@NamedQueries(value =
        {
                @NamedQuery(name = "Institution.getAll", query = "SELECT c FROM institution c")
        })
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String institute;

    private String address;

    private String post;

    private String country;

    private String email;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstitute() { return institute; }

    public void setInstitute(String institute) { this.institute = institute; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String title) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(Instant dateOfBirth) {
        this.email = email;
    }

}