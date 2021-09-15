package io.rad.goaltrack.repository;

import io.rad.goaltrack.domain.PersonGoalActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PersonGoalActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonGoalActivityRepository extends MongoRepository<PersonGoalActivity, String> {}
