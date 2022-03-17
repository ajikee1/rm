package com.ajith.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ajith.com.IntegrationTest;
import com.ajith.com.domain.ComponentDefinition;
import com.ajith.com.repository.ComponentDefinitionRepository;
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
 * Integration tests for the {@link ComponentDefinitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComponentDefinitionResourceIT {

    private static final Long DEFAULT_COMPONENT_ID = 1L;
    private static final Long UPDATED_COMPONENT_ID = 2L;

    private static final String DEFAULT_COMPONENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/component-definitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComponentDefinitionRepository componentDefinitionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComponentDefinitionMockMvc;

    private ComponentDefinition componentDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentDefinition createEntity(EntityManager em) {
        ComponentDefinition componentDefinition = new ComponentDefinition()
            .componentId(DEFAULT_COMPONENT_ID)
            .componentName(DEFAULT_COMPONENT_NAME);
        return componentDefinition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentDefinition createUpdatedEntity(EntityManager em) {
        ComponentDefinition componentDefinition = new ComponentDefinition()
            .componentId(UPDATED_COMPONENT_ID)
            .componentName(UPDATED_COMPONENT_NAME);
        return componentDefinition;
    }

    @BeforeEach
    public void initTest() {
        componentDefinition = createEntity(em);
    }

    @Test
    @Transactional
    void createComponentDefinition() throws Exception {
        int databaseSizeBeforeCreate = componentDefinitionRepository.findAll().size();
        // Create the ComponentDefinition
        restComponentDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isCreated());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        ComponentDefinition testComponentDefinition = componentDefinitionList.get(componentDefinitionList.size() - 1);
        assertThat(testComponentDefinition.getComponentId()).isEqualTo(DEFAULT_COMPONENT_ID);
        assertThat(testComponentDefinition.getComponentName()).isEqualTo(DEFAULT_COMPONENT_NAME);
    }

    @Test
    @Transactional
    void createComponentDefinitionWithExistingId() throws Exception {
        // Create the ComponentDefinition with an existing ID
        componentDefinition.setId(1L);

        int databaseSizeBeforeCreate = componentDefinitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponentDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComponentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentDefinitionRepository.findAll().size();
        // set the field null
        componentDefinition.setComponentId(null);

        // Create the ComponentDefinition, which fails.

        restComponentDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComponentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentDefinitionRepository.findAll().size();
        // set the field null
        componentDefinition.setComponentName(null);

        // Create the ComponentDefinition, which fails.

        restComponentDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComponentDefinitions() throws Exception {
        // Initialize the database
        componentDefinitionRepository.saveAndFlush(componentDefinition);

        // Get all the componentDefinitionList
        restComponentDefinitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componentDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].componentId").value(hasItem(DEFAULT_COMPONENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].componentName").value(hasItem(DEFAULT_COMPONENT_NAME)));
    }

    @Test
    @Transactional
    void getComponentDefinition() throws Exception {
        // Initialize the database
        componentDefinitionRepository.saveAndFlush(componentDefinition);

        // Get the componentDefinition
        restComponentDefinitionMockMvc
            .perform(get(ENTITY_API_URL_ID, componentDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(componentDefinition.getId().intValue()))
            .andExpect(jsonPath("$.componentId").value(DEFAULT_COMPONENT_ID.intValue()))
            .andExpect(jsonPath("$.componentName").value(DEFAULT_COMPONENT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingComponentDefinition() throws Exception {
        // Get the componentDefinition
        restComponentDefinitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComponentDefinition() throws Exception {
        // Initialize the database
        componentDefinitionRepository.saveAndFlush(componentDefinition);

        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();

        // Update the componentDefinition
        ComponentDefinition updatedComponentDefinition = componentDefinitionRepository.findById(componentDefinition.getId()).get();
        // Disconnect from session so that the updates on updatedComponentDefinition are not directly saved in db
        em.detach(updatedComponentDefinition);
        updatedComponentDefinition.componentId(UPDATED_COMPONENT_ID).componentName(UPDATED_COMPONENT_NAME);

        restComponentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComponentDefinition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComponentDefinition))
            )
            .andExpect(status().isOk());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ComponentDefinition testComponentDefinition = componentDefinitionList.get(componentDefinitionList.size() - 1);
        assertThat(testComponentDefinition.getComponentId()).isEqualTo(UPDATED_COMPONENT_ID);
        assertThat(testComponentDefinition.getComponentName()).isEqualTo(UPDATED_COMPONENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingComponentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();
        componentDefinition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, componentDefinition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComponentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();
        componentDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComponentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();
        componentDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComponentDefinitionWithPatch() throws Exception {
        // Initialize the database
        componentDefinitionRepository.saveAndFlush(componentDefinition);

        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();

        // Update the componentDefinition using partial update
        ComponentDefinition partialUpdatedComponentDefinition = new ComponentDefinition();
        partialUpdatedComponentDefinition.setId(componentDefinition.getId());

        partialUpdatedComponentDefinition.componentId(UPDATED_COMPONENT_ID);

        restComponentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentDefinition))
            )
            .andExpect(status().isOk());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ComponentDefinition testComponentDefinition = componentDefinitionList.get(componentDefinitionList.size() - 1);
        assertThat(testComponentDefinition.getComponentId()).isEqualTo(UPDATED_COMPONENT_ID);
        assertThat(testComponentDefinition.getComponentName()).isEqualTo(DEFAULT_COMPONENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateComponentDefinitionWithPatch() throws Exception {
        // Initialize the database
        componentDefinitionRepository.saveAndFlush(componentDefinition);

        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();

        // Update the componentDefinition using partial update
        ComponentDefinition partialUpdatedComponentDefinition = new ComponentDefinition();
        partialUpdatedComponentDefinition.setId(componentDefinition.getId());

        partialUpdatedComponentDefinition.componentId(UPDATED_COMPONENT_ID).componentName(UPDATED_COMPONENT_NAME);

        restComponentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentDefinition))
            )
            .andExpect(status().isOk());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ComponentDefinition testComponentDefinition = componentDefinitionList.get(componentDefinitionList.size() - 1);
        assertThat(testComponentDefinition.getComponentId()).isEqualTo(UPDATED_COMPONENT_ID);
        assertThat(testComponentDefinition.getComponentName()).isEqualTo(UPDATED_COMPONENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingComponentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();
        componentDefinition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, componentDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComponentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();
        componentDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComponentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = componentDefinitionRepository.findAll().size();
        componentDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentDefinition in the database
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComponentDefinition() throws Exception {
        // Initialize the database
        componentDefinitionRepository.saveAndFlush(componentDefinition);

        int databaseSizeBeforeDelete = componentDefinitionRepository.findAll().size();

        // Delete the componentDefinition
        restComponentDefinitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, componentDefinition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComponentDefinition> componentDefinitionList = componentDefinitionRepository.findAll();
        assertThat(componentDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
