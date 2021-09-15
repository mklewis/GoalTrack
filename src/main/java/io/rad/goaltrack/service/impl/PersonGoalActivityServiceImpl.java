package io.rad.goaltrack.service.impl;

import io.rad.goaltrack.domain.PersonGoalActivity;
import io.rad.goaltrack.repository.PersonGoalActivityRepository;
import io.rad.goaltrack.service.PersonGoalActivityService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link PersonGoalActivity}.
 */
@Service
public class PersonGoalActivityServiceImpl implements PersonGoalActivityService {

    private final Logger log = LoggerFactory.getLogger(PersonGoalActivityServiceImpl.class);

    private final PersonGoalActivityRepository personGoalActivityRepository;

    public PersonGoalActivityServiceImpl(PersonGoalActivityRepository personGoalActivityRepository) {
        this.personGoalActivityRepository = personGoalActivityRepository;
    }

    @Override
    public PersonGoalActivity save(PersonGoalActivity personGoalActivity) {
        log.debug("Request to save PersonGoalActivity : {}", personGoalActivity);
        return personGoalActivityRepository.save(personGoalActivity);
    }

    @Override
    public Optional<PersonGoalActivity> partialUpdate(PersonGoalActivity personGoalActivity) {
        log.debug("Request to partially update PersonGoalActivity : {}", personGoalActivity);

        return personGoalActivityRepository
            .findById(personGoalActivity.getId())
            .map(
                existingPersonGoalActivity -> {
                    if (personGoalActivity.getTargetDate() != null) {
                        existingPersonGoalActivity.setTargetDate(personGoalActivity.getTargetDate());
                    }
                    if (personGoalActivity.getStartDate() != null) {
                        existingPersonGoalActivity.setStartDate(personGoalActivity.getStartDate());
                    }
                    if (personGoalActivity.getEndDate() != null) {
                        existingPersonGoalActivity.setEndDate(personGoalActivity.getEndDate());
                    }
                    if (personGoalActivity.getStatus() != null) {
                        existingPersonGoalActivity.setStatus(personGoalActivity.getStatus());
                    }

                    return existingPersonGoalActivity;
                }
            )
            .map(personGoalActivityRepository::save);
    }

    @Override
    public List<PersonGoalActivity> findAll() {
        log.debug("Request to get all PersonGoalActivities");
        return personGoalActivityRepository.findAll();
    }

    @Override
    public Optional<PersonGoalActivity> findOne(String id) {
        log.debug("Request to get PersonGoalActivity : {}", id);
        return personGoalActivityRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete PersonGoalActivity : {}", id);
        personGoalActivityRepository.deleteById(id);
    }
}
