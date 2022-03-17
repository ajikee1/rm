package com.ajith.com.web.rest;

import com.ajith.com.domain.ReleaseCatalog;
import com.ajith.com.repository.ReleaseCatalogRepository;
import com.ajith.com.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ajith.com.domain.ReleaseCatalog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReleaseCatalogResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseCatalogResource.class);

    private static final String ENTITY_NAME = "releaseCatalog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleaseCatalogRepository releaseCatalogRepository;

    public ReleaseCatalogResource(ReleaseCatalogRepository releaseCatalogRepository) {
        this.releaseCatalogRepository = releaseCatalogRepository;
    }

    /**
     * {@code POST  /release-catalogs} : Create a new releaseCatalog.
     *
     * @param releaseCatalog the releaseCatalog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releaseCatalog, or with status {@code 400 (Bad Request)} if the releaseCatalog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/release-catalogs")
    public ResponseEntity<ReleaseCatalog> createReleaseCatalog(@Valid @RequestBody ReleaseCatalog releaseCatalog)
        throws URISyntaxException {
        log.debug("REST request to save ReleaseCatalog : {}", releaseCatalog);
        if (releaseCatalog.getId() != null) {
            throw new BadRequestAlertException("A new releaseCatalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleaseCatalog result = releaseCatalogRepository.save(releaseCatalog);
        return ResponseEntity
            .created(new URI("/api/release-catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /release-catalogs/:id} : Updates an existing releaseCatalog.
     *
     * @param id the id of the releaseCatalog to save.
     * @param releaseCatalog the releaseCatalog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releaseCatalog,
     * or with status {@code 400 (Bad Request)} if the releaseCatalog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releaseCatalog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/release-catalogs/{id}")
    public ResponseEntity<ReleaseCatalog> updateReleaseCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReleaseCatalog releaseCatalog
    ) throws URISyntaxException {
        log.debug("REST request to update ReleaseCatalog : {}, {}", id, releaseCatalog);
        if (releaseCatalog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releaseCatalog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releaseCatalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReleaseCatalog result = releaseCatalogRepository.save(releaseCatalog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, releaseCatalog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /release-catalogs/:id} : Partial updates given fields of an existing releaseCatalog, field will ignore if it is null
     *
     * @param id the id of the releaseCatalog to save.
     * @param releaseCatalog the releaseCatalog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releaseCatalog,
     * or with status {@code 400 (Bad Request)} if the releaseCatalog is not valid,
     * or with status {@code 404 (Not Found)} if the releaseCatalog is not found,
     * or with status {@code 500 (Internal Server Error)} if the releaseCatalog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/release-catalogs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReleaseCatalog> partialUpdateReleaseCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReleaseCatalog releaseCatalog
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReleaseCatalog partially : {}, {}", id, releaseCatalog);
        if (releaseCatalog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releaseCatalog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releaseCatalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReleaseCatalog> result = releaseCatalogRepository
            .findById(releaseCatalog.getId())
            .map(existingReleaseCatalog -> {
                if (releaseCatalog.getReleaseId() != null) {
                    existingReleaseCatalog.setReleaseId(releaseCatalog.getReleaseId());
                }
                if (releaseCatalog.getScheduledDateTime() != null) {
                    existingReleaseCatalog.setScheduledDateTime(releaseCatalog.getScheduledDateTime());
                }

                return existingReleaseCatalog;
            })
            .map(releaseCatalogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, releaseCatalog.getId().toString())
        );
    }

    /**
     * {@code GET  /release-catalogs} : get all the releaseCatalogs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releaseCatalogs in body.
     */
    @GetMapping("/release-catalogs")
    public List<ReleaseCatalog> getAllReleaseCatalogs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ReleaseCatalogs");
        return releaseCatalogRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /release-catalogs/:id} : get the "id" releaseCatalog.
     *
     * @param id the id of the releaseCatalog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releaseCatalog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/release-catalogs/{id}")
    public ResponseEntity<ReleaseCatalog> getReleaseCatalog(@PathVariable Long id) {
        log.debug("REST request to get ReleaseCatalog : {}", id);
        Optional<ReleaseCatalog> releaseCatalog = releaseCatalogRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(releaseCatalog);
    }

    /**
     * {@code DELETE  /release-catalogs/:id} : delete the "id" releaseCatalog.
     *
     * @param id the id of the releaseCatalog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/release-catalogs/{id}")
    public ResponseEntity<Void> deleteReleaseCatalog(@PathVariable Long id) {
        log.debug("REST request to delete ReleaseCatalog : {}", id);
        releaseCatalogRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
