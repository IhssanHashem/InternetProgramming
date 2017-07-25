package com.portal.parents.repository;

import com.portal.parents.domain.StudentParents;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the StudentParents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentParentsRepository extends JpaRepository<StudentParents,Long> {
    
    @Query("select distinct student_parents from StudentParents student_parents left join fetch student_parents.classes")
    List<StudentParents> findAllWithEagerRelationships();

    @Query("select student_parents from StudentParents student_parents left join fetch student_parents.classes where student_parents.id =:id")
    StudentParents findOneWithEagerRelationships(@Param("id") Long id);
    
}
