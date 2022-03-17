package com.ajith.com.web.rest;

import com.ajith.com.domain.ComponentSet;
import com.ajith.com.repository.ComponentSetRepository;
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
 * REST controller for managing {@link com.ajith.com.domain.ComponentSet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComponentSetResource {

    private final Logger log = LoggerFactory.getLogger(ComponentSetResource.class);

    private static final String ENTITY_NAME = "componentSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponentSetRepository componentSetRepository;

    public ComponentSetResource(ComponentSetRepository componentSetRepository) {
        this.componentSetRepository = componentSetRepository;
    }

    /**
     * {@code POST  /component-sets} : Create a new componentSet.
     *
     * @param componentSet the componentSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componentSet, or with status {@code 400 (Bad Request)} if the componentSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/component-sets")
    public ResponseEntity<ComponentSet> createComponentSet(@Valid @RequestBody ComponentSet componentSet) throws URISyntaxException {
        log.debug("REST request to save ComponentSet : {}", componentSet);
        if (componentSet.getId() != null) {
            throw new BadRequestAlertException("A new componentSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComponentSet result = componentSetRepository.save(componentSet);
        return ResponseEntity
            .created(new URI("/api/component-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /component-sets/:id} : Updates an existing componentSet.
     *
     * @param id the id of the componentSet to save.
     * @param componentSet the componentSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentSet,
     * or with status {@code 400 (Bad Request)} if the componentSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componentSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/component-sets/{id}")
    public ResponseEntity<ComponentSet> updateComponentSet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComponentSet componentSet
    ) throws URISyntaxException {
        log.debug("REST request to update ComponentSet : {}, {}", id, componentSet);
        if (componentSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComponentSet result = componentSetRepository.save(componentSet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componentSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /component-sets/:id} : Partial updates given fields of an existing componentSet, field will ignore if it is null
     *
     * @param id the id of the componentSet to save.
     * @param componentSet the componentSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentSet,
     * or with status {@code 400 (Bad Request)} if the componentSet is not valid,
     * or with status {@code 404 (Not Found)} if the componentSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the componentSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/component-sets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComponentSet> partialUpdateComponentSet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComponentSet componentSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComponentSet partially : {}, {}", id, componentSet);
        if (componentSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComponentSet> result = componentSetRepository
            .findById(componentSet.getId())
            .map(existingComponentSet -> {
                if (componentSet.getComponentSetName() != null) {
                    existingComponentSet.setComponentSetName(componentSet.getComponentSetName());
                }

                return existingComponentSet;
            })
            .map(componentSetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componentSet.getId().toString())
        );
    }

    /**
     * {@code GET  /component-sets} : get all the componentSets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componentSets in body.
     */
    @GetMapping("/component-sets")
    public List<ComponentSet> getAllComponentSets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ComponentSets");
        return componentSetRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /component-sets/:id} : get the "id" componentSet.
     *
     * @param id the id of the componentSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componentSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/component-sets/{id}")
    public ResponseEntity<ComponentSet> getComponentSet(@PathVariable Long id) {
        log.debug("REST request to get ComponentSet : {}", id);
        Optional<ComponentSet> componentSet = componentSetRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(componentSet);
    }

    /**
     * {@code DELETE  /component-sets/:id} : delete the "id" componentSet.
     *
     * @param id the id of the componentSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/component-sets/{id}")
    public ResponseEntity<Void> deleteComponentSet(@PathVariable Long id) {
        log.debug("REST request to delete ComponentSet : {}", id);
        componentSetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
