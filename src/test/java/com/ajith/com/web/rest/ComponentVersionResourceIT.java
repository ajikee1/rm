package com.ajith.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ajith.com.IntegrationTest;
import com.ajith.com.domain.ComponentVersion;
import com.ajith.com.repository.ComponentVersionRepository;
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
 * Integration tests for the {@link ComponentVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComponentVersionResourceIT {

    private static final Long DEFAULT_COMPONENT_VERSION_NUMBER = 1L;
    private static final Long UPDATED_COMPONENT_VERSION_NUMBER = 2L;

    private static final String ENTITY_API_URL = "/api/component-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComponentVersionRepository componentVersionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComponentVersionMockMvc;

    private ComponentVersion componentVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentVersion createEntity(EntityManager em) {
        ComponentVersion componentVersion = new ComponentVersion().componentVersionNumber(DEFAULT_COMPONENT_VERSION_NUMBER);
        return componentVersion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentVersion createUpdatedEntity(EntityManager em) {
        ComponentVersion componentVersion = new ComponentVersion().componentVersionNumber(UPDATED_COMPONENT_VERSION_NUMBER);
        return componentVersion;
    }

    @BeforeEach
    public void initTest() {
        componentVersion = createEntity(em);
    }

    @Test
    @Transactional
    void createComponentVersion() throws Exception {
        int databaseSizeBeforeCreate = componentVersionRepository.findAll().size();
        // Create the ComponentVersion
        restComponentVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isCreated());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeCreate + 1);
        ComponentVersion testComponentVersion = componentVersionList.get(componentVersionList.size() - 1);
        assertThat(testComponentVersion.getComponentVersionNumber()).isEqualTo(DEFAULT_COMPONENT_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void createComponentVersionWithExistingId() throws Exception {
        // Create the ComponentVersion with an existing ID
        componentVersion.setId(1L);

        int databaseSizeBeforeCreate = componentVersionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponentVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComponentVersionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentVersionRepository.findAll().size();
        // set the field null
        componentVersion.setComponentVersionNumber(null);

        // Create the ComponentVersion, which fails.

        restComponentVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isBadRequest());

        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComponentVersions() throws Exception {
        // Initialize the database
        componentVersionRepository.saveAndFlush(componentVersion);

        // Get all the componentVersionList
        restComponentVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componentVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].componentVersionNumber").value(hasItem(DEFAULT_COMPONENT_VERSION_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getComponentVersion() throws Exception {
        // Initialize the database
        componentVersionRepository.saveAndFlush(componentVersion);

        // Get the componentVersion
        restComponentVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, componentVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(componentVersion.getId().intValue()))
            .andExpect(jsonPath("$.componentVersionNumber").value(DEFAULT_COMPONENT_VERSION_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingComponentVersion() throws Exception {
        // Get the componentVersion
        restComponentVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComponentVersion() throws Exception {
        // Initialize the database
        componentVersionRepository.saveAndFlush(componentVersion);

        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();

        // Update the componentVersion
        ComponentVersion updatedComponentVersion = componentVersionRepository.findById(componentVersion.getId()).get();
        // Disconnect from session so that the updates on updatedComponentVersion are not directly saved in db
        em.detach(updatedComponentVersion);
        updatedComponentVersion.componentVersionNumber(UPDATED_COMPONENT_VERSION_NUMBER);

        restComponentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComponentVersion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComponentVersion))
            )
            .andExpect(status().isOk());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
        ComponentVersion testComponentVersion = componentVersionList.get(componentVersionList.size() - 1);
        assertThat(testComponentVersion.getComponentVersionNumber()).isEqualTo(UPDATED_COMPONENT_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingComponentVersion() throws Exception {
        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();
        componentVersion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, componentVersion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComponentVersion() throws Exception {
        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();
        componentVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComponentVersion() throws Exception {
        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();
        componentVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentVersionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComponentVersionWithPatch() throws Exception {
        // Initialize the database
        componentVersionRepository.saveAndFlush(componentVersion);

        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();

        // Update the componentVersion using partial update
        ComponentVersion partialUpdatedComponentVersion = new ComponentVersion();
        partialUpdatedComponentVersion.setId(componentVersion.getId());

        restComponentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentVersion))
            )
            .andExpect(status().isOk());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
        ComponentVersion testComponentVersion = componentVersionList.get(componentVersionList.size() - 1);
        assertThat(testComponentVersion.getComponentVersionNumber()).isEqualTo(DEFAULT_COMPONENT_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateComponentVersionWithPatch() throws Exception {
        // Initialize the database
        componentVersionRepository.saveAndFlush(componentVersion);

        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();

        // Update the componentVersion using partial update
        ComponentVersion partialUpdatedComponentVersion = new ComponentVersion();
        partialUpdatedComponentVersion.setId(componentVersion.getId());

        partialUpdatedComponentVersion.componentVersionNumber(UPDATED_COMPONENT_VERSION_NUMBER);

        restComponentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentVersion))
            )
            .andExpect(status().isOk());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
        ComponentVersion testComponentVersion = componentVersionList.get(componentVersionList.size() - 1);
        assertThat(testComponentVersion.getComponentVersionNumber()).isEqualTo(UPDATED_COMPONENT_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingComponentVersion() throws Exception {
        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();
        componentVersion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, componentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComponentVersion() throws Exception {
        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();
        componentVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComponentVersion() throws Exception {
        int databaseSizeBeforeUpdate = componentVersionRepository.findAll().size();
        componentVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentVersion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentVersion in the database
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComponentVersion() throws Exception {
        // Initialize the database
        componentVersionRepository.saveAndFlush(componentVersion);

        int databaseSizeBeforeDelete = componentVersionRepository.findAll().size();

        // Delete the componentVersion
        restComponentVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, componentVersion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComponentVersion> componentVersionList = componentVersionRepository.findAll();
        assertThat(componentVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
