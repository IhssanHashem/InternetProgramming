package com.portal.parents.service.mapper;

import com.portal.parents.domain.*;
import com.portal.parents.service.dto.ClassesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Classes and its DTO ClassesDTO.
 */
@Mapper(componentModel = "spring", uses = {InstructorMapper.class, })
public interface ClassesMapper extends EntityMapper <ClassesDTO, Classes> {

    @Mapping(source = "instructor.id", target = "instructorId")
    @Mapping(source = "instructor.email", target = "instructorEmail")
    ClassesDTO toDto(Classes classes); 

    @Mapping(source = "instructorId", target = "instructor")
    @Mapping(target = "studentParents", ignore = true)
    Classes toEntity(ClassesDTO classesDTO); 
    default Classes fromId(Long id) {
        if (id == null) {
            return null;
        }
        Classes classes = new Classes();
        classes.setId(id);
        return classes;
    }
}
