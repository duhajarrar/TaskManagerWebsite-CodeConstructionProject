package com.bzu.taskmanager.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.bzu.taskmanager.model.ERole;
import com.bzu.taskmanager.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}