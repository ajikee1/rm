package com.ajith.com.repository;

import com.ajith.com.domain.ComponentSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComponentSet entity.
 */
@Repository
public interface ComponentSetRepository extends JpaRepository<ComponentSet, Long> {
    default Optional<ComponentSet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ComponentSet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ComponentSet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct componentSet from ComponentSet componentSet left join fetch componentSet.componentId",
        countQuery = "select count(distinct componentSet) from ComponentSet componentSet"
    )
    Page<ComponentSet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct componentSet from ComponentSet componentSet left join fetch componentSet.componentId")
    List<ComponentSet> findAllWithToOneRelationships();

    @Query("select componentSet from ComponentSet componentSet left join fetch componentSet.componentId where componentSet.id =:id")
    Optional<ComponentSet> findOneWithToOneRelationships(@Param("id") Long id);
}
