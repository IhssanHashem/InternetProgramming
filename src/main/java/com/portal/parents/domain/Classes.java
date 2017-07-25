package com.portal.parents.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Classes.
 */
@Entity
@Table(name = "classes")
@Document(indexName = "classes")
public class Classes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_description")
    private String classDescription;

    @ManyToOne
    private Instructor instructor;

    @ManyToMany(mappedBy = "classes")
    @JsonIgnore
    private Set<StudentParents> studentParents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public Classes className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public Classes classDescription(String classDescription) {
        this.classDescription = classDescription;
        return this;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Classes instructor(Instructor instructor) {
        this.instructor = instructor;
        return this;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Set<StudentParents> getStudentParents() {
        return studentParents;
    }

    public Classes studentParents(Set<StudentParents> studentParents) {
        this.studentParents = studentParents;
        return this;
    }

    public Classes addStudentParent(StudentParents studentParents) {
        this.studentParents.add(studentParents);
        studentParents.getClasses().add(this);
        return this;
    }

    public Classes removeStudentParent(StudentParents studentParents) {
        this.studentParents.remove(studentParents);
        studentParents.getClasses().remove(this);
        return this;
    }

    public void setStudentParents(Set<StudentParents> studentParents) {
        this.studentParents = studentParents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Classes classes = (Classes) o;
        if (classes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Classes{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", classDescription='" + getClassDescription() + "'" +
            "}";
    }
}
