package com.portal.parents.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.portal.parents.domain.Instructor;

import com.portal.parents.repository.InstructorRepository;
import com.portal.parents.repository.search.InstructorSearchRepository;
import com.portal.parents.web.rest.util.HeaderUtil;
import com.portal.parents.service.dto.InstructorDTO;
import com.portal.parents.service.mapper.InstructorMapper;
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
 * REST controller for managing Instructor.
 */
@RestController
@RequestMapping("/api")
public class InstructorResource {

    private final Logger log = LoggerFactory.getLogger(InstructorResource.class);

    private static final String ENTITY_NAME = "instructor";

    private final InstructorRepository instructorRepository;

    private final InstructorMapper instructorMapper;

    private final InstructorSearchRepository instructorSearchRepository;

    public InstructorResource(InstructorRepository instructorRepository, InstructorMapper instructorMapper, InstructorSearchRepository instructorSearchRepository) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper = instructorMapper;
        this.instructorSearchRepository = instructorSearchRepository;
    }

    /**
     * POST  /instructors : Create a new instructor.
     *
     * @param instructorDTO the instructorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instructorDTO, or with status 400 (Bad Request) if the instructor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/instructors")
    @Timed
    public ResponseEntity<InstructorDTO> createInstructor(@Valid @RequestBody InstructorDTO instructorDTO) throws URISyntaxException {
        log.debug("REST request to save Instructor : {}", instructorDTO);
        if (instructorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new instructor cannot already have an ID")).body(null);
        }
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        instructor = instructorRepository.save(instructor);
        InstructorDTO result = instructorMapper.toDto(instructor);
        instructorSearchRepository.save(instructor);
        return ResponseEntity.created(new URI("/api/instructors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instructors : Updates an existing instructor.
     *
     * @param instructorDTO the instructorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instructorDTO,
     * or with status 400 (Bad Request) if the instructorDTO is not valid,
     * or with status 500 (Internal Server Error) if the instructorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/instructors")
    @Timed
    public ResponseEntity<InstructorDTO> updateInstructor(@Valid @RequestBody InstructorDTO instructorDTO) throws URISyntaxException {
        log.debug("REST request to update Instructor : {}", instructorDTO);
        if (instructorDTO.getId() == null) {
            return createInstructor(instructorDTO);
        }
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        instructor = instructorRepository.save(instructor);
        InstructorDTO result = instructorMapper.toDto(instructor);
        instructorSearchRepository.save(instructor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, instructorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instructors : get all the instructors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instructors in body
     */
    @GetMapping("/instructors")
    @Timed
    public List<InstructorDTO> getAllInstructors() {
        log.debug("REST request to get all Instructors");
        List<Instructor> instructors = instructorRepository.findAll();
        return instructorMapper.toDto(instructors);
    }

    /**
     * GET  /instructors/:id : get the "id" instructor.
     *
     * @param id the id of the instructorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instructorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/instructors/{id}")
    @Timed
    public ResponseEntity<InstructorDTO> getInstructor(@PathVariable Long id) {
        log.debug("REST request to get Instructor : {}", id);
        Instructor instructor = instructorRepository.findOne(id);
        InstructorDTO instructorDTO = instructorMapper.toDto(instructor);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(instructorDTO));
    }

    /**
     * DELETE  /instructors/:id : delete the "id" instructor.
     *
     * @param id the id of the instructorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/instructors/{id}")
    @Timed
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        log.debug("REST request to delete Instructor : {}", id);
        instructorRepository.delete(id);
        instructorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/instructors?query=:query : search for the instructor corresponding
     * to the query.
     *
     * @param query the query of the instructor search
     * @return the result of the search
     */
    @GetMapping("/_search/instructors")
    @Timed
    public List<InstructorDTO> searchInstructors(@RequestParam String query) {
        log.debug("REST request to search Instructors for query {}", query);
        return StreamSupport
            .stream(instructorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(instructorMapper::toDto)
            .collect(Collectors.toList());
    }

}
