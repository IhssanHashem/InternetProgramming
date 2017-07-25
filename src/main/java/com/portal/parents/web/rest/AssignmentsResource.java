package com.portal.parents.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.portal.parents.domain.Assignments;

import com.portal.parents.repository.AssignmentsRepository;
import com.portal.parents.repository.search.AssignmentsSearchRepository;
import com.portal.parents.web.rest.util.HeaderUtil;
import com.portal.parents.service.dto.AssignmentsDTO;
import com.portal.parents.service.mapper.AssignmentsMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Assignments.
 */
@RestController
@RequestMapping("/api")
public class AssignmentsResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentsResource.class);

    private static final String ENTITY_NAME = "assignments";

    private final AssignmentsRepository assignmentsRepository;

    private final AssignmentsMapper assignmentsMapper;

    private final AssignmentsSearchRepository assignmentsSearchRepository;

    public AssignmentsResource(AssignmentsRepository assignmentsRepository, AssignmentsMapper assignmentsMapper, AssignmentsSearchRepository assignmentsSearchRepository) {
        this.assignmentsRepository = assignmentsRepository;
        this.assignmentsMapper = assignmentsMapper;
        this.assignmentsSearchRepository = assignmentsSearchRepository;
    }

    /**
     * POST  /assignments : Create a new assignments.
     *
     * @param assignmentsDTO the assignmentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assignmentsDTO, or with status 400 (Bad Request) if the assignments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assignments")
    @Timed
    public ResponseEntity<AssignmentsDTO> createAssignments(@Valid @RequestBody AssignmentsDTO assignmentsDTO) throws URISyntaxException {
        log.debug("REST request to save Assignments : {}", assignmentsDTO);
        if (assignmentsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new assignments cannot already have an ID")).body(null);
        }
        Assignments assignments = assignmentsMapper.toEntity(assignmentsDTO);
        assignments = assignmentsRepository.save(assignments);
        AssignmentsDTO result = assignmentsMapper.toDto(assignments);
        assignmentsSearchRepository.save(assignments);
        return ResponseEntity.created(new URI("/api/assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assignments : Updates an existing assignments.
     *
     * @param assignmentsDTO the assignmentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assignmentsDTO,
     * or with status 400 (Bad Request) if the assignmentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the assignmentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assignments")
    @Timed
    public ResponseEntity<AssignmentsDTO> updateAssignments(@Valid @RequestBody AssignmentsDTO assignmentsDTO) throws URISyntaxException {
        log.debug("REST request to update Assignments : {}", assignmentsDTO);
        if (assignmentsDTO.getId() == null) {
            return createAssignments(assignmentsDTO);
        }
        Assignments assignments = assignmentsMapper.toEntity(assignmentsDTO);
        assignments = assignmentsRepository.save(assignments);
        AssignmentsDTO result = assignmentsMapper.toDto(assignments);
        assignmentsSearchRepository.save(assignments);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assignmentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assignments : get all the assignments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of assignments in body
     */
    @GetMapping("/assignments")
    @Timed
    public List<AssignmentsDTO> getAllAssignments() {
        log.debug("REST request to get all Assignments");
        List<Assignments> assignments = assignmentsRepository.findAll();
        return assignmentsMapper.toDto(assignments);
    }

    /**
     * GET  /assignments/:id : get the "id" assignments.
     *
     * @param id the id of the assignmentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assignmentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/assignments/{id}")
    @Timed
    public ResponseEntity<AssignmentsDTO> getAssignments(@PathVariable Long id) {
        log.debug("REST request to get Assignments : {}", id);
        Assignments assignments = assignmentsRepository.findOne(id);
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(assignments);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(assignmentsDTO));
    }

    /**
     * DELETE  /assignments/:id : delete the "id" assignments.
     *
     * @param id the id of the assignmentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assignments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssignments(@PathVariable Long id) {
        log.debug("REST request to delete Assignments : {}", id);
        assignmentsRepository.delete(id);
        assignmentsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/assignments?query=:query : search for the assignments corresponding
     * to the query.
     *
     * @param query the query of the assignments search
     * @return the result of the search
     */
    @GetMapping("/_search/assignments")
    @Timed
    public List<AssignmentsDTO> searchAssignments(@RequestParam String query) {
        log.debug("REST request to search Assignments for query {}", query);
        return StreamSupport
            .stream(assignmentsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(assignmentsMapper::toDto)
            .collect(Collectors.toList());
    }

}
