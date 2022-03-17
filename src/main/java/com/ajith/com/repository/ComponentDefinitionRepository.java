package com.ajith.com.repository;

import com.ajith.com.domain.ComponentDefinition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComponentDefinition entity.
 */
@Repository
public interface ComponentDefinitionRepository extends JpaRepository<ComponentDefinition, Long> {
    default Optional<ComponentDefinition> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ComponentDefinition> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ComponentDefinition> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct componentDefinition from ComponentDefinition componentDefinition left join fetch componentDefinition.deploymentKindCatalog",
        countQuery = "select count(distinct componentDefinition) from ComponentDefinition componentDefinition"
    )
    Page<ComponentDefinition> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct componentDefinition from ComponentDefinition componentDefinition left join fetch componentDefinition.deploymentKindCatalog"
    )
    List<ComponentDefinition> findAllWithToOneRelationships();

    @Query(
        "select componentDefinition from ComponentDefinition componentDefinition left join fetch componentDefinition.deploymentKindCatalog where componentDefinition.id =:id"
    )
    Optional<ComponentDefinition> findOneWithToOneRelationships(@Param("id") Long id);
}
