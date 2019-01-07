package si.fri.rso.smartarticle.institutions.models.dtos;

import java.time.Instant;

public class Account {
    private Integer id;
    private String firstName;
    private String lastName;
    private Instant dateOfBirth;
    private String title;
    private String instituteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getInstituteId() { return instituteId; }

    public void setInstituteId(String instituteId) { this.instituteId = instituteId; }
}
