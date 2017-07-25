package com.portal.parents.web.rest;

import com.portal.parents.ParentsPortalApp;

import com.portal.parents.domain.Classes;
import com.portal.parents.repository.ClassesRepository;
import com.portal.parents.repository.search.ClassesSearchRepository;
import com.portal.parents.service.dto.ClassesDTO;
import com.portal.parents.service.mapper.ClassesMapper;
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
 * Test class for the ClassesResource REST controller.
 *
 * @see ClassesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ParentsPortalApp.class)
public class ClassesResourceIntTest {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private ClassesSearchRepository classesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassesMockMvc;

    private Classes classes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassesResource classesResource = new ClassesResource(classesRepository, classesMapper, classesSearchRepository);
        this.restClassesMockMvc = MockMvcBuilders.standaloneSetup(classesResource)
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
    public static Classes createEntity(EntityManager em) {
        Classes classes = new Classes()
            .className(DEFAULT_CLASS_NAME)
            .classDescription(DEFAULT_CLASS_DESCRIPTION);
        return classes;
    }

    @Before
    public void initTest() {
        classesSearchRepository.deleteAll();
        classes = createEntity(em);
    }

    @Test
    @Transactional
    public void createClasses() throws Exception {
        int databaseSizeBeforeCreate = classesRepository.findAll().size();

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);
        restClassesMockMvc.perform(post("/api/classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classesDTO)))
            .andExpect(status().isCreated());

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll();
        assertThat(classesList).hasSize(databaseSizeBeforeCreate + 1);
        Classes testClasses = classesList.get(classesList.size() - 1);
        assertThat(testClasses.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testClasses.getClassDescription()).isEqualTo(DEFAULT_CLASS_DESCRIPTION);

        // Validate the Classes in Elasticsearch
        Classes classesEs = classesSearchRepository.findOne(testClasses.getId());
        assertThat(classesEs).isEqualToComparingFieldByField(testClasses);
    }

    @Test
    @Transactional
    public void createClassesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classesRepository.findAll().size();

        // Create the Classes with an existing ID
        classes.setId(1L);
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassesMockMvc.perform(post("/api/classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Classes> classesList = classesRepository.findAll();
        assertThat(classesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classesRepository.findAll().size();
        // set the field null
        classes.setClassName(null);

        // Create the Classes, which fails.
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        restClassesMockMvc.perform(post("/api/classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classesDTO)))
            .andExpect(status().isBadRequest());

        List<Classes> classesList = classesRepository.findAll();
        assertThat(classesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClasses() throws Exception {
        // Initialize the database
        classesRepository.saveAndFlush(classes);

        // Get all the classesList
        restClassesMockMvc.perform(get("/api/classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classes.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].classDescription").value(hasItem(DEFAULT_CLASS_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getClasses() throws Exception {
        // Initialize the database
        classesRepository.saveAndFlush(classes);

        // Get the classes
        restClassesMockMvc.perform(get("/api/classes/{id}", classes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classes.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.classDescription").value(DEFAULT_CLASS_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClasses() throws Exception {
        // Get the classes
        restClassesMockMvc.perform(get("/api/classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClasses() throws Exception {
        // Initialize the database
        classesRepository.saveAndFlush(classes);
        classesSearchRepository.save(classes);
        int databaseSizeBeforeUpdate = classesRepository.findAll().size();

        // Update the classes
        Classes updatedClasses = classesRepository.findOne(classes.getId());
        updatedClasses
            .className(UPDATED_CLASS_NAME)
            .classDescription(UPDATED_CLASS_DESCRIPTION);
        ClassesDTO classesDTO = classesMapper.toDto(updatedClasses);

        restClassesMockMvc.perform(put("/api/classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classesDTO)))
            .andExpect(status().isOk());

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
        Classes testClasses = classesList.get(classesList.size() - 1);
        assertThat(testClasses.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testClasses.getClassDescription()).isEqualTo(UPDATED_CLASS_DESCRIPTION);

        // Validate the Classes in Elasticsearch
        Classes classesEs = classesSearchRepository.findOne(testClasses.getId());
        assertThat(classesEs).isEqualToComparingFieldByField(testClasses);
    }

    @Test
    @Transactional
    public void updateNonExistingClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().size();

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassesMockMvc.perform(put("/api/classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classesDTO)))
            .andExpect(status().isCreated());

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClasses() throws Exception {
        // Initialize the database
        classesRepository.saveAndFlush(classes);
        classesSearchRepository.save(classes);
        int databaseSizeBeforeDelete = classesRepository.findAll().size();

        // Get the classes
        restClassesMockMvc.perform(delete("/api/classes/{id}", classes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean classesExistsInEs = classesSearchRepository.exists(classes.getId());
        assertThat(classesExistsInEs).isFalse();

        // Validate the database is empty
        List<Classes> classesList = classesRepository.findAll();
        assertThat(classesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClasses() throws Exception {
        // Initialize the database
        classesRepository.saveAndFlush(classes);
        classesSearchRepository.save(classes);

        // Search the classes
        restClassesMockMvc.perform(get("/api/_search/classes?query=id:" + classes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classes.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].classDescription").value(hasItem(DEFAULT_CLASS_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classes.class);
        Classes classes1 = new Classes();
        classes1.setId(1L);
        Classes classes2 = new Classes();
        classes2.setId(classes1.getId());
        assertThat(classes1).isEqualTo(classes2);
        classes2.setId(2L);
        assertThat(classes1).isNotEqualTo(classes2);
        classes1.setId(null);
        assertThat(classes1).isNotEqualTo(classes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassesDTO.class);
        ClassesDTO classesDTO1 = new ClassesDTO();
        classesDTO1.setId(1L);
        ClassesDTO classesDTO2 = new ClassesDTO();
        assertThat(classesDTO1).isNotEqualTo(classesDTO2);
        classesDTO2.setId(classesDTO1.getId());
        assertThat(classesDTO1).isEqualTo(classesDTO2);
        classesDTO2.setId(2L);
        assertThat(classesDTO1).isNotEqualTo(classesDTO2);
        classesDTO1.setId(null);
        assertThat(classesDTO1).isNotEqualTo(classesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(classesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(classesMapper.fromId(null)).isNull();
    }
}
