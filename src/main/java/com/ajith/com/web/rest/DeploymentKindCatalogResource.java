package com.ajith.com.web.rest;

import com.ajith.com.domain.DeploymentKindCatalog;
import com.ajith.com.repository.DeploymentKindCatalogRepository;
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
 * REST controller for managing {@link com.ajith.com.domain.DeploymentKindCatalog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeploymentKindCatalogResource {

    private final Logger log = LoggerFactory.getLogger(DeploymentKindCatalogResource.class);

    private static final String ENTITY_NAME = "deploymentKindCatalog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeploymentKindCatalogRepository deploymentKindCatalogRepository;

    public DeploymentKindCatalogResource(DeploymentKindCatalogRepository deploymentKindCatalogRepository) {
        this.deploymentKindCatalogRepository = deploymentKindCatalogRepository;
    }

    /**
     * {@code POST  /deployment-kind-catalogs} : Create a new deploymentKindCatalog.
     *
     * @param deploymentKindCatalog the deploymentKindCatalog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deploymentKindCatalog, or with status {@code 400 (Bad Request)} if the deploymentKindCatalog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deployment-kind-catalogs")
    public ResponseEntity<DeploymentKindCatalog> createDeploymentKindCatalog(
        @Valid @RequestBody DeploymentKindCatalog deploymentKindCatalog
    ) throws URISyntaxException {
        log.debug("REST request to save DeploymentKindCatalog : {}", deploymentKindCatalog);
        if (deploymentKindCatalog.getId() != null) {
            throw new BadRequestAlertException("A new deploymentKindCatalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeploymentKindCatalog result = deploymentKindCatalogRepository.save(deploymentKindCatalog);
        return ResponseEntity
            .created(new URI("/api/deployment-kind-catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deployment-kind-catalogs/:id} : Updates an existing deploymentKindCatalog.
     *
     * @param id the id of the deploymentKindCatalog to save.
     * @param deploymentKindCatalog the deploymentKindCatalog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deploymentKindCatalog,
     * or with status {@code 400 (Bad Request)} if the deploymentKindCatalog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deploymentKindCatalog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deployment-kind-catalogs/{id}")
    public ResponseEntity<DeploymentKindCatalog> updateDeploymentKindCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeploymentKindCatalog deploymentKindCatalog
    ) throws URISyntaxException {
        log.debug("REST request to update DeploymentKindCatalog : {}, {}", id, deploymentKindCatalog);
        if (deploymentKindCatalog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deploymentKindCatalog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deploymentKindCatalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeploymentKindCatalog result = deploymentKindCatalogRepository.save(deploymentKindCatalog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deploymentKindCatalog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deployment-kind-catalogs/:id} : Partial updates given fields of an existing deploymentKindCatalog, field will ignore if it is null
     *
     * @param id the id of the deploymentKindCatalog to save.
     * @param deploymentKindCatalog the deploymentKindCatalog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deploymentKindCatalog,
     * or with status {@code 400 (Bad Request)} if the deploymentKindCatalog is not valid,
     * or with status {@code 404 (Not Found)} if the deploymentKindCatalog is not found,
     * or with status {@code 500 (Internal Server Error)} if the deploymentKindCatalog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deployment-kind-catalogs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeploymentKindCatalog> partialUpdateDeploymentKindCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeploymentKindCatalog deploymentKindCatalog
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeploymentKindCatalog partially : {}, {}", id, deploymentKindCatalog);
        if (deploymentKindCatalog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deploymentKindCatalog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deploymentKindCatalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeploymentKindCatalog> result = deploymentKindCatalogRepository
            .findById(deploymentKindCatalog.getId())
            .map(existingDeploymentKindCatalog -> {
                if (deploymentKindCatalog.getDeploymentKind() != null) {
                    existingDeploymentKindCatalog.setDeploymentKind(deploymentKindCatalog.getDeploymentKind());
                }
                if (deploymentKindCatalog.getDeploymentDefinition() != null) {
                    existingDeploymentKindCatalog.setDeploymentDefinition(deploymentKindCatalog.getDeploymentDefinition());
                }

                return existingDeploymentKindCatalog;
            })
            .map(deploymentKindCatalogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deploymentKindCatalog.getId().toString())
        );
    }

    /**
     * {@code GET  /deployment-kind-catalogs} : get all the deploymentKindCatalogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deploymentKindCatalogs in body.
     */
    @GetMapping("/deployment-kind-catalogs")
    public List<DeploymentKindCatalog> getAllDeploymentKindCatalogs() {
        log.debug("REST request to get all DeploymentKindCatalogs");
        return deploymentKindCatalogRepository.findAll();
    }

    /**
     * {@code GET  /deployment-kind-catalogs/:id} : get the "id" deploymentKindCatalog.
     *
     * @param id the id of the deploymentKindCatalog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deploymentKindCatalog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deployment-kind-catalogs/{id}")
    public ResponseEntity<DeploymentKindCatalog> getDeploymentKindCatalog(@PathVariable Long id) {
        log.debug("REST request to get DeploymentKindCatalog : {}", id);
        Optional<DeploymentKindCatalog> deploymentKindCatalog = deploymentKindCatalogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deploymentKindCatalog);
    }

    /**
     * {@code DELETE  /deployment-kind-catalogs/:id} : delete the "id" deploymentKindCatalog.
     *
     * @param id the id of the deploymentKindCatalog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deployment-kind-catalogs/{id}")
    public ResponseEntity<Void> deleteDeploymentKindCatalog(@PathVariable Long id) {
        log.debug("REST request to delete DeploymentKindCatalog : {}", id);
        deploymentKindCatalogRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
