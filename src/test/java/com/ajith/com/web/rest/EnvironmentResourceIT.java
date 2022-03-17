package com.ajith.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ajith.com.IntegrationTest;
import com.ajith.com.domain.Environment;
import com.ajith.com.repository.EnvironmentRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EnvironmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnvironmentResourceIT {

    private static final String DEFAULT_ENVIRONMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENVIRONMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENVIRONMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENVIRONMENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/environments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnvironmentMockMvc;

    private Environment environment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Environment createEntity(EntityManager em) {
        Environment environment = new Environment().environmentName(DEFAULT_ENVIRONMENT_NAME).environmentType(DEFAULT_ENVIRONMENT_TYPE);
        return environment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Environment createUpdatedEntity(EntityManager em) {
        Environment environment = new Environment().environmentName(UPDATED_ENVIRONMENT_NAME).environmentType(UPDATED_ENVIRONMENT_TYPE);
        return environment;
    }

    @BeforeEach
    public void initTest() {
        environment = createEntity(em);
    }

    @Test
    @Transactional
    void createEnvironment() throws Exception {
        int databaseSizeBeforeCreate = environmentRepository.findAll().size();
        // Create the Environment
        restEnvironmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isCreated());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate + 1);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getEnvironmentName()).isEqualTo(DEFAULT_ENVIRONMENT_NAME);
        assertThat(testEnvironment.getEnvironmentType()).isEqualTo(DEFAULT_ENVIRONMENT_TYPE);
    }

    @Test
    @Transactional
    void createEnvironmentWithExistingId() throws Exception {
        // Create the Environment with an existing ID
        environment.setId(1L);

        int databaseSizeBeforeCreate = environmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnvironmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnvironmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = environmentRepository.findAll().size();
        // set the field null
        environment.setEnvironmentName(null);

        // Create the Environment, which fails.

        restEnvironmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest());

        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnvironmentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = environmentRepository.findAll().size();
        // set the field null
        environment.setEnvironmentType(null);

        // Create the Environment, which fails.

        restEnvironmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest());

        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnvironments() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environment.getId().intValue())))
            .andExpect(jsonPath("$.[*].environmentName").value(hasItem(DEFAULT_ENVIRONMENT_NAME)))
            .andExpect(jsonPath("$.[*].environmentType").value(hasItem(DEFAULT_ENVIRONMENT_TYPE)));
    }

    @Test
    @Transactional
    void getEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get the environment
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL_ID, environment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(environment.getId().intValue()))
            .andExpect(jsonPath("$.environmentName").value(DEFAULT_ENVIRONMENT_NAME))
            .andExpect(jsonPath("$.environmentType").value(DEFAULT_ENVIRONMENT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingEnvironment() throws Exception {
        // Get the environment
        restEnvironmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment
        Environment updatedEnvironment = environmentRepository.findById(environment.getId()).get();
        // Disconnect from session so that the updates on updatedEnvironment are not directly saved in db
        em.detach(updatedEnvironment);
        updatedEnvironment.environmentName(UPDATED_ENVIRONMENT_NAME).environmentType(UPDATED_ENVIRONMENT_TYPE);

        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnvironment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnvironment))
            )
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getEnvironmentName()).isEqualTo(UPDATED_ENVIRONMENT_NAME);
        assertThat(testEnvironment.getEnvironmentType()).isEqualTo(UPDATED_ENVIRONMENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, environment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnvironmentWithPatch() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment using partial update
        Environment partialUpdatedEnvironment = new Environment();
        partialUpdatedEnvironment.setId(environment.getId());

        partialUpdatedEnvironment.environmentName(UPDATED_ENVIRONMENT_NAME).environmentType(UPDATED_ENVIRONMENT_TYPE);

        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnvironment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnvironment))
            )
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getEnvironmentName()).isEqualTo(UPDATED_ENVIRONMENT_NAME);
        assertThat(testEnvironment.getEnvironmentType()).isEqualTo(UPDATED_ENVIRONMENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateEnvironmentWithPatch() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment using partial update
        Environment partialUpdatedEnvironment = new Environment();
        partialUpdatedEnvironment.setId(environment.getId());

        partialUpdatedEnvironment.environmentName(UPDATED_ENVIRONMENT_NAME).environmentType(UPDATED_ENVIRONMENT_TYPE);

        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnvironment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnvironment))
            )
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getEnvironmentName()).isEqualTo(UPDATED_ENVIRONMENT_NAME);
        assertThat(testEnvironment.getEnvironmentType()).isEqualTo(UPDATED_ENVIRONMENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, environment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(environment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(environment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(environment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeDelete = environmentRepository.findAll().size();

        // Delete the environment
        restEnvironmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, environment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
