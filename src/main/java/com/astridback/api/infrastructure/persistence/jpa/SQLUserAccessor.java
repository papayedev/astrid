package com.astridback.api.infrastructure.persistence.jpa;

import com.astridback.api.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLUserAccessor extends CrudRepository<UserEntity, String> {
    UserEntity findByEmailAddress(String emailAddress);
}