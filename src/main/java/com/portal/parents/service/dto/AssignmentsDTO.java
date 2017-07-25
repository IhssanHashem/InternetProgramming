package com.portal.parents.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Assignments entity.
 */
public class AssignmentsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer assignmentNumber;

    private Long classesId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(Integer assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public Long getClassesId() {
        return classesId;
    }

    public void setClassesId(Long classesId) {
        this.classesId = classesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssignmentsDTO assignmentsDTO = (AssignmentsDTO) o;
        if(assignmentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assignmentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AssignmentsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", assignmentNumber='" + getAssignmentNumber() + "'" +
            "}";
    }
}
