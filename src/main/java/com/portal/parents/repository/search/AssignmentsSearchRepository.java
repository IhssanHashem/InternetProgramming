package com.portal.parents.repository.search;

import com.portal.parents.domain.Assignments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Assignments entity.
 */
public interface AssignmentsSearchRepository extends ElasticsearchRepository<Assignments, Long> {
}
