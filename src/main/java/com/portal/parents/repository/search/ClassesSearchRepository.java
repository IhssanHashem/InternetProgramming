package com.portal.parents.repository.search;

import com.portal.parents.domain.Classes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Classes entity.
 */
public interface ClassesSearchRepository extends ElasticsearchRepository<Classes, Long> {
}
