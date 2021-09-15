package io.rad.goaltrack.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.rad.goaltrack.IntegrationTest;
import io.rad.goaltrack.domain.Goal;
import io.rad.goaltrack.repository.GoalRepository;
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
 * Integration tests for the {@link GoalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GoalResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/goals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private MockMvc restGoalMockMvc;

    private Goal goal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createEntity() {
        Goal goal = new Goal().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return goal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createUpdatedEntity() {
        Goal goal = new Goal().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return goal;
    }

    @BeforeEach
    public void initTest() {
        goalRepository.deleteAll();
        goal = createEntity();
    }

    @Test
    void createGoal() throws Exception {
        int databaseSizeBeforeCreate = goalRepository.findAll().size();
        // Create the Goal
        restGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isCreated());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeCreate + 1);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGoal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createGoalWithExistingId() throws Exception {
        // Create the Goal with an existing ID
        goal.setId("existing_id");

        int databaseSizeBeforeCreate = goalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllGoals() throws Exception {
        // Initialize the database
        goalRepository.save(goal);

        // Get all the goalList
        restGoalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goal.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    void getGoal() throws Exception {
        // Initialize the database
        goalRepository.save(goal);

        // Get the goal
        restGoalMockMvc
            .perform(get(ENTITY_API_URL_ID, goal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(goal.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingGoal() throws Exception {
        // Get the goal
        restGoalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewGoal() throws Exception {
        // Initialize the database
        goalRepository.save(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal
        Goal updatedGoal = goalRepository.findById(goal.getId()).get();
        updatedGoal.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGoal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGoal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, goal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGoalWithPatch() throws Exception {
        // Initialize the database
        goalRepository.save(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal using partial update
        Goal partialUpdatedGoal = new Goal();
        partialUpdatedGoal.setId(goal.getId());

        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGoal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateGoalWithPatch() throws Exception {
        // Initialize the database
        goalRepository.save(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal using partial update
        Goal partialUpdatedGoal = new Goal();
        partialUpdatedGoal.setId(goal.getId());

        partialUpdatedGoal.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGoal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, goal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGoal() throws Exception {
        // Initialize the database
        goalRepository.save(goal);

        int databaseSizeBeforeDelete = goalRepository.findAll().size();

        // Delete the goal
        restGoalMockMvc
            .perform(delete(ENTITY_API_URL_ID, goal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
