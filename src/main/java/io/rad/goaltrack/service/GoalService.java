package io.rad.goaltrack.service;

import io.rad.goaltrack.domain.Goal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Goal}.
 */
public interface GoalService {
    /**
     * Save a goal.
     *
     * @param goal the entity to save.
     * @return the persisted entity.
     */
    Goal save(Goal goal);

    /**
     * Partially updates a goal.
     *
     * @param goal the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Goal> partialUpdate(Goal goal);

    /**
     * Get all the goals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Goal> findAll(Pageable pageable);

    /**
     * Get the "id" goal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Goal> findOne(String id);

    /**
     * Delete the "id" goal.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
