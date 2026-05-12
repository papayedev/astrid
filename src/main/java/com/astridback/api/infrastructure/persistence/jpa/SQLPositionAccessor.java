package com.astridback.api.infrastructure.persistence.jpa;

import com.astridback.api.infrastructure.persistence.entity.PositionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLPositionAccessor extends CrudRepository<PositionEntity, String> {

}
