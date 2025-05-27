package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.bizum.BizumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BizumRepository extends JpaRepository<BizumEntity, Integer> {
    @Query("SELECT b from f_bizum b where b.from.id = ?1 OR b.to.id = ?1")
    List<BizumEntity> getBizums(long id);

    @Query("SELECT b from f_bizum b WHERE (b.from.id = ?1 OR b.to.id = ?1) AND b.isRequesting = false")
    List<BizumEntity> getNonRequestedBizums(long id);

    @Query("SELECT b from f_bizum b WHERE b.from.id = ?1 AND b.isRequesting = true")
    List<BizumEntity> getRequestedBizums(long id);

    @Query(value = """
    SELECT * FROM f_bizum b
    WHERE b._from = :fromId
      AND b._to IN (
          SELECT b2._to
          FROM f_bizum b2
          WHERE b2._from = :fromId
          GROUP BY b2._to
          HAVING COUNT(*) >= 3
      )
    """, nativeQuery = true)
    List<BizumEntity> getMoreThan3Bizums(@Param("fromId") long fromId);
}
