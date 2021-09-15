<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="goalTrackerApp.goal.home.createOrEditLabel"
          data-cy="GoalCreateUpdateHeading"
          v-text="$t('goalTrackerApp.goal.home.createOrEditLabel')"
        >
          Create or edit a Goal
        </h2>
        <div>
          <div class="form-group" v-if="goal.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="goal.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.goal.title')" for="goal-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="goal-title"
              data-cy="title"
              :class="{ valid: !$v.goal.title.$invalid, invalid: $v.goal.title.$invalid }"
              v-model="$v.goal.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.goal.description')" for="goal-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="goal-description"
              data-cy="description"
              :class="{ valid: !$v.goal.description.$invalid, invalid: $v.goal.description.$invalid }"
              v-model="$v.goal.description.$model"
            />
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
            :disabled="$v.goal.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./goal-update.component.ts"></script>
