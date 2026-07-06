package com.yvonne.onakawash.repository;

import com.yvonne.onakawash.entity.TangoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TangoItemRepository extends JpaRepository<TangoItemEntity, String> {
    @Query("select count(t) from TangoItemEntity t join t.coveredKanaItemIds coveredKanaItemId where coveredKanaItemId = :kanaItemId")
    long countByCoveredKanaItemId(@Param("kanaItemId") String kanaItemId);
}