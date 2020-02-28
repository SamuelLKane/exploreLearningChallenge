package com.samuelkane.exploreLearningChallenge.domain;

import com.samuelkane.exploreLearningChallenge.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
