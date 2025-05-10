package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.bizum.BizumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BizumRepository extends JpaRepository<BizumEntity, Integer> {
}
