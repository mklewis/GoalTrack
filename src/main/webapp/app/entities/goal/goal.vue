<template>
  <div>
    <h2 id="page-heading" data-cy="GoalHeading">
      <span v-text="$t('goalTrackerApp.goal.home.title')" id="goal-heading">Goals</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('goalTrackerApp.goal.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'GoalCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-goal">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('goalTrackerApp.goal.home.createLabel')"> Create a new Goal </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && goals && goals.length === 0">
      <span v-text="$t('goalTrackerApp.goal.home.notFound')">No goals found</span>
    </div>
    <div class="table-responsive" v-if="goals && goals.length > 0">
      <table class="table table-striped" aria-describedby="goals">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="$t('goalTrackerApp.goal.title')">Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('goalTrackerApp.goal.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('targetDate')">
              <span v-text="$t('goalTrackerApp.goal.targetDate')">Target Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('startDate')">
              <span v-text="$t('goalTrackerApp.goal.startDate')">Start Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('endDate')">
              <span v-text="$t('goalTrackerApp.goal.endDate')">End Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('status')">
              <span v-text="$t('goalTrackerApp.goal.status')">Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="goal in goals" :key="goal.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'GoalView', params: { goalId: goal.id } }">{{ goal.id }}</router-link>
            </td>
            <td>{{ goal.title }}</td>
            <td>{{ goal.description }}</td>
            <td>{{ goal.targetDate ? $d(Date.parse(goal.targetDate), 'short') : '' }}</td>
            <td>{{ goal.startDate ? $d(Date.parse(goal.startDate), 'short') : '' }}</td>
            <td>{{ goal.endDate ? $d(Date.parse(goal.endDate), 'short') : '' }}</td>
            <td v-text="$t('goalTrackerApp.Status.' + goal.status)">{{ goal.status }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'GoalView', params: { goalId: goal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'GoalEdit', params: { goalId: goal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(goal)"
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
        <infinite-loading
          ref="infiniteLoading"
          v-if="totalItems > itemsPerPage"
          :identifier="infiniteId"
          slot="append"
          @infinite="loadMore"
          force-use-infinite-wrapper=".el-table__body-wrapper"
          :distance="20"
        >
        </infinite-loading>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="goalTrackerApp.goal.delete.question" data-cy="goalDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-goal-heading" v-text="$t('goalTrackerApp.goal.delete.question', { id: removeId })">
          Are you sure you want to delete this Goal?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-goal"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeGoal()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./goal.component.ts"></script>
