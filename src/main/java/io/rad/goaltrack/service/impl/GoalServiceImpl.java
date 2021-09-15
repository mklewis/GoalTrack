package io.rad.goaltrack.service.impl;

import io.rad.goaltrack.domain.Goal;
import io.rad.goaltrack.repository.GoalRepository;
import io.rad.goaltrack.service.GoalService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Goal}.
 */
@Service
public class GoalServiceImpl implements GoalService {

    private final Logger log = LoggerFactory.getLogger(GoalServiceImpl.class);

    private final GoalRepository goalRepository;

    public GoalServiceImpl(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public Goal save(Goal goal) {
        log.debug("Request to save Goal : {}", goal);
        return goalRepository.save(goal);
    }

    @Override
    public Optional<Goal> partialUpdate(Goal goal) {
        log.debug("Request to partially update Goal : {}", goal);

        return goalRepository
            .findById(goal.getId())
            .map(
                existingGoal -> {
                    if (goal.getTitle() != null) {
                        existingGoal.setTitle(goal.getTitle());
                    }
                    if (goal.getDescription() != null) {
                        existingGoal.setDescription(goal.getDescription());
                    }
                    if (goal.getTargetDate() != null) {
                        existingGoal.setTargetDate(goal.getTargetDate());
                    }
                    if (goal.getStartDate() != null) {
                        existingGoal.setStartDate(goal.getStartDate());
                    }
                    if (goal.getEndDate() != null) {
                        existingGoal.setEndDate(goal.getEndDate());
                    }
                    if (goal.getStatus() != null) {
                        existingGoal.setStatus(goal.getStatus());
                    }

                    return existingGoal;
                }
            )
            .map(goalRepository::save);
    }

    @Override
    public Page<Goal> findAll(Pageable pageable) {
        log.debug("Request to get all Goals");
        return goalRepository.findAll(pageable);
    }

    @Override
    public Optional<Goal> findOne(String id) {
        log.debug("Request to get Goal : {}", id);
        return goalRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Goal : {}", id);
        goalRepository.deleteById(id);
    }
}
