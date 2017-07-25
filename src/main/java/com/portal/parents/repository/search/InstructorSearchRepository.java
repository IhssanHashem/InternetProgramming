package com.portal.parents.repository.search;

import com.portal.parents.domain.Instructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Instructor entity.
 */
public interface InstructorSearchRepository extends ElasticsearchRepository<Instructor, Long> {
}
