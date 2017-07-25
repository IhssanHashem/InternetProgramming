package com.portal.parents.service.mapper;

import com.portal.parents.domain.*;
import com.portal.parents.service.dto.AssignmentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Assignments and its DTO AssignmentsDTO.
 */
@Mapper(componentModel = "spring", uses = {ClassesMapper.class, })
public interface AssignmentsMapper extends EntityMapper <AssignmentsDTO, Assignments> {

    @Mapping(source = "classes.id", target = "classesId")
    AssignmentsDTO toDto(Assignments assignments); 

    @Mapping(source = "classesId", target = "classes")
    Assignments toEntity(AssignmentsDTO assignmentsDTO); 
    default Assignments fromId(Long id) {
        if (id == null) {
            return null;
        }
        Assignments assignments = new Assignments();
        assignments.setId(id);
        return assignments;
    }
}
