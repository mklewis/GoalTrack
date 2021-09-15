package io.rad.goaltrack.service;

import io.rad.goaltrack.domain.PersonGoalActivity;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PersonGoalActivity}.
 */
public interface PersonGoalActivityService {
    /**
     * Save a personGoalActivity.
     *
     * @param personGoalActivity the entity to save.
     * @return the persisted entity.
     */
    PersonGoalActivity save(PersonGoalActivity personGoalActivity);

    /**
     * Partially updates a personGoalActivity.
     *
     * @param personGoalActivity the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonGoalActivity> partialUpdate(PersonGoalActivity personGoalActivity);

    /**
     * Get all the personGoalActivities.
     *
     * @return the list of entities.
     */
    List<PersonGoalActivity> findAll();

    /**
     * Get the "id" personGoalActivity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonGoalActivity> findOne(String id);

    /**
     * Delete the "id" personGoalActivity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
