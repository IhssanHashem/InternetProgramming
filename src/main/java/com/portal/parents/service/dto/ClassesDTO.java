package com.portal.parents.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Classes entity.
 */
public class ClassesDTO implements Serializable {

    private Long id;

    @NotNull
    private String className;

    private String classDescription;

    private Long instructorId;

    private String instructorEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassesDTO classesDTO = (ClassesDTO) o;
        if(classesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassesDTO{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", classDescription='" + getClassDescription() + "'" +
            "}";
    }
}
