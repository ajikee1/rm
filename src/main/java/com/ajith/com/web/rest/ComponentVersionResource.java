package com.ajith.com.web.rest;

import com.ajith.com.domain.ComponentVersion;
import com.ajith.com.repository.ComponentVersionRepository;
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
 * REST controller for managing {@link com.ajith.com.domain.ComponentVersion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComponentVersionResource {

    private final Logger log = LoggerFactory.getLogger(ComponentVersionResource.class);

    private static final String ENTITY_NAME = "componentVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponentVersionRepository componentVersionRepository;

    public ComponentVersionResource(ComponentVersionRepository componentVersionRepository) {
        this.componentVersionRepository = componentVersionRepository;
    }

    /**
     * {@code POST  /component-versions} : Create a new componentVersion.
     *
     * @param componentVersion the componentVersion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componentVersion, or with status {@code 400 (Bad Request)} if the componentVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/component-versions")
    public ResponseEntity<ComponentVersion> createComponentVersion(@Valid @RequestBody ComponentVersion componentVersion)
        throws URISyntaxException {
        log.debug("REST request to save ComponentVersion : {}", componentVersion);
        if (componentVersion.getId() != null) {
            throw new BadRequestAlertException("A new componentVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComponentVersion result = componentVersionRepository.save(componentVersion);
        return ResponseEntity
            .created(new URI("/api/component-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /component-versions/:id} : Updates an existing componentVersion.
     *
     * @param id the id of the componentVersion to save.
     * @param componentVersion the componentVersion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentVersion,
     * or with status {@code 400 (Bad Request)} if the componentVersion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componentVersion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/component-versions/{id}")
    public ResponseEntity<ComponentVersion> updateComponentVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComponentVersion componentVersion
    ) throws URISyntaxException {
        log.debug("REST request to update ComponentVersion : {}, {}", id, componentVersion);
        if (componentVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentVersion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComponentVersion result = componentVersionRepository.save(componentVersion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componentVersion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /component-versions/:id} : Partial updates given fields of an existing componentVersion, field will ignore if it is null
     *
     * @param id the id of the componentVersion to save.
     * @param componentVersion the componentVersion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentVersion,
     * or with status {@code 400 (Bad Request)} if the componentVersion is not valid,
     * or with status {@code 404 (Not Found)} if the componentVersion is not found,
     * or with status {@code 500 (Internal Server Error)} if the componentVersion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/component-versions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComponentVersion> partialUpdateComponentVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComponentVersion componentVersion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComponentVersion partially : {}, {}", id, componentVersion);
        if (componentVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentVersion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComponentVersion> result = componentVersionRepository
            .findById(componentVersion.getId())
            .map(existingComponentVersion -> {
                if (componentVersion.getComponentVersionNumber() != null) {
                    existingComponentVersion.setComponentVersionNumber(componentVersion.getComponentVersionNumber());
                }

                return existingComponentVersion;
            })
            .map(componentVersionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componentVersion.getId().toString())
        );
    }

    /**
     * {@code GET  /component-versions} : get all the componentVersions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componentVersions in body.
     */
    @GetMapping("/component-versions")
    public List<ComponentVersion> getAllComponentVersions(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ComponentVersions");
        return componentVersionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /component-versions/:id} : get the "id" componentVersion.
     *
     * @param id the id of the componentVersion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componentVersion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/component-versions/{id}")
    public ResponseEntity<ComponentVersion> getComponentVersion(@PathVariable Long id) {
        log.debug("REST request to get ComponentVersion : {}", id);
        Optional<ComponentVersion> componentVersion = componentVersionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(componentVersion);
    }

    /**
     * {@code DELETE  /component-versions/:id} : delete the "id" componentVersion.
     *
     * @param id the id of the componentVersion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/component-versions/{id}")
    public ResponseEntity<Void> deleteComponentVersion(@PathVariable Long id) {
        log.debug("REST request to delete ComponentVersion : {}", id);
        componentVersionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
