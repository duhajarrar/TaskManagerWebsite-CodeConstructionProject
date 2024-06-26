package com.bzu.taskmanager.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bzu.taskmanager.model.User;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);
  Optional<User> findById(String userId);
  List<User> findAll();
  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}