<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="goal">
        <h2 class="jh-entity-heading" data-cy="goalDetailsHeading">
          <span v-text="$t('goalTrackerApp.goal.detail.title')">Goal</span> {{ goal.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('goalTrackerApp.goal.title')">Title</span>
          </dt>
          <dd>
            <span>{{ goal.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.goal.description')">Description</span>
          </dt>
          <dd>
            <span>{{ goal.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.goal.targetDate')">Target Date</span>
          </dt>
          <dd>
            <span v-if="goal.targetDate">{{ $d(Date.parse(goal.targetDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.goal.startDate')">Start Date</span>
          </dt>
          <dd>
            <span v-if="goal.startDate">{{ $d(Date.parse(goal.startDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.goal.endDate')">End Date</span>
          </dt>
          <dd>
            <span v-if="goal.endDate">{{ $d(Date.parse(goal.endDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.goal.status')">Status</span>
          </dt>
          <dd>
            <span v-text="$t('goalTrackerApp.Status.' + goal.status)">{{ goal.status }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="goal.id" :to="{ name: 'GoalEdit', params: { goalId: goal.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./goal-details.component.ts"></script>
