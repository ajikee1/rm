package com.ajith.com.repository;

import com.ajith.com.domain.ReleaseCatalog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReleaseCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseCatalogRepository extends JpaRepository<ReleaseCatalog, Long> {}
