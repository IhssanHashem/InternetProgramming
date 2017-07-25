package com.portal.parents.repository;

import com.portal.parents.domain.Assignments;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Assignments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentsRepository extends JpaRepository<Assignments,Long> {
    
}
