package com.samuelkane.exploreLearningChallenge.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Boolean existsByFirstNameAndLastName(String firstname, String lastname);
}
