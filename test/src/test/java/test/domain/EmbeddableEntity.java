package test.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmbeddableEntity {
    private static final long serialVersionUID = 1L;

    @Column
    private String firstName;

    @Column
    private String lastName;

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
}
