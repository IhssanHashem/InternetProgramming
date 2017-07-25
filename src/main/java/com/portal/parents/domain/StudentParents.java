package com.portal.parents.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StudentParents.
 */
@Entity
@Table(name = "student_parents")
@Document(indexName = "studentparents")
public class StudentParents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "student_parents_classes",
               joinColumns = @JoinColumn(name="student_parents_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="classes_id", referencedColumnName="id"))
    private Set<Classes> classes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public StudentParents email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public StudentParents firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public StudentParents lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public StudentParents user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Classes> getClasses() {
        return classes;
    }

    public StudentParents classes(Set<Classes> classes) {
        this.classes = classes;
        return this;
    }

    public StudentParents addClasses(Classes classes) {
        this.classes.add(classes);
        classes.getStudentParents().add(this);
        return this;
    }

    public StudentParents removeClasses(Classes classes) {
        this.classes.remove(classes);
        classes.getStudentParents().remove(this);
        return this;
    }

    public void setClasses(Set<Classes> classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentParents studentParents = (StudentParents) o;
        if (studentParents.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), studentParents.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StudentParents{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
