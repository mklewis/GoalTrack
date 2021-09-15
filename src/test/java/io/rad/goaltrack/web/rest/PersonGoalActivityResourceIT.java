package io.rad.goaltrack.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.rad.goaltrack.IntegrationTest;
import io.rad.goaltrack.domain.PersonGoalActivity;
import io.rad.goaltrack.domain.enumeration.Status;
import io.rad.goaltrack.repository.PersonGoalActivityRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link PersonGoalActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonGoalActivityResourceIT {

    private static final Instant DEFAULT_TARGET_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TARGET_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.STARTED;

    private static final String ENTITY_API_URL = "/api/person-goal-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PersonGoalActivityRepository personGoalActivityRepository;

    @Autowired
    private MockMvc restPersonGoalActivityMockMvc;

    private PersonGoalActivity personGoalActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonGoalActivity createEntity() {
        PersonGoalActivity personGoalActivity = new PersonGoalActivity()
            .targetDate(DEFAULT_TARGET_DATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS);
        return personGoalActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonGoalActivity createUpdatedEntity() {
        PersonGoalActivity personGoalActivity = new PersonGoalActivity()
            .targetDate(UPDATED_TARGET_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);
        return personGoalActivity;
    }

    @BeforeEach
    public void initTest() {
        personGoalActivityRepository.deleteAll();
        personGoalActivity = createEntity();
    }

    @Test
    void createPersonGoalActivity() throws Exception {
        int databaseSizeBeforeCreate = personGoalActivityRepository.findAll().size();
        // Create the PersonGoalActivity
        restPersonGoalActivityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isCreated());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeCreate + 1);
        PersonGoalActivity testPersonGoalActivity = personGoalActivityList.get(personGoalActivityList.size() - 1);
        assertThat(testPersonGoalActivity.getTargetDate()).isEqualTo(DEFAULT_TARGET_DATE);
        assertThat(testPersonGoalActivity.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPersonGoalActivity.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPersonGoalActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createPersonGoalActivityWithExistingId() throws Exception {
        // Create the PersonGoalActivity with an existing ID
        personGoalActivity.setId("existing_id");

        int databaseSizeBeforeCreate = personGoalActivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonGoalActivityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPersonGoalActivities() throws Exception {
        // Initialize the database
        personGoalActivityRepository.save(personGoalActivity);

        // Get all the personGoalActivityList
        restPersonGoalActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personGoalActivity.getId())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    void getPersonGoalActivity() throws Exception {
        // Initialize the database
        personGoalActivityRepository.save(personGoalActivity);

        // Get the personGoalActivity
        restPersonGoalActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, personGoalActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personGoalActivity.getId()))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingPersonGoalActivity() throws Exception {
        // Get the personGoalActivity
        restPersonGoalActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewPersonGoalActivity() throws Exception {
        // Initialize the database
        personGoalActivityRepository.save(personGoalActivity);

        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();

        // Update the personGoalActivity
        PersonGoalActivity updatedPersonGoalActivity = personGoalActivityRepository.findById(personGoalActivity.getId()).get();
        updatedPersonGoalActivity
            .targetDate(UPDATED_TARGET_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restPersonGoalActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonGoalActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonGoalActivity))
            )
            .andExpect(status().isOk());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
        PersonGoalActivity testPersonGoalActivity = personGoalActivityList.get(personGoalActivityList.size() - 1);
        assertThat(testPersonGoalActivity.getTargetDate()).isEqualTo(UPDATED_TARGET_DATE);
        assertThat(testPersonGoalActivity.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPersonGoalActivity.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPersonGoalActivity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingPersonGoalActivity() throws Exception {
        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();
        personGoalActivity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonGoalActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personGoalActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPersonGoalActivity() throws Exception {
        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();
        personGoalActivity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonGoalActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPersonGoalActivity() throws Exception {
        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();
        personGoalActivity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonGoalActivityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePersonGoalActivityWithPatch() throws Exception {
        // Initialize the database
        personGoalActivityRepository.save(personGoalActivity);

        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();

        // Update the personGoalActivity using partial update
        PersonGoalActivity partialUpdatedPersonGoalActivity = new PersonGoalActivity();
        partialUpdatedPersonGoalActivity.setId(personGoalActivity.getId());

        partialUpdatedPersonGoalActivity.status(UPDATED_STATUS);

        restPersonGoalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonGoalActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonGoalActivity))
            )
            .andExpect(status().isOk());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
        PersonGoalActivity testPersonGoalActivity = personGoalActivityList.get(personGoalActivityList.size() - 1);
        assertThat(testPersonGoalActivity.getTargetDate()).isEqualTo(DEFAULT_TARGET_DATE);
        assertThat(testPersonGoalActivity.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPersonGoalActivity.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPersonGoalActivity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdatePersonGoalActivityWithPatch() throws Exception {
        // Initialize the database
        personGoalActivityRepository.save(personGoalActivity);

        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();

        // Update the personGoalActivity using partial update
        PersonGoalActivity partialUpdatedPersonGoalActivity = new PersonGoalActivity();
        partialUpdatedPersonGoalActivity.setId(personGoalActivity.getId());

        partialUpdatedPersonGoalActivity
            .targetDate(UPDATED_TARGET_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restPersonGoalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonGoalActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonGoalActivity))
            )
            .andExpect(status().isOk());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
        PersonGoalActivity testPersonGoalActivity = personGoalActivityList.get(personGoalActivityList.size() - 1);
        assertThat(testPersonGoalActivity.getTargetDate()).isEqualTo(UPDATED_TARGET_DATE);
        assertThat(testPersonGoalActivity.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPersonGoalActivity.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPersonGoalActivity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingPersonGoalActivity() throws Exception {
        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();
        personGoalActivity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonGoalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personGoalActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPersonGoalActivity() throws Exception {
        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();
        personGoalActivity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonGoalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPersonGoalActivity() throws Exception {
        int databaseSizeBeforeUpdate = personGoalActivityRepository.findAll().size();
        personGoalActivity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonGoalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personGoalActivity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonGoalActivity in the database
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePersonGoalActivity() throws Exception {
        // Initialize the database
        personGoalActivityRepository.save(personGoalActivity);

        int databaseSizeBeforeDelete = personGoalActivityRepository.findAll().size();

        // Delete the personGoalActivity
        restPersonGoalActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, personGoalActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonGoalActivity> personGoalActivityList = personGoalActivityRepository.findAll();
        assertThat(personGoalActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
