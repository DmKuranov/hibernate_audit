package test.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ChildEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private ComplexEntity parent;

    @OneToOne(mappedBy = "sibling")
    private ComplexEntity sibling;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ComplexEntity getParent() {
        return parent;
    }

    public void setParent(ComplexEntity parent) {
        this.parent = parent;
    }

    public ComplexEntity getSibling() {
        return sibling;
    }

    public void setSibling(ComplexEntity sibling) {
        this.sibling = sibling;
    }
}
