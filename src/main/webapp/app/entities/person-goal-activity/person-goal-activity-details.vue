<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="personGoalActivity">
        <h2 class="jh-entity-heading" data-cy="personGoalActivityDetailsHeading">
          <span v-text="$t('goalTrackerApp.personGoalActivity.detail.title')">PersonGoalActivity</span> {{ personGoalActivity.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('goalTrackerApp.personGoalActivity.targetDate')">Target Date</span>
          </dt>
          <dd>
            <span v-if="personGoalActivity.targetDate">{{ $d(Date.parse(personGoalActivity.targetDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.personGoalActivity.startDate')">Start Date</span>
          </dt>
          <dd>
            <span v-if="personGoalActivity.startDate">{{ $d(Date.parse(personGoalActivity.startDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.personGoalActivity.endDate')">End Date</span>
          </dt>
          <dd>
            <span v-if="personGoalActivity.endDate">{{ $d(Date.parse(personGoalActivity.endDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.personGoalActivity.status')">Status</span>
          </dt>
          <dd>
            <span v-text="$t('goalTrackerApp.Status.' + personGoalActivity.status)">{{ personGoalActivity.status }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.personGoalActivity.goal')">Goal</span>
          </dt>
          <dd>
            <div v-if="personGoalActivity.goal">
              <router-link :to="{ name: 'GoalView', params: { goalId: personGoalActivity.goal.id } }">{{
                personGoalActivity.goal.title
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.personGoalActivity.person')">Person</span>
          </dt>
          <dd>
            <div v-if="personGoalActivity.person">
              <router-link :to="{ name: 'PersonView', params: { personId: personGoalActivity.person.id } }">{{
                personGoalActivity.person.email
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="personGoalActivity.id"
          :to="{ name: 'PersonGoalActivityEdit', params: { personGoalActivityId: personGoalActivity.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./person-goal-activity-details.component.ts"></script>
