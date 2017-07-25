package com.portal.parents.web.rest;

import com.portal.parents.ParentsPortalApp;

import com.portal.parents.domain.StudentParents;
import com.portal.parents.repository.StudentParentsRepository;
import com.portal.parents.repository.search.StudentParentsSearchRepository;
import com.portal.parents.service.dto.StudentParentsDTO;
import com.portal.parents.service.mapper.StudentParentsMapper;
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
 * Test class for the StudentParentsResource REST controller.
 *
 * @see StudentParentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ParentsPortalApp.class)
public class StudentParentsResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    @Autowired
    private StudentParentsRepository studentParentsRepository;

    @Autowired
    private StudentParentsMapper studentParentsMapper;

    @Autowired
    private StudentParentsSearchRepository studentParentsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentParentsMockMvc;

    private StudentParents studentParents;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentParentsResource studentParentsResource = new StudentParentsResource(studentParentsRepository, studentParentsMapper, studentParentsSearchRepository);
        this.restStudentParentsMockMvc = MockMvcBuilders.standaloneSetup(studentParentsResource)
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
    public static StudentParents createEntity(EntityManager em) {
        StudentParents studentParents = new StudentParents()
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return studentParents;
    }

    @Before
    public void initTest() {
        studentParentsSearchRepository.deleteAll();
        studentParents = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentParents() throws Exception {
        int databaseSizeBeforeCreate = studentParentsRepository.findAll().size();

        // Create the StudentParents
        StudentParentsDTO studentParentsDTO = studentParentsMapper.toDto(studentParents);
        restStudentParentsMockMvc.perform(post("/api/student-parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentParentsDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentParents in the database
        List<StudentParents> studentParentsList = studentParentsRepository.findAll();
        assertThat(studentParentsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentParents testStudentParents = studentParentsList.get(studentParentsList.size() - 1);
        assertThat(testStudentParents.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudentParents.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudentParents.getLastName()).isEqualTo(DEFAULT_LAST_NAME);

        // Validate the StudentParents in Elasticsearch
        StudentParents studentParentsEs = studentParentsSearchRepository.findOne(testStudentParents.getId());
        assertThat(studentParentsEs).isEqualToComparingFieldByField(testStudentParents);
    }

    @Test
    @Transactional
    public void createStudentParentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentParentsRepository.findAll().size();

        // Create the StudentParents with an existing ID
        studentParents.setId(1L);
        StudentParentsDTO studentParentsDTO = studentParentsMapper.toDto(studentParents);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentParentsMockMvc.perform(post("/api/student-parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentParentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentParents> studentParentsList = studentParentsRepository.findAll();
        assertThat(studentParentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudentParents() throws Exception {
        // Initialize the database
        studentParentsRepository.saveAndFlush(studentParents);

        // Get all the studentParentsList
        restStudentParentsMockMvc.perform(get("/api/student-parents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentParents.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStudentParents() throws Exception {
        // Initialize the database
        studentParentsRepository.saveAndFlush(studentParents);

        // Get the studentParents
        restStudentParentsMockMvc.perform(get("/api/student-parents/{id}", studentParents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studentParents.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStudentParents() throws Exception {
        // Get the studentParents
        restStudentParentsMockMvc.perform(get("/api/student-parents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentParents() throws Exception {
        // Initialize the database
        studentParentsRepository.saveAndFlush(studentParents);
        studentParentsSearchRepository.save(studentParents);
        int databaseSizeBeforeUpdate = studentParentsRepository.findAll().size();

        // Update the studentParents
        StudentParents updatedStudentParents = studentParentsRepository.findOne(studentParents.getId());
        updatedStudentParents
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        StudentParentsDTO studentParentsDTO = studentParentsMapper.toDto(updatedStudentParents);

        restStudentParentsMockMvc.perform(put("/api/student-parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentParentsDTO)))
            .andExpect(status().isOk());

        // Validate the StudentParents in the database
        List<StudentParents> studentParentsList = studentParentsRepository.findAll();
        assertThat(studentParentsList).hasSize(databaseSizeBeforeUpdate);
        StudentParents testStudentParents = studentParentsList.get(studentParentsList.size() - 1);
        assertThat(testStudentParents.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudentParents.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudentParents.getLastName()).isEqualTo(UPDATED_LAST_NAME);

        // Validate the StudentParents in Elasticsearch
        StudentParents studentParentsEs = studentParentsSearchRepository.findOne(testStudentParents.getId());
        assertThat(studentParentsEs).isEqualToComparingFieldByField(testStudentParents);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentParents() throws Exception {
        int databaseSizeBeforeUpdate = studentParentsRepository.findAll().size();

        // Create the StudentParents
        StudentParentsDTO studentParentsDTO = studentParentsMapper.toDto(studentParents);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentParentsMockMvc.perform(put("/api/student-parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentParentsDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentParents in the database
        List<StudentParents> studentParentsList = studentParentsRepository.findAll();
        assertThat(studentParentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudentParents() throws Exception {
        // Initialize the database
        studentParentsRepository.saveAndFlush(studentParents);
        studentParentsSearchRepository.save(studentParents);
        int databaseSizeBeforeDelete = studentParentsRepository.findAll().size();

        // Get the studentParents
        restStudentParentsMockMvc.perform(delete("/api/student-parents/{id}", studentParents.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentParentsExistsInEs = studentParentsSearchRepository.exists(studentParents.getId());
        assertThat(studentParentsExistsInEs).isFalse();

        // Validate the database is empty
        List<StudentParents> studentParentsList = studentParentsRepository.findAll();
        assertThat(studentParentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudentParents() throws Exception {
        // Initialize the database
        studentParentsRepository.saveAndFlush(studentParents);
        studentParentsSearchRepository.save(studentParents);

        // Search the studentParents
        restStudentParentsMockMvc.perform(get("/api/_search/student-parents?query=id:" + studentParents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentParents.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentParents.class);
        StudentParents studentParents1 = new StudentParents();
        studentParents1.setId(1L);
        StudentParents studentParents2 = new StudentParents();
        studentParents2.setId(studentParents1.getId());
        assertThat(studentParents1).isEqualTo(studentParents2);
        studentParents2.setId(2L);
        assertThat(studentParents1).isNotEqualTo(studentParents2);
        studentParents1.setId(null);
        assertThat(studentParents1).isNotEqualTo(studentParents2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentParentsDTO.class);
        StudentParentsDTO studentParentsDTO1 = new StudentParentsDTO();
        studentParentsDTO1.setId(1L);
        StudentParentsDTO studentParentsDTO2 = new StudentParentsDTO();
        assertThat(studentParentsDTO1).isNotEqualTo(studentParentsDTO2);
        studentParentsDTO2.setId(studentParentsDTO1.getId());
        assertThat(studentParentsDTO1).isEqualTo(studentParentsDTO2);
        studentParentsDTO2.setId(2L);
        assertThat(studentParentsDTO1).isNotEqualTo(studentParentsDTO2);
        studentParentsDTO1.setId(null);
        assertThat(studentParentsDTO1).isNotEqualTo(studentParentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(studentParentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(studentParentsMapper.fromId(null)).isNull();
    }
}
