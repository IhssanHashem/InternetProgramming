package com.portal.parents.repository;

import com.portal.parents.domain.Instructor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Instructor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstructorRepository extends JpaRepository<Instructor,Long> {
    
}
