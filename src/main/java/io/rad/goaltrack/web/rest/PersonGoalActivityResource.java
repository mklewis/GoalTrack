package io.rad.goaltrack.web.rest;

import io.rad.goaltrack.domain.PersonGoalActivity;
import io.rad.goaltrack.repository.PersonGoalActivityRepository;
import io.rad.goaltrack.service.PersonGoalActivityService;
import io.rad.goaltrack.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.rad.goaltrack.domain.PersonGoalActivity}.
 */
@RestController
@RequestMapping("/api")
public class PersonGoalActivityResource {

    private final Logger log = LoggerFactory.getLogger(PersonGoalActivityResource.class);

    private static final String ENTITY_NAME = "personGoalActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonGoalActivityService personGoalActivityService;

    private final PersonGoalActivityRepository personGoalActivityRepository;

    public PersonGoalActivityResource(
        PersonGoalActivityService personGoalActivityService,
        PersonGoalActivityRepository personGoalActivityRepository
    ) {
        this.personGoalActivityService = personGoalActivityService;
        this.personGoalActivityRepository = personGoalActivityRepository;
    }

    /**
     * {@code POST  /person-goal-activities} : Create a new personGoalActivity.
     *
     * @param personGoalActivity the personGoalActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personGoalActivity, or with status {@code 400 (Bad Request)} if the personGoalActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-goal-activities")
    public ResponseEntity<PersonGoalActivity> createPersonGoalActivity(@RequestBody PersonGoalActivity personGoalActivity)
        throws URISyntaxException {
        log.debug("REST request to save PersonGoalActivity : {}", personGoalActivity);
        if (personGoalActivity.getId() != null) {
            throw new BadRequestAlertException("A new personGoalActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonGoalActivity result = personGoalActivityService.save(personGoalActivity);
        return ResponseEntity
            .created(new URI("/api/person-goal-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /person-goal-activities/:id} : Updates an existing personGoalActivity.
     *
     * @param id the id of the personGoalActivity to save.
     * @param personGoalActivity the personGoalActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personGoalActivity,
     * or with status {@code 400 (Bad Request)} if the personGoalActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personGoalActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-goal-activities/{id}")
    public ResponseEntity<PersonGoalActivity> updatePersonGoalActivity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PersonGoalActivity personGoalActivity
    ) throws URISyntaxException {
        log.debug("REST request to update PersonGoalActivity : {}, {}", id, personGoalActivity);
        if (personGoalActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personGoalActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personGoalActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonGoalActivity result = personGoalActivityService.save(personGoalActivity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personGoalActivity.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-goal-activities/:id} : Partial updates given fields of an existing personGoalActivity, field will ignore if it is null
     *
     * @param id the id of the personGoalActivity to save.
     * @param personGoalActivity the personGoalActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personGoalActivity,
     * or with status {@code 400 (Bad Request)} if the personGoalActivity is not valid,
     * or with status {@code 404 (Not Found)} if the personGoalActivity is not found,
     * or with status {@code 500 (Internal Server Error)} if the personGoalActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-goal-activities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonGoalActivity> partialUpdatePersonGoalActivity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PersonGoalActivity personGoalActivity
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonGoalActivity partially : {}, {}", id, personGoalActivity);
        if (personGoalActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personGoalActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personGoalActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonGoalActivity> result = personGoalActivityService.partialUpdate(personGoalActivity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personGoalActivity.getId())
        );
    }

    /**
     * {@code GET  /person-goal-activities} : get all the personGoalActivities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personGoalActivities in body.
     */
    @GetMapping("/person-goal-activities")
    public List<PersonGoalActivity> getAllPersonGoalActivities() {
        log.debug("REST request to get all PersonGoalActivities");
        return personGoalActivityService.findAll();
    }

    /**
     * {@code GET  /person-goal-activities/:id} : get the "id" personGoalActivity.
     *
     * @param id the id of the personGoalActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personGoalActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-goal-activities/{id}")
    public ResponseEntity<PersonGoalActivity> getPersonGoalActivity(@PathVariable String id) {
        log.debug("REST request to get PersonGoalActivity : {}", id);
        Optional<PersonGoalActivity> personGoalActivity = personGoalActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personGoalActivity);
    }

    /**
     * {@code DELETE  /person-goal-activities/:id} : delete the "id" personGoalActivity.
     *
     * @param id the id of the personGoalActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-goal-activities/{id}")
    public ResponseEntity<Void> deletePersonGoalActivity(@PathVariable String id) {
        log.debug("REST request to delete PersonGoalActivity : {}", id);
        personGoalActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
