package test.domain;

import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class ComplexEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    @Embedded
    private EmbeddableEntity personInfo;

    @ElementCollection
    @CollectionTable(
            name="ComplexToSetOfStrings",
            joinColumns=@JoinColumn(name="complexEntityId")
    )
    @Column(name="stringValue")
    private Set<String> setOfStrings;

    /*
    TODO not supported now
    @ElementCollection(targetClass=java.lang.String.class, fetch = FetchType.EAGER)
    @SortNatural
    @CollectionTable(name = "ComplexToMapOfStrings", joinColumns = @JoinColumn(name = "complexEntityId"))
    @MapKeyColumn(name = "stringKey")
    @Column(name = "stringValue")
    private Map<String, String> mapOfStrings;
    */

    @OneToMany(mappedBy = "parent")
    private List<ChildEntity> childs;

    @OneToOne
    @JoinColumn(name="child_id")
    private ChildEntity sibling;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmbeddableEntity getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(EmbeddableEntity personInfo) {
        this.personInfo = personInfo;
    }

    public Set<String> getSetOfStrings() {
        return setOfStrings;
    }

    public void setSetOfStrings(Set<String> setOfStrings) {
        this.setOfStrings = setOfStrings;
    }

    public List<ChildEntity> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildEntity> childs) {
        this.childs = childs;
    }

    public ChildEntity getSibling() {
        return sibling;
    }

    public void setSibling(ChildEntity sibling) {
        this.sibling = sibling;
    }
}
