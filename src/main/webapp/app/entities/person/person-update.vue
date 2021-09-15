<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="goalTrackerApp.person.home.createOrEditLabel"
          data-cy="PersonCreateUpdateHeading"
          v-text="$t('goalTrackerApp.person.home.createOrEditLabel')"
        >
          Create or edit a Person
        </h2>
        <div>
          <div class="form-group" v-if="person.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="person.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.person.firstName')" for="person-firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              name="firstName"
              id="person-firstName"
              data-cy="firstName"
              :class="{ valid: !$v.person.firstName.$invalid, invalid: $v.person.firstName.$invalid }"
              v-model="$v.person.firstName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.person.lastName')" for="person-lastName">Last Name</label>
            <input
              type="text"
              class="form-control"
              name="lastName"
              id="person-lastName"
              data-cy="lastName"
              :class="{ valid: !$v.person.lastName.$invalid, invalid: $v.person.lastName.$invalid }"
              v-model="$v.person.lastName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.person.email')" for="person-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="person-email"
              data-cy="email"
              :class="{ valid: !$v.person.email.$invalid, invalid: $v.person.email.$invalid }"
              v-model="$v.person.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('goalTrackerApp.person.phoneNumber')" for="person-phoneNumber">Phone Number</label>
            <input
              type="text"
              class="form-control"
              name="phoneNumber"
              id="person-phoneNumber"
              data-cy="phoneNumber"
              :class="{ valid: !$v.person.phoneNumber.$invalid, invalid: $v.person.phoneNumber.$invalid }"
              v-model="$v.person.phoneNumber.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('goalTrackerApp.person.goal')" for="person-goal">Goal</label>
            <select
              class="form-control"
              id="person-goal"
              data-cy="goal"
              multiple
              name="goal"
              v-if="person.goals !== undefined"
              v-model="person.goals"
            >
              <option v-bind:value="getSelected(person.goals, goalOption)" v-for="goalOption in goals" :key="goalOption.id">
                {{ goalOption.id }}
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
            :disabled="$v.person.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./person-update.component.ts"></script>
