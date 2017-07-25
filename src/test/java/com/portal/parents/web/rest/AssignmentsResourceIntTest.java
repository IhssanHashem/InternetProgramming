package com.portal.parents.web.rest;

import com.portal.parents.ParentsPortalApp;

import com.portal.parents.domain.Assignments;
import com.portal.parents.repository.AssignmentsRepository;
import com.portal.parents.repository.search.AssignmentsSearchRepository;
import com.portal.parents.service.dto.AssignmentsDTO;
import com.portal.parents.service.mapper.AssignmentsMapper;
import com.portal.parents.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssignmentsResource REST controller.
 *
 * @see AssignmentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ParentsPortalApp.class)
public class AssignmentsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ASSIGNMENT_NUMBER = 1;
    private static final Integer UPDATED_ASSIGNMENT_NUMBER = 2;

    @Autowired
    private AssignmentsRepository assignmentsRepository;

    @Autowired
    private AssignmentsMapper assignmentsMapper;

    @Autowired
    private AssignmentsSearchRepository assignmentsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAssignmentsMockMvc;

    private Assignments assignments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssignmentsResource assignmentsResource = new AssignmentsResource(assignmentsRepository, assignmentsMapper, assignmentsSearchRepository);
        this.restAssignmentsMockMvc = MockMvcBuilders.standaloneSetup(assignmentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignments createEntity(EntityManager em) {
        Assignments assignments = new Assignments()
            .name(DEFAULT_NAME)
            .assignmentNumber(DEFAULT_ASSIGNMENT_NUMBER);
        return assignments;
    }

    @Before
    public void initTest() {
        assignmentsSearchRepository.deleteAll();
        assignments = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssignments() throws Exception {
        int databaseSizeBeforeCreate = assignmentsRepository.findAll().size();

        // Create the Assignments
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(assignments);
        restAssignmentsMockMvc.perform(post("/api/assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignmentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Assignments in the database
        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Assignments testAssignments = assignmentsList.get(assignmentsList.size() - 1);
        assertThat(testAssignments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAssignments.getAssignmentNumber()).isEqualTo(DEFAULT_ASSIGNMENT_NUMBER);

        // Validate the Assignments in Elasticsearch
        Assignments assignmentsEs = assignmentsSearchRepository.findOne(testAssignments.getId());
        assertThat(assignmentsEs).isEqualToComparingFieldByField(testAssignments);
    }

    @Test
    @Transactional
    public void createAssignmentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assignmentsRepository.findAll().size();

        // Create the Assignments with an existing ID
        assignments.setId(1L);
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(assignments);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignmentsMockMvc.perform(post("/api/assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignmentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentsRepository.findAll().size();
        // set the field null
        assignments.setName(null);

        // Create the Assignments, which fails.
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(assignments);

        restAssignmentsMockMvc.perform(post("/api/assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignmentsDTO)))
            .andExpect(status().isBadRequest());

        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssignmentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentsRepository.findAll().size();
        // set the field null
        assignments.setAssignmentNumber(null);

        // Create the Assignments, which fails.
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(assignments);

        restAssignmentsMockMvc.perform(post("/api/assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignmentsDTO)))
            .andExpect(status().isBadRequest());

        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssignments() throws Exception {
        // Initialize the database
        assignmentsRepository.saveAndFlush(assignments);

        // Get all the assignmentsList
        restAssignmentsMockMvc.perform(get("/api/assignments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignments.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].assignmentNumber").value(hasItem(DEFAULT_ASSIGNMENT_NUMBER)));
    }

    @Test
    @Transactional
    public void getAssignments() throws Exception {
        // Initialize the database
        assignmentsRepository.saveAndFlush(assignments);

        // Get the assignments
        restAssignmentsMockMvc.perform(get("/api/assignments/{id}", assignments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assignments.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.assignmentNumber").value(DEFAULT_ASSIGNMENT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingAssignments() throws Exception {
        // Get the assignments
        restAssignmentsMockMvc.perform(get("/api/assignments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssignments() throws Exception {
        // Initialize the database
        assignmentsRepository.saveAndFlush(assignments);
        assignmentsSearchRepository.save(assignments);
        int databaseSizeBeforeUpdate = assignmentsRepository.findAll().size();

        // Update the assignments
        Assignments updatedAssignments = assignmentsRepository.findOne(assignments.getId());
        updatedAssignments
            .name(UPDATED_NAME)
            .assignmentNumber(UPDATED_ASSIGNMENT_NUMBER);
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(updatedAssignments);

        restAssignmentsMockMvc.perform(put("/api/assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignmentsDTO)))
            .andExpect(status().isOk());

        // Validate the Assignments in the database
        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeUpdate);
        Assignments testAssignments = assignmentsList.get(assignmentsList.size() - 1);
        assertThat(testAssignments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssignments.getAssignmentNumber()).isEqualTo(UPDATED_ASSIGNMENT_NUMBER);

        // Validate the Assignments in Elasticsearch
        Assignments assignmentsEs = assignmentsSearchRepository.findOne(testAssignments.getId());
        assertThat(assignmentsEs).isEqualToComparingFieldByField(testAssignments);
    }

    @Test
    @Transactional
    public void updateNonExistingAssignments() throws Exception {
        int databaseSizeBeforeUpdate = assignmentsRepository.findAll().size();

        // Create the Assignments
        AssignmentsDTO assignmentsDTO = assignmentsMapper.toDto(assignments);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAssignmentsMockMvc.perform(put("/api/assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignmentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Assignments in the database
        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAssignments() throws Exception {
        // Initialize the database
        assignmentsRepository.saveAndFlush(assignments);
        assignmentsSearchRepository.save(assignments);
        int databaseSizeBeforeDelete = assignmentsRepository.findAll().size();

        // Get the assignments
        restAssignmentsMockMvc.perform(delete("/api/assignments/{id}", assignments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean assignmentsExistsInEs = assignmentsSearchRepository.exists(assignments.getId());
        assertThat(assignmentsExistsInEs).isFalse();

        // Validate the database is empty
        List<Assignments> assignmentsList = assignmentsRepository.findAll();
        assertThat(assignmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAssignments() throws Exception {
        // Initialize the database
        assignmentsRepository.saveAndFlush(assignments);
        assignmentsSearchRepository.save(assignments);

        // Search the assignments
        restAssignmentsMockMvc.perform(get("/api/_search/assignments?query=id:" + assignments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignments.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].assignmentNumber").value(hasItem(DEFAULT_ASSIGNMENT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assignments.class);
        Assignments assignments1 = new Assignments();
        assignments1.setId(1L);
        Assignments assignments2 = new Assignments();
        assignments2.setId(assignments1.getId());
        assertThat(assignments1).isEqualTo(assignments2);
        assignments2.setId(2L);
        assertThat(assignments1).isNotEqualTo(assignments2);
        assignments1.setId(null);
        assertThat(assignments1).isNotEqualTo(assignments2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentsDTO.class);
        AssignmentsDTO assignmentsDTO1 = new AssignmentsDTO();
        assignmentsDTO1.setId(1L);
        AssignmentsDTO assignmentsDTO2 = new AssignmentsDTO();
        assertThat(assignmentsDTO1).isNotEqualTo(assignmentsDTO2);
        assignmentsDTO2.setId(assignmentsDTO1.getId());
        assertThat(assignmentsDTO1).isEqualTo(assignmentsDTO2);
        assignmentsDTO2.setId(2L);
        assertThat(assignmentsDTO1).isNotEqualTo(assignmentsDTO2);
        assignmentsDTO1.setId(null);
        assertThat(assignmentsDTO1).isNotEqualTo(assignmentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(assignmentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(assignmentsMapper.fromId(null)).isNull();
    }
}
