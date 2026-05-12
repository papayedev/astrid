package com.astridback.api.infrastructure.persistence.jpa;

import com.astridback.api.application.ports.PositionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SQLPositionRepository implements PositionRepository {
    private SQLPositionAccessor accessor;

    public SQLPositionRepository(SQLPositionAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public void clear() {
        accessor.deleteAll();
    }
}
