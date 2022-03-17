package com.ajith.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ajith.com.IntegrationTest;
import com.ajith.com.domain.DeploymentKindCatalog;
import com.ajith.com.repository.DeploymentKindCatalogRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DeploymentKindCatalogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeploymentKindCatalogResourceIT {

    private static final String DEFAULT_DEPLOYMENT_KIND = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOYMENT_KIND = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOYMENT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOYMENT_DEFINITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deployment-kind-catalogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeploymentKindCatalogRepository deploymentKindCatalogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeploymentKindCatalogMockMvc;

    private DeploymentKindCatalog deploymentKindCatalog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeploymentKindCatalog createEntity(EntityManager em) {
        DeploymentKindCatalog deploymentKindCatalog = new DeploymentKindCatalog()
            .deploymentKind(DEFAULT_DEPLOYMENT_KIND)
            .deploymentDefinition(DEFAULT_DEPLOYMENT_DEFINITION);
        return deploymentKindCatalog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeploymentKindCatalog createUpdatedEntity(EntityManager em) {
        DeploymentKindCatalog deploymentKindCatalog = new DeploymentKindCatalog()
            .deploymentKind(UPDATED_DEPLOYMENT_KIND)
            .deploymentDefinition(UPDATED_DEPLOYMENT_DEFINITION);
        return deploymentKindCatalog;
    }

    @BeforeEach
    public void initTest() {
        deploymentKindCatalog = createEntity(em);
    }

    @Test
    @Transactional
    void createDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeCreate = deploymentKindCatalogRepository.findAll().size();
        // Create the DeploymentKindCatalog
        restDeploymentKindCatalogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isCreated());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeCreate + 1);
        DeploymentKindCatalog testDeploymentKindCatalog = deploymentKindCatalogList.get(deploymentKindCatalogList.size() - 1);
        assertThat(testDeploymentKindCatalog.getDeploymentKind()).isEqualTo(DEFAULT_DEPLOYMENT_KIND);
        assertThat(testDeploymentKindCatalog.getDeploymentDefinition()).isEqualTo(DEFAULT_DEPLOYMENT_DEFINITION);
    }

    @Test
    @Transactional
    void createDeploymentKindCatalogWithExistingId() throws Exception {
        // Create the DeploymentKindCatalog with an existing ID
        deploymentKindCatalog.setId(1L);

        int databaseSizeBeforeCreate = deploymentKindCatalogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeploymentKindCatalogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeploymentKindIsRequired() throws Exception {
        int databaseSizeBeforeTest = deploymentKindCatalogRepository.findAll().size();
        // set the field null
        deploymentKindCatalog.setDeploymentKind(null);

        // Create the DeploymentKindCatalog, which fails.

        restDeploymentKindCatalogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isBadRequest());

        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeploymentKindCatalogs() throws Exception {
        // Initialize the database
        deploymentKindCatalogRepository.saveAndFlush(deploymentKindCatalog);

        // Get all the deploymentKindCatalogList
        restDeploymentKindCatalogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deploymentKindCatalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].deploymentKind").value(hasItem(DEFAULT_DEPLOYMENT_KIND)))
            .andExpect(jsonPath("$.[*].deploymentDefinition").value(hasItem(DEFAULT_DEPLOYMENT_DEFINITION.toString())));
    }

    @Test
    @Transactional
    void getDeploymentKindCatalog() throws Exception {
        // Initialize the database
        deploymentKindCatalogRepository.saveAndFlush(deploymentKindCatalog);

        // Get the deploymentKindCatalog
        restDeploymentKindCatalogMockMvc
            .perform(get(ENTITY_API_URL_ID, deploymentKindCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deploymentKindCatalog.getId().intValue()))
            .andExpect(jsonPath("$.deploymentKind").value(DEFAULT_DEPLOYMENT_KIND))
            .andExpect(jsonPath("$.deploymentDefinition").value(DEFAULT_DEPLOYMENT_DEFINITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeploymentKindCatalog() throws Exception {
        // Get the deploymentKindCatalog
        restDeploymentKindCatalogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeploymentKindCatalog() throws Exception {
        // Initialize the database
        deploymentKindCatalogRepository.saveAndFlush(deploymentKindCatalog);

        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();

        // Update the deploymentKindCatalog
        DeploymentKindCatalog updatedDeploymentKindCatalog = deploymentKindCatalogRepository.findById(deploymentKindCatalog.getId()).get();
        // Disconnect from session so that the updates on updatedDeploymentKindCatalog are not directly saved in db
        em.detach(updatedDeploymentKindCatalog);
        updatedDeploymentKindCatalog.deploymentKind(UPDATED_DEPLOYMENT_KIND).deploymentDefinition(UPDATED_DEPLOYMENT_DEFINITION);

        restDeploymentKindCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeploymentKindCatalog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeploymentKindCatalog))
            )
            .andExpect(status().isOk());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
        DeploymentKindCatalog testDeploymentKindCatalog = deploymentKindCatalogList.get(deploymentKindCatalogList.size() - 1);
        assertThat(testDeploymentKindCatalog.getDeploymentKind()).isEqualTo(UPDATED_DEPLOYMENT_KIND);
        assertThat(testDeploymentKindCatalog.getDeploymentDefinition()).isEqualTo(UPDATED_DEPLOYMENT_DEFINITION);
    }

    @Test
    @Transactional
    void putNonExistingDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();
        deploymentKindCatalog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeploymentKindCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deploymentKindCatalog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();
        deploymentKindCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentKindCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();
        deploymentKindCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentKindCatalogMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeploymentKindCatalogWithPatch() throws Exception {
        // Initialize the database
        deploymentKindCatalogRepository.saveAndFlush(deploymentKindCatalog);

        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();

        // Update the deploymentKindCatalog using partial update
        DeploymentKindCatalog partialUpdatedDeploymentKindCatalog = new DeploymentKindCatalog();
        partialUpdatedDeploymentKindCatalog.setId(deploymentKindCatalog.getId());

        partialUpdatedDeploymentKindCatalog.deploymentKind(UPDATED_DEPLOYMENT_KIND).deploymentDefinition(UPDATED_DEPLOYMENT_DEFINITION);

        restDeploymentKindCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeploymentKindCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeploymentKindCatalog))
            )
            .andExpect(status().isOk());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
        DeploymentKindCatalog testDeploymentKindCatalog = deploymentKindCatalogList.get(deploymentKindCatalogList.size() - 1);
        assertThat(testDeploymentKindCatalog.getDeploymentKind()).isEqualTo(UPDATED_DEPLOYMENT_KIND);
        assertThat(testDeploymentKindCatalog.getDeploymentDefinition()).isEqualTo(UPDATED_DEPLOYMENT_DEFINITION);
    }

    @Test
    @Transactional
    void fullUpdateDeploymentKindCatalogWithPatch() throws Exception {
        // Initialize the database
        deploymentKindCatalogRepository.saveAndFlush(deploymentKindCatalog);

        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();

        // Update the deploymentKindCatalog using partial update
        DeploymentKindCatalog partialUpdatedDeploymentKindCatalog = new DeploymentKindCatalog();
        partialUpdatedDeploymentKindCatalog.setId(deploymentKindCatalog.getId());

        partialUpdatedDeploymentKindCatalog.deploymentKind(UPDATED_DEPLOYMENT_KIND).deploymentDefinition(UPDATED_DEPLOYMENT_DEFINITION);

        restDeploymentKindCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeploymentKindCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeploymentKindCatalog))
            )
            .andExpect(status().isOk());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
        DeploymentKindCatalog testDeploymentKindCatalog = deploymentKindCatalogList.get(deploymentKindCatalogList.size() - 1);
        assertThat(testDeploymentKindCatalog.getDeploymentKind()).isEqualTo(UPDATED_DEPLOYMENT_KIND);
        assertThat(testDeploymentKindCatalog.getDeploymentDefinition()).isEqualTo(UPDATED_DEPLOYMENT_DEFINITION);
    }

    @Test
    @Transactional
    void patchNonExistingDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();
        deploymentKindCatalog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeploymentKindCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deploymentKindCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();
        deploymentKindCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentKindCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeploymentKindCatalog() throws Exception {
        int databaseSizeBeforeUpdate = deploymentKindCatalogRepository.findAll().size();
        deploymentKindCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentKindCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deploymentKindCatalog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeploymentKindCatalog in the database
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeploymentKindCatalog() throws Exception {
        // Initialize the database
        deploymentKindCatalogRepository.saveAndFlush(deploymentKindCatalog);

        int databaseSizeBeforeDelete = deploymentKindCatalogRepository.findAll().size();

        // Delete the deploymentKindCatalog
        restDeploymentKindCatalogMockMvc
            .perform(delete(ENTITY_API_URL_ID, deploymentKindCatalog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeploymentKindCatalog> deploymentKindCatalogList = deploymentKindCatalogRepository.findAll();
        assertThat(deploymentKindCatalogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
