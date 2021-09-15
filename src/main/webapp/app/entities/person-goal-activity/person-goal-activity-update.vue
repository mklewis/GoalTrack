<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="goalTrackerApp.personGoalActivity.home.createOrEditLabel"
          data-cy="PersonGoalActivityCreateUpdateHeading"
          v-text="$t('goalTrackerApp.personGoalActivity.home.createOrEditLabel')"
        >
          Create or edit a PersonGoalActivity
        </h2>
        <div>
          <div class="form-group" v-if="personGoalActivity.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="personGoalActivity.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('goalTrackerApp.personGoalActivity.targetDate')"
              for="person-goal-activity-targetDate"
              >Target Date</label
            >
            <div class="d-flex">
              <input
                id="person-goal-activity-targetDate"
                data-cy="targetDate"
                type="datetime-local"
                class="form-control"
                name="targetDate"
                :class="{ valid: !$v.personGoalActivity.targetDate.$invalid, invalid: $v.personGoalActivity.targetDate.$invalid }"
                :value="convertDateTimeFromServer($v.personGoalActivity.targetDate.$model)"
                @change="updateInstantField('targetDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('goalTrackerApp.personGoalActivity.startDate')"
              for="person-goal-activity-startDate"
              >Start Date</label
            >
            <div class="d-flex">
              <input
                id="person-goal-activity-startDate"
                data-cy="startDate"
                type="datetime-local"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.personGoalActivity.startDate.$invalid, invalid: $v.personGoalActivity.startDate.$invalid }"
                :value="convertDateTimeFromServer($v.personGoalActivity.startDate.$model)"
                @change="updateInstantField('startDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.personGoalActivity.endDate')" for="person-goal-activity-endDate"
              >End Date</label
            >
            <div class="d-flex">
              <input
                id="person-goal-activity-endDate"
                data-cy="endDate"
                type="datetime-local"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.personGoalActivity.endDate.$invalid, invalid: $v.personGoalActivity.endDate.$invalid }"
                :value="convertDateTimeFromServer($v.personGoalActivity.endDate.$model)"
                @change="updateInstantField('endDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.personGoalActivity.status')" for="person-goal-activity-status"
              >Status</label
            >
            <select
              class="form-control"
              name="status"
              :class="{ valid: !$v.personGoalActivity.status.$invalid, invalid: $v.personGoalActivity.status.$invalid }"
              v-model="$v.personGoalActivity.status.$model"
              id="person-goal-activity-status"
              data-cy="status"
            >
              <option value="CREATED" v-bind:label="$t('goalTrackerApp.Status.CREATED')">CREATED</option>
              <option value="STARTED" v-bind:label="$t('goalTrackerApp.Status.STARTED')">STARTED</option>
              <option value="STOPPED" v-bind:label="$t('goalTrackerApp.Status.STOPPED')">STOPPED</option>
              <option value="COMPLETED" v-bind:label="$t('goalTrackerApp.Status.COMPLETED')">COMPLETED</option>
              <option value="CANCELED" v-bind:label="$t('goalTrackerApp.Status.CANCELED')">CANCELED</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.personGoalActivity.goal')" for="person-goal-activity-goal"
              >Goal</label
            >
            <select class="form-control" id="person-goal-activity-goal" data-cy="goal" name="goal" v-model="personGoalActivity.goal">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  personGoalActivity.goal && goalOption.id === personGoalActivity.goal.id ? personGoalActivity.goal : goalOption
                "
                v-for="goalOption in goals"
                :key="goalOption.id"
              >
                {{ goalOption.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.personGoalActivity.person')" for="person-goal-activity-person"
              >Person</label
            >
            <select
              class="form-control"
              id="person-goal-activity-person"
              data-cy="person"
              name="person"
              v-model="personGoalActivity.person"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  personGoalActivity.person && personOption.id === personGoalActivity.person.id ? personGoalActivity.person : personOption
                "
                v-for="personOption in people"
                :key="personOption.id"
              >
                {{ personOption.email }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.personGoalActivity.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./person-goal-activity-update.component.ts"></script>
