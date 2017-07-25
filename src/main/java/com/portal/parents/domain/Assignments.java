package com.portal.parents.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Assignments.
 */
@Entity
@Table(name = "assignments")
@Document(indexName = "assignments")
public class Assignments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "assignment_number", nullable = false)
    private Integer assignmentNumber;

    @ManyToOne
    private Classes classes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Assignments name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssignmentNumber() {
        return assignmentNumber;
    }

    public Assignments assignmentNumber(Integer assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
        return this;
    }

    public void setAssignmentNumber(Integer assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public Classes getClasses() {
        return classes;
    }

    public Assignments classes(Classes classes) {
        this.classes = classes;
        return this;
    }

    public void setClasses(Classes classes) {
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
        Assignments assignments = (Assignments) o;
        if (assignments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assignments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Assignments{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", assignmentNumber='" + getAssignmentNumber() + "'" +
            "}";
    }
}
