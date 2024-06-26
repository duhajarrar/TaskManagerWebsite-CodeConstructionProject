package com.bzu.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.bzu.taskmanager.model.RefreshToken;
import com.bzu.taskmanager.model.User;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);


  @Query("{'user' : ?0 }")
  int deleteByUser(User user);
}
