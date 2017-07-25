package com.portal.parents.service.mapper;

import com.portal.parents.domain.*;
import com.portal.parents.service.dto.InstructorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Instructor and its DTO InstructorDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface InstructorMapper extends EntityMapper <InstructorDTO, Instructor> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    InstructorDTO toDto(Instructor instructor); 

    @Mapping(source = "userId", target = "user")
    Instructor toEntity(InstructorDTO instructorDTO); 
    default Instructor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Instructor instructor = new Instructor();
        instructor.setId(id);
        return instructor;
    }
}
