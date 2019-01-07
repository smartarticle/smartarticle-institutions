package si.fri.rso.smartarticle.institutions.models.entities;

import si.fri.rso.smartarticle.institutions.models.dtos.Account;

import javax.persistence.*;

import java.util.List;

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

    @Transient
    private List<Account> accounts;


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

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}