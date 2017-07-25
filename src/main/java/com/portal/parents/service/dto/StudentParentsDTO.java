package com.portal.parents.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StudentParents entity.
 */
public class StudentParentsDTO implements Serializable {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private Long userId;

    private String userLogin;

    private Set<ClassesDTO> classes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Set<ClassesDTO> getClasses() {
        return classes;
    }

    public void setClasses(Set<ClassesDTO> classes) {
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

        StudentParentsDTO studentParentsDTO = (StudentParentsDTO) o;
        if(studentParentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), studentParentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StudentParentsDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
