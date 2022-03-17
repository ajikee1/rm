package com.ajith.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ajith.com.IntegrationTest;
import com.ajith.com.domain.ComponentSet;
import com.ajith.com.repository.ComponentSetRepository;
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
 * Integration tests for the {@link ComponentSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComponentSetResourceIT {

    private static final String DEFAULT_COMPONENT_SET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENT_SET_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/component-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComponentSetRepository componentSetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComponentSetMockMvc;

    private ComponentSet componentSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentSet createEntity(EntityManager em) {
        ComponentSet componentSet = new ComponentSet().componentSetName(DEFAULT_COMPONENT_SET_NAME);
        return componentSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentSet createUpdatedEntity(EntityManager em) {
        ComponentSet componentSet = new ComponentSet().componentSetName(UPDATED_COMPONENT_SET_NAME);
        return componentSet;
    }

    @BeforeEach
    public void initTest() {
        componentSet = createEntity(em);
    }

    @Test
    @Transactional
    void createComponentSet() throws Exception {
        int databaseSizeBeforeCreate = componentSetRepository.findAll().size();
        // Create the ComponentSet
        restComponentSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentSet)))
            .andExpect(status().isCreated());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeCreate + 1);
        ComponentSet testComponentSet = componentSetList.get(componentSetList.size() - 1);
        assertThat(testComponentSet.getComponentSetName()).isEqualTo(DEFAULT_COMPONENT_SET_NAME);
    }

    @Test
    @Transactional
    void createComponentSetWithExistingId() throws Exception {
        // Create the ComponentSet with an existing ID
        componentSet.setId(1L);

        int databaseSizeBeforeCreate = componentSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponentSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentSet)))
            .andExpect(status().isBadRequest());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComponentSetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentSetRepository.findAll().size();
        // set the field null
        componentSet.setComponentSetName(null);

        // Create the ComponentSet, which fails.

        restComponentSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentSet)))
            .andExpect(status().isBadRequest());

        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComponentSets() throws Exception {
        // Initialize the database
        componentSetRepository.saveAndFlush(componentSet);

        // Get all the componentSetList
        restComponentSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componentSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].componentSetName").value(hasItem(DEFAULT_COMPONENT_SET_NAME)));
    }

    @Test
    @Transactional
    void getComponentSet() throws Exception {
        // Initialize the database
        componentSetRepository.saveAndFlush(componentSet);

        // Get the componentSet
        restComponentSetMockMvc
            .perform(get(ENTITY_API_URL_ID, componentSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(componentSet.getId().intValue()))
            .andExpect(jsonPath("$.componentSetName").value(DEFAULT_COMPONENT_SET_NAME));
    }

    @Test
    @Transactional
    void getNonExistingComponentSet() throws Exception {
        // Get the componentSet
        restComponentSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComponentSet() throws Exception {
        // Initialize the database
        componentSetRepository.saveAndFlush(componentSet);

        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();

        // Update the componentSet
        ComponentSet updatedComponentSet = componentSetRepository.findById(componentSet.getId()).get();
        // Disconnect from session so that the updates on updatedComponentSet are not directly saved in db
        em.detach(updatedComponentSet);
        updatedComponentSet.componentSetName(UPDATED_COMPONENT_SET_NAME);

        restComponentSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComponentSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComponentSet))
            )
            .andExpect(status().isOk());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
        ComponentSet testComponentSet = componentSetList.get(componentSetList.size() - 1);
        assertThat(testComponentSet.getComponentSetName()).isEqualTo(UPDATED_COMPONENT_SET_NAME);
    }

    @Test
    @Transactional
    void putNonExistingComponentSet() throws Exception {
        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();
        componentSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, componentSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComponentSet() throws Exception {
        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();
        componentSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComponentSet() throws Exception {
        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();
        componentSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentSetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentSet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComponentSetWithPatch() throws Exception {
        // Initialize the database
        componentSetRepository.saveAndFlush(componentSet);

        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();

        // Update the componentSet using partial update
        ComponentSet partialUpdatedComponentSet = new ComponentSet();
        partialUpdatedComponentSet.setId(componentSet.getId());

        partialUpdatedComponentSet.componentSetName(UPDATED_COMPONENT_SET_NAME);

        restComponentSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentSet))
            )
            .andExpect(status().isOk());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
        ComponentSet testComponentSet = componentSetList.get(componentSetList.size() - 1);
        assertThat(testComponentSet.getComponentSetName()).isEqualTo(UPDATED_COMPONENT_SET_NAME);
    }

    @Test
    @Transactional
    void fullUpdateComponentSetWithPatch() throws Exception {
        // Initialize the database
        componentSetRepository.saveAndFlush(componentSet);

        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();

        // Update the componentSet using partial update
        ComponentSet partialUpdatedComponentSet = new ComponentSet();
        partialUpdatedComponentSet.setId(componentSet.getId());

        partialUpdatedComponentSet.componentSetName(UPDATED_COMPONENT_SET_NAME);

        restComponentSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentSet))
            )
            .andExpect(status().isOk());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
        ComponentSet testComponentSet = componentSetList.get(componentSetList.size() - 1);
        assertThat(testComponentSet.getComponentSetName()).isEqualTo(UPDATED_COMPONENT_SET_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingComponentSet() throws Exception {
        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();
        componentSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, componentSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComponentSet() throws Exception {
        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();
        componentSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComponentSet() throws Exception {
        int databaseSizeBeforeUpdate = componentSetRepository.findAll().size();
        componentSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentSetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(componentSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentSet in the database
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComponentSet() throws Exception {
        // Initialize the database
        componentSetRepository.saveAndFlush(componentSet);

        int databaseSizeBeforeDelete = componentSetRepository.findAll().size();

        // Delete the componentSet
        restComponentSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, componentSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComponentSet> componentSetList = componentSetRepository.findAll();
        assertThat(componentSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
