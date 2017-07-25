package com.portal.parents.repository;

import com.portal.parents.domain.Classes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Classes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassesRepository extends JpaRepository<Classes,Long> {
    
}
