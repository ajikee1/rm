package com.ajith.com.repository;

import com.ajith.com.domain.ComponentVersion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComponentVersion entity.
 */
@Repository
public interface ComponentVersionRepository extends JpaRepository<ComponentVersion, Long> {
    default Optional<ComponentVersion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ComponentVersion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ComponentVersion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct componentVersion from ComponentVersion componentVersion left join fetch componentVersion.componentId",
        countQuery = "select count(distinct componentVersion) from ComponentVersion componentVersion"
    )
    Page<ComponentVersion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct componentVersion from ComponentVersion componentVersion left join fetch componentVersion.componentId")
    List<ComponentVersion> findAllWithToOneRelationships();

    @Query(
        "select componentVersion from ComponentVersion componentVersion left join fetch componentVersion.componentId where componentVersion.id =:id"
    )
    Optional<ComponentVersion> findOneWithToOneRelationships(@Param("id") Long id);
}
