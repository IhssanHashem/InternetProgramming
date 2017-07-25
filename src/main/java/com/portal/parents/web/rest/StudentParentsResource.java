package com.portal.parents.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.portal.parents.domain.StudentParents;

import com.portal.parents.repository.StudentParentsRepository;
import com.portal.parents.repository.search.StudentParentsSearchRepository;
import com.portal.parents.web.rest.util.HeaderUtil;
import com.portal.parents.service.dto.StudentParentsDTO;
import com.portal.parents.service.mapper.StudentParentsMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StudentParents.
 */
@RestController
@RequestMapping("/api")
public class StudentParentsResource {

    private final Logger log = LoggerFactory.getLogger(StudentParentsResource.class);

    private static final String ENTITY_NAME = "studentParents";

    private final StudentParentsRepository studentParentsRepository;

    private final StudentParentsMapper studentParentsMapper;

    private final StudentParentsSearchRepository studentParentsSearchRepository;

    public StudentParentsResource(StudentParentsRepository studentParentsRepository, StudentParentsMapper studentParentsMapper, StudentParentsSearchRepository studentParentsSearchRepository) {
        this.studentParentsRepository = studentParentsRepository;
        this.studentParentsMapper = studentParentsMapper;
        this.studentParentsSearchRepository = studentParentsSearchRepository;
    }

    /**
     * POST  /student-parents : Create a new studentParents.
     *
     * @param studentParentsDTO the studentParentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentParentsDTO, or with status 400 (Bad Request) if the studentParents has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-parents")
    @Timed
    public ResponseEntity<StudentParentsDTO> createStudentParents(@RequestBody StudentParentsDTO studentParentsDTO) throws URISyntaxException {
        log.debug("REST request to save StudentParents : {}", studentParentsDTO);
        if (studentParentsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studentParents cannot already have an ID")).body(null);
        }
        StudentParents studentParents = studentParentsMapper.toEntity(studentParentsDTO);
        studentParents = studentParentsRepository.save(studentParents);
        StudentParentsDTO result = studentParentsMapper.toDto(studentParents);
        studentParentsSearchRepository.save(studentParents);
        return ResponseEntity.created(new URI("/api/student-parents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /student-parents : Updates an existing studentParents.
     *
     * @param studentParentsDTO the studentParentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentParentsDTO,
     * or with status 400 (Bad Request) if the studentParentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentParentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-parents")
    @Timed
    public ResponseEntity<StudentParentsDTO> updateStudentParents(@RequestBody StudentParentsDTO studentParentsDTO) throws URISyntaxException {
        log.debug("REST request to update StudentParents : {}", studentParentsDTO);
        if (studentParentsDTO.getId() == null) {
            return createStudentParents(studentParentsDTO);
        }
        StudentParents studentParents = studentParentsMapper.toEntity(studentParentsDTO);
        studentParents = studentParentsRepository.save(studentParents);
        StudentParentsDTO result = studentParentsMapper.toDto(studentParents);
        studentParentsSearchRepository.save(studentParents);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentParentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /student-parents : get all the studentParents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of studentParents in body
     */
    @GetMapping("/student-parents")
    @Timed
    public List<StudentParentsDTO> getAllStudentParents() {
        log.debug("REST request to get all StudentParents");
        List<StudentParents> studentParents = studentParentsRepository.findAllWithEagerRelationships();
        return studentParentsMapper.toDto(studentParents);
    }

    /**
     * GET  /student-parents/:id : get the "id" studentParents.
     *
     * @param id the id of the studentParentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentParentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/student-parents/{id}")
    @Timed
    public ResponseEntity<StudentParentsDTO> getStudentParents(@PathVariable Long id) {
        log.debug("REST request to get StudentParents : {}", id);
        StudentParents studentParents = studentParentsRepository.findOneWithEagerRelationships(id);
        StudentParentsDTO studentParentsDTO = studentParentsMapper.toDto(studentParents);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentParentsDTO));
    }

    /**
     * DELETE  /student-parents/:id : delete the "id" studentParents.
     *
     * @param id the id of the studentParentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-parents/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentParents(@PathVariable Long id) {
        log.debug("REST request to delete StudentParents : {}", id);
        studentParentsRepository.delete(id);
        studentParentsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/student-parents?query=:query : search for the studentParents corresponding
     * to the query.
     *
     * @param query the query of the studentParents search
     * @return the result of the search
     */
    @GetMapping("/_search/student-parents")
    @Timed
    public List<StudentParentsDTO> searchStudentParents(@RequestParam String query) {
        log.debug("REST request to search StudentParents for query {}", query);
        return StreamSupport
            .stream(studentParentsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(studentParentsMapper::toDto)
            .collect(Collectors.toList());
    }

}
