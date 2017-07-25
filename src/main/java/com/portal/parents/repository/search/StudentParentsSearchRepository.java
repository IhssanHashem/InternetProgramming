package com.portal.parents.repository.search;

import com.portal.parents.domain.StudentParents;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudentParents entity.
 */
public interface StudentParentsSearchRepository extends ElasticsearchRepository<StudentParents, Long> {
}
