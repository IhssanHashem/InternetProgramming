package com.portal.parents.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.portal.parents.domain.Classes;

import com.portal.parents.repository.ClassesRepository;
import com.portal.parents.repository.search.ClassesSearchRepository;
import com.portal.parents.web.rest.util.HeaderUtil;
import com.portal.parents.service.dto.ClassesDTO;
import com.portal.parents.service.mapper.ClassesMapper;
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
 * REST controller for managing Classes.
 */
@RestController
@RequestMapping("/api")
public class ClassesResource {

    private final Logger log = LoggerFactory.getLogger(ClassesResource.class);

    private static final String ENTITY_NAME = "classes";

    private final ClassesRepository classesRepository;

    private final ClassesMapper classesMapper;

    private final ClassesSearchRepository classesSearchRepository;

    public ClassesResource(ClassesRepository classesRepository, ClassesMapper classesMapper, ClassesSearchRepository classesSearchRepository) {
        this.classesRepository = classesRepository;
        this.classesMapper = classesMapper;
        this.classesSearchRepository = classesSearchRepository;
    }

    /**
     * POST  /classes : Create a new classes.
     *
     * @param classesDTO the classesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classesDTO, or with status 400 (Bad Request) if the classes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/classes")
    @Timed
    public ResponseEntity<ClassesDTO> createClasses(@Valid @RequestBody ClassesDTO classesDTO) throws URISyntaxException {
        log.debug("REST request to save Classes : {}", classesDTO);
        if (classesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new classes cannot already have an ID")).body(null);
        }
        Classes classes = classesMapper.toEntity(classesDTO);
        classes = classesRepository.save(classes);
        ClassesDTO result = classesMapper.toDto(classes);
        classesSearchRepository.save(classes);
        return ResponseEntity.created(new URI("/api/classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classes : Updates an existing classes.
     *
     * @param classesDTO the classesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classesDTO,
     * or with status 400 (Bad Request) if the classesDTO is not valid,
     * or with status 500 (Internal Server Error) if the classesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/classes")
    @Timed
    public ResponseEntity<ClassesDTO> updateClasses(@Valid @RequestBody ClassesDTO classesDTO) throws URISyntaxException {
        log.debug("REST request to update Classes : {}", classesDTO);
        if (classesDTO.getId() == null) {
            return createClasses(classesDTO);
        }
        Classes classes = classesMapper.toEntity(classesDTO);
        classes = classesRepository.save(classes);
        ClassesDTO result = classesMapper.toDto(classes);
        classesSearchRepository.save(classes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classes : get all the classes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classes in body
     */
    @GetMapping("/classes")
    @Timed
    public List<ClassesDTO> getAllClasses() {
        log.debug("REST request to get all Classes");
        List<Classes> classes = classesRepository.findAll();
        return classesMapper.toDto(classes);
    }

    /**
     * GET  /classes/:id : get the "id" classes.
     *
     * @param id the id of the classesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/classes/{id}")
    @Timed
    public ResponseEntity<ClassesDTO> getClasses(@PathVariable Long id) {
        log.debug("REST request to get Classes : {}", id);
        Classes classes = classesRepository.findOne(id);
        ClassesDTO classesDTO = classesMapper.toDto(classes);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classesDTO));
    }

    /**
     * DELETE  /classes/:id : delete the "id" classes.
     *
     * @param id the id of the classesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/classes/{id}")
    @Timed
    public ResponseEntity<Void> deleteClasses(@PathVariable Long id) {
        log.debug("REST request to delete Classes : {}", id);
        classesRepository.delete(id);
        classesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/classes?query=:query : search for the classes corresponding
     * to the query.
     *
     * @param query the query of the classes search
     * @return the result of the search
     */
    @GetMapping("/_search/classes")
    @Timed
    public List<ClassesDTO> searchClasses(@RequestParam String query) {
        log.debug("REST request to search Classes for query {}", query);
        return StreamSupport
            .stream(classesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(classesMapper::toDto)
            .collect(Collectors.toList());
    }

}
