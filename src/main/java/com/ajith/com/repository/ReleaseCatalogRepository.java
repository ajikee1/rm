package com.ajith.com.repository;

import com.ajith.com.domain.ReleaseCatalog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReleaseCatalog entity.
 */
@Repository
public interface ReleaseCatalogRepository extends JpaRepository<ReleaseCatalog, Long> {
    default Optional<ReleaseCatalog> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReleaseCatalog> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReleaseCatalog> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct releaseCatalog from ReleaseCatalog releaseCatalog left join fetch releaseCatalog.componentId left join fetch releaseCatalog.componentVersionNumber",
        countQuery = "select count(distinct releaseCatalog) from ReleaseCatalog releaseCatalog"
    )
    Page<ReleaseCatalog> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct releaseCatalog from ReleaseCatalog releaseCatalog left join fetch releaseCatalog.componentId left join fetch releaseCatalog.componentVersionNumber"
    )
    List<ReleaseCatalog> findAllWithToOneRelationships();

    @Query(
        "select releaseCatalog from ReleaseCatalog releaseCatalog left join fetch releaseCatalog.componentId left join fetch releaseCatalog.componentVersionNumber where releaseCatalog.id =:id"
    )
    Optional<ReleaseCatalog> findOneWithToOneRelationships(@Param("id") Long id);
}
