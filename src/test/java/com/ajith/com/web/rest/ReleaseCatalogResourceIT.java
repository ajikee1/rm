package com.ajith.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ajith.com.IntegrationTest;
import com.ajith.com.domain.ReleaseCatalog;
import com.ajith.com.repository.ReleaseCatalogRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ReleaseCatalogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReleaseCatalogResourceIT {

    private static final String DEFAULT_RELEASE_ID = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SCHEDULED_DATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHEDULED_DATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/release-catalogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReleaseCatalogRepository releaseCatalogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleaseCatalogMockMvc;

    private ReleaseCatalog releaseCatalog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleaseCatalog createEntity(EntityManager em) {
        ReleaseCatalog releaseCatalog = new ReleaseCatalog().releaseId(DEFAULT_RELEASE_ID).scheduledDateTime(DEFAULT_SCHEDULED_DATE_TIME);
        return releaseCatalog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleaseCatalog createUpdatedEntity(EntityManager em) {
        ReleaseCatalog releaseCatalog = new ReleaseCatalog().releaseId(UPDATED_RELEASE_ID).scheduledDateTime(UPDATED_SCHEDULED_DATE_TIME);
        return releaseCatalog;
    }

    @BeforeEach
    public void initTest() {
        releaseCatalog = createEntity(em);
    }

    @Test
    @Transactional
    void createReleaseCatalog() throws Exception {
        int databaseSizeBeforeCreate = releaseCatalogRepository.findAll().size();
        // Create the ReleaseCatalog
        restReleaseCatalogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isCreated());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeCreate + 1);
        ReleaseCatalog testReleaseCatalog = releaseCatalogList.get(releaseCatalogList.size() - 1);
        assertThat(testReleaseCatalog.getReleaseId()).isEqualTo(DEFAULT_RELEASE_ID);
        assertThat(testReleaseCatalog.getScheduledDateTime()).isEqualTo(DEFAULT_SCHEDULED_DATE_TIME);
    }

    @Test
    @Transactional
    void createReleaseCatalogWithExistingId() throws Exception {
        // Create the ReleaseCatalog with an existing ID
        releaseCatalog.setId(1L);

        int databaseSizeBeforeCreate = releaseCatalogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleaseCatalogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReleaseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseCatalogRepository.findAll().size();
        // set the field null
        releaseCatalog.setReleaseId(null);

        // Create the ReleaseCatalog, which fails.

        restReleaseCatalogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduledDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseCatalogRepository.findAll().size();
        // set the field null
        releaseCatalog.setScheduledDateTime(null);

        // Create the ReleaseCatalog, which fails.

        restReleaseCatalogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReleaseCatalogs() throws Exception {
        // Initialize the database
        releaseCatalogRepository.saveAndFlush(releaseCatalog);

        // Get all the releaseCatalogList
        restReleaseCatalogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releaseCatalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].releaseId").value(hasItem(DEFAULT_RELEASE_ID)))
            .andExpect(jsonPath("$.[*].scheduledDateTime").value(hasItem(DEFAULT_SCHEDULED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getReleaseCatalog() throws Exception {
        // Initialize the database
        releaseCatalogRepository.saveAndFlush(releaseCatalog);

        // Get the releaseCatalog
        restReleaseCatalogMockMvc
            .perform(get(ENTITY_API_URL_ID, releaseCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releaseCatalog.getId().intValue()))
            .andExpect(jsonPath("$.releaseId").value(DEFAULT_RELEASE_ID))
            .andExpect(jsonPath("$.scheduledDateTime").value(DEFAULT_SCHEDULED_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReleaseCatalog() throws Exception {
        // Get the releaseCatalog
        restReleaseCatalogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReleaseCatalog() throws Exception {
        // Initialize the database
        releaseCatalogRepository.saveAndFlush(releaseCatalog);

        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();

        // Update the releaseCatalog
        ReleaseCatalog updatedReleaseCatalog = releaseCatalogRepository.findById(releaseCatalog.getId()).get();
        // Disconnect from session so that the updates on updatedReleaseCatalog are not directly saved in db
        em.detach(updatedReleaseCatalog);
        updatedReleaseCatalog.releaseId(UPDATED_RELEASE_ID).scheduledDateTime(UPDATED_SCHEDULED_DATE_TIME);

        restReleaseCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReleaseCatalog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReleaseCatalog))
            )
            .andExpect(status().isOk());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
        ReleaseCatalog testReleaseCatalog = releaseCatalogList.get(releaseCatalogList.size() - 1);
        assertThat(testReleaseCatalog.getReleaseId()).isEqualTo(UPDATED_RELEASE_ID);
        assertThat(testReleaseCatalog.getScheduledDateTime()).isEqualTo(UPDATED_SCHEDULED_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingReleaseCatalog() throws Exception {
        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();
        releaseCatalog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releaseCatalog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReleaseCatalog() throws Exception {
        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();
        releaseCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReleaseCatalog() throws Exception {
        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();
        releaseCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseCatalogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseCatalog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReleaseCatalogWithPatch() throws Exception {
        // Initialize the database
        releaseCatalogRepository.saveAndFlush(releaseCatalog);

        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();

        // Update the releaseCatalog using partial update
        ReleaseCatalog partialUpdatedReleaseCatalog = new ReleaseCatalog();
        partialUpdatedReleaseCatalog.setId(releaseCatalog.getId());

        restReleaseCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleaseCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleaseCatalog))
            )
            .andExpect(status().isOk());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
        ReleaseCatalog testReleaseCatalog = releaseCatalogList.get(releaseCatalogList.size() - 1);
        assertThat(testReleaseCatalog.getReleaseId()).isEqualTo(DEFAULT_RELEASE_ID);
        assertThat(testReleaseCatalog.getScheduledDateTime()).isEqualTo(DEFAULT_SCHEDULED_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateReleaseCatalogWithPatch() throws Exception {
        // Initialize the database
        releaseCatalogRepository.saveAndFlush(releaseCatalog);

        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();

        // Update the releaseCatalog using partial update
        ReleaseCatalog partialUpdatedReleaseCatalog = new ReleaseCatalog();
        partialUpdatedReleaseCatalog.setId(releaseCatalog.getId());

        partialUpdatedReleaseCatalog.releaseId(UPDATED_RELEASE_ID).scheduledDateTime(UPDATED_SCHEDULED_DATE_TIME);

        restReleaseCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleaseCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleaseCatalog))
            )
            .andExpect(status().isOk());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
        ReleaseCatalog testReleaseCatalog = releaseCatalogList.get(releaseCatalogList.size() - 1);
        assertThat(testReleaseCatalog.getReleaseId()).isEqualTo(UPDATED_RELEASE_ID);
        assertThat(testReleaseCatalog.getScheduledDateTime()).isEqualTo(UPDATED_SCHEDULED_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingReleaseCatalog() throws Exception {
        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();
        releaseCatalog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, releaseCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReleaseCatalog() throws Exception {
        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();
        releaseCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReleaseCatalog() throws Exception {
        int databaseSizeBeforeUpdate = releaseCatalogRepository.findAll().size();
        releaseCatalog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(releaseCatalog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleaseCatalog in the database
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReleaseCatalog() throws Exception {
        // Initialize the database
        releaseCatalogRepository.saveAndFlush(releaseCatalog);

        int databaseSizeBeforeDelete = releaseCatalogRepository.findAll().size();

        // Delete the releaseCatalog
        restReleaseCatalogMockMvc
            .perform(delete(ENTITY_API_URL_ID, releaseCatalog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleaseCatalog> releaseCatalogList = releaseCatalogRepository.findAll();
        assertThat(releaseCatalogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
