package com.ajith.com.repository;

import com.ajith.com.domain.DeploymentKindCatalog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeploymentKindCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeploymentKindCatalogRepository extends JpaRepository<DeploymentKindCatalog, Long> {}
