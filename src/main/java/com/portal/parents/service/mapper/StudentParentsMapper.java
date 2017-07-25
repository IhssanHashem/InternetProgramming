package com.portal.parents.service.mapper;

import com.portal.parents.domain.*;
import com.portal.parents.service.dto.StudentParentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StudentParents and its DTO StudentParentsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ClassesMapper.class, })
public interface StudentParentsMapper extends EntityMapper <StudentParentsDTO, StudentParents> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    StudentParentsDTO toDto(StudentParents studentParents); 

    @Mapping(source = "userId", target = "user")
    StudentParents toEntity(StudentParentsDTO studentParentsDTO); 
    default StudentParents fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentParents studentParents = new StudentParents();
        studentParents.setId(id);
        return studentParents;
    }
}
