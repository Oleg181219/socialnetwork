package JavaPRO.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "town")
public class Town {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

//  One person Many town ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @OneToMany(mappedBy = "town", orphanRemoval = true)
//    private Set<Person> person = new HashSet<>();
//
//    public Set<Person> getPerson() {
//        return person;
//    }
//
//    public boolean addPerson(Person person) {
//        person.setTownId(this);
//        return getPerson().add(person);
//    }
//
//    public void removePerson(Person person) {
//        getPerson().remove(person);
//    }
//
//    public void setPerson(Set<Person> person) {
//        this.person = person;
//    }

//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
