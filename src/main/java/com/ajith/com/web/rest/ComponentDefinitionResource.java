package com.ajith.com.web.rest;

import com.ajith.com.domain.ComponentDefinition;
import com.ajith.com.repository.ComponentDefinitionRepository;
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
 * REST controller for managing {@link com.ajith.com.domain.ComponentDefinition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComponentDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(ComponentDefinitionResource.class);

    private static final String ENTITY_NAME = "componentDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponentDefinitionRepository componentDefinitionRepository;

    public ComponentDefinitionResource(ComponentDefinitionRepository componentDefinitionRepository) {
        this.componentDefinitionRepository = componentDefinitionRepository;
    }

    /**
     * {@code POST  /component-definitions} : Create a new componentDefinition.
     *
     * @param componentDefinition the componentDefinition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componentDefinition, or with status {@code 400 (Bad Request)} if the componentDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/component-definitions")
    public ResponseEntity<ComponentDefinition> createComponentDefinition(@Valid @RequestBody ComponentDefinition componentDefinition)
        throws URISyntaxException {
        log.debug("REST request to save ComponentDefinition : {}", componentDefinition);
        if (componentDefinition.getId() != null) {
            throw new BadRequestAlertException("A new componentDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComponentDefinition result = componentDefinitionRepository.save(componentDefinition);
        return ResponseEntity
            .created(new URI("/api/component-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /component-definitions/:id} : Updates an existing componentDefinition.
     *
     * @param id the id of the componentDefinition to save.
     * @param componentDefinition the componentDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentDefinition,
     * or with status {@code 400 (Bad Request)} if the componentDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componentDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/component-definitions/{id}")
    public ResponseEntity<ComponentDefinition> updateComponentDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComponentDefinition componentDefinition
    ) throws URISyntaxException {
        log.debug("REST request to update ComponentDefinition : {}, {}", id, componentDefinition);
        if (componentDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComponentDefinition result = componentDefinitionRepository.save(componentDefinition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componentDefinition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /component-definitions/:id} : Partial updates given fields of an existing componentDefinition, field will ignore if it is null
     *
     * @param id the id of the componentDefinition to save.
     * @param componentDefinition the componentDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentDefinition,
     * or with status {@code 400 (Bad Request)} if the componentDefinition is not valid,
     * or with status {@code 404 (Not Found)} if the componentDefinition is not found,
     * or with status {@code 500 (Internal Server Error)} if the componentDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/component-definitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComponentDefinition> partialUpdateComponentDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComponentDefinition componentDefinition
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComponentDefinition partially : {}, {}", id, componentDefinition);
        if (componentDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComponentDefinition> result = componentDefinitionRepository
            .findById(componentDefinition.getId())
            .map(existingComponentDefinition -> {
                if (componentDefinition.getComponentId() != null) {
                    existingComponentDefinition.setComponentId(componentDefinition.getComponentId());
                }
                if (componentDefinition.getComponentName() != null) {
                    existingComponentDefinition.setComponentName(componentDefinition.getComponentName());
                }

                return existingComponentDefinition;
            })
            .map(componentDefinitionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componentDefinition.getId().toString())
        );
    }

    /**
     * {@code GET  /component-definitions} : get all the componentDefinitions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componentDefinitions in body.
     */
    @GetMapping("/component-definitions")
    public List<ComponentDefinition> getAllComponentDefinitions(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ComponentDefinitions");
        return componentDefinitionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /component-definitions/:id} : get the "id" componentDefinition.
     *
     * @param id the id of the componentDefinition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componentDefinition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/component-definitions/{id}")
    public ResponseEntity<ComponentDefinition> getComponentDefinition(@PathVariable Long id) {
        log.debug("REST request to get ComponentDefinition : {}", id);
        Optional<ComponentDefinition> componentDefinition = componentDefinitionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(componentDefinition);
    }

    /**
     * {@code DELETE  /component-definitions/:id} : delete the "id" componentDefinition.
     *
     * @param id the id of the componentDefinition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/component-definitions/{id}")
    public ResponseEntity<Void> deleteComponentDefinition(@PathVariable Long id) {
        log.debug("REST request to delete ComponentDefinition : {}", id);
        componentDefinitionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
