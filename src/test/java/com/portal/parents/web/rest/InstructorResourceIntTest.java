package com.portal.parents.web.rest;

import com.portal.parents.ParentsPortalApp;

import com.portal.parents.domain.Instructor;
import com.portal.parents.repository.InstructorRepository;
import com.portal.parents.repository.search.InstructorSearchRepository;
import com.portal.parents.service.dto.InstructorDTO;
import com.portal.parents.service.mapper.InstructorMapper;
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
 * Test class for the InstructorResource REST controller.
 *
 * @see InstructorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ParentsPortalApp.class)
public class InstructorResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorMapper instructorMapper;

    @Autowired
    private InstructorSearchRepository instructorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInstructorMockMvc;

    private Instructor instructor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstructorResource instructorResource = new InstructorResource(instructorRepository, instructorMapper, instructorSearchRepository);
        this.restInstructorMockMvc = MockMvcBuilders.standaloneSetup(instructorResource)
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
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor()
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return instructor;
    }

    @Before
    public void initTest() {
        instructorSearchRepository.deleteAll();
        instructor = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor
        InstructorDTO instructorDTO = instructorMapper.toDto(instructor);
        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructorDTO)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInstructor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testInstructor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);

        // Validate the Instructor in Elasticsearch
        Instructor instructorEs = instructorSearchRepository.findOne(testInstructor.getId());
        assertThat(instructorEs).isEqualToComparingFieldByField(testInstructor);
    }

    @Test
    @Transactional
    public void createInstructorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor with an existing ID
        instructor.setId(1L);
        InstructorDTO instructorDTO = instructorMapper.toDto(instructor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().size();
        // set the field null
        instructor.setEmail(null);

        // Create the Instructor, which fails.
        InstructorDTO instructorDTO = instructorMapper.toDto(instructor);

        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructorDTO)))
            .andExpect(status().isBadRequest());

        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstructors() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get all the instructorList
        restInstructorMockMvc.perform(get("/api/instructors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instructor.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstructor() throws Exception {
        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findOne(instructor.getId());
        updatedInstructor
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        InstructorDTO instructorDTO = instructorMapper.toDto(updatedInstructor);

        restInstructorMockMvc.perform(put("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructorDTO)))
            .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInstructor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testInstructor.getLastName()).isEqualTo(UPDATED_LAST_NAME);

        // Validate the Instructor in Elasticsearch
        Instructor instructorEs = instructorSearchRepository.findOne(testInstructor.getId());
        assertThat(instructorEs).isEqualToComparingFieldByField(testInstructor);
    }

    @Test
    @Transactional
    public void updateNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Create the Instructor
        InstructorDTO instructorDTO = instructorMapper.toDto(instructor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInstructorMockMvc.perform(put("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructorDTO)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);
        int databaseSizeBeforeDelete = instructorRepository.findAll().size();

        // Get the instructor
        restInstructorMockMvc.perform(delete("/api/instructors/{id}", instructor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean instructorExistsInEs = instructorSearchRepository.exists(instructor.getId());
        assertThat(instructorExistsInEs).isFalse();

        // Validate the database is empty
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);

        // Search the instructor
        restInstructorMockMvc.perform(get("/api/_search/instructors?query=id:" + instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instructor.class);
        Instructor instructor1 = new Instructor();
        instructor1.setId(1L);
        Instructor instructor2 = new Instructor();
        instructor2.setId(instructor1.getId());
        assertThat(instructor1).isEqualTo(instructor2);
        instructor2.setId(2L);
        assertThat(instructor1).isNotEqualTo(instructor2);
        instructor1.setId(null);
        assertThat(instructor1).isNotEqualTo(instructor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstructorDTO.class);
        InstructorDTO instructorDTO1 = new InstructorDTO();
        instructorDTO1.setId(1L);
        InstructorDTO instructorDTO2 = new InstructorDTO();
        assertThat(instructorDTO1).isNotEqualTo(instructorDTO2);
        instructorDTO2.setId(instructorDTO1.getId());
        assertThat(instructorDTO1).isEqualTo(instructorDTO2);
        instructorDTO2.setId(2L);
        assertThat(instructorDTO1).isNotEqualTo(instructorDTO2);
        instructorDTO1.setId(null);
        assertThat(instructorDTO1).isNotEqualTo(instructorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(instructorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(instructorMapper.fromId(null)).isNull();
    }
}
