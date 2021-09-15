<template>
  <div>
    <h2 id="page-heading" data-cy="PersonGoalActivityHeading">
      <span v-text="$t('goalTrackerApp.personGoalActivity.home.title')" id="person-goal-activity-heading">Person Goal Activities</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('goalTrackerApp.personGoalActivity.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PersonGoalActivityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-person-goal-activity"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('goalTrackerApp.personGoalActivity.home.createLabel')"> Create a new Person Goal Activity </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && personGoalActivities && personGoalActivities.length === 0">
      <span v-text="$t('goalTrackerApp.personGoalActivity.home.notFound')">No personGoalActivities found</span>
    </div>
    <div class="table-responsive" v-if="personGoalActivities && personGoalActivities.length > 0">
      <table class="table table-striped" aria-describedby="personGoalActivities">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('goalTrackerApp.personGoalActivity.targetDate')">Target Date</span></th>
            <th scope="row"><span v-text="$t('goalTrackerApp.personGoalActivity.startDate')">Start Date</span></th>
            <th scope="row"><span v-text="$t('goalTrackerApp.personGoalActivity.endDate')">End Date</span></th>
            <th scope="row"><span v-text="$t('goalTrackerApp.personGoalActivity.status')">Status</span></th>
            <th scope="row"><span v-text="$t('goalTrackerApp.personGoalActivity.goal')">Goal</span></th>
            <th scope="row"><span v-text="$t('goalTrackerApp.personGoalActivity.person')">Person</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="personGoalActivity in personGoalActivities" :key="personGoalActivity.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PersonGoalActivityView', params: { personGoalActivityId: personGoalActivity.id } }">{{
                personGoalActivity.id
              }}</router-link>
            </td>
            <td>{{ personGoalActivity.targetDate ? $d(Date.parse(personGoalActivity.targetDate), 'short') : '' }}</td>
            <td>{{ personGoalActivity.startDate ? $d(Date.parse(personGoalActivity.startDate), 'short') : '' }}</td>
            <td>{{ personGoalActivity.endDate ? $d(Date.parse(personGoalActivity.endDate), 'short') : '' }}</td>
            <td v-text="$t('goalTrackerApp.Status.' + personGoalActivity.status)">{{ personGoalActivity.status }}</td>
            <td>
              <div v-if="personGoalActivity.goal">
                <router-link :to="{ name: 'GoalView', params: { goalId: personGoalActivity.goal.id } }">{{
                  personGoalActivity.goal.title
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="personGoalActivity.person">
                <router-link :to="{ name: 'PersonView', params: { personId: personGoalActivity.person.id } }">{{
                  personGoalActivity.person.email
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PersonGoalActivityView', params: { personGoalActivityId: personGoalActivity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PersonGoalActivityEdit', params: { personGoalActivityId: personGoalActivity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(personGoalActivity)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="goalTrackerApp.personGoalActivity.delete.question"
          data-cy="personGoalActivityDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-personGoalActivity-heading" v-text="$t('goalTrackerApp.personGoalActivity.delete.question', { id: removeId })">
          Are you sure you want to delete this Person Goal Activity?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-personGoalActivity"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePersonGoalActivity()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./person-goal-activity.component.ts"></script>
