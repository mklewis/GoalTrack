<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="person">
        <h2 class="jh-entity-heading" data-cy="personDetailsHeading">
          <span v-text="$t('goalTrackerApp.person.detail.title')">Person</span> {{ person.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('goalTrackerApp.person.firstName')">First Name</span>
          </dt>
          <dd>
            <span>{{ person.firstName }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.person.lastName')">Last Name</span>
          </dt>
          <dd>
            <span>{{ person.lastName }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.person.email')">Email</span>
          </dt>
          <dd>
            <span>{{ person.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.person.phoneNumber')">Phone Number</span>
          </dt>
          <dd>
            <span>{{ person.phoneNumber }}</span>
          </dd>
          <dt>
            <span v-text="$t('goalTrackerApp.person.goal')">Goal</span>
          </dt>
          <dd>
            <span v-for="(goal, i) in person.goals" :key="goal.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'GoalView', params: { goalId: goal.id } }">{{ goal.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="person.id" :to="{ name: 'PersonEdit', params: { personId: person.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./person-details.component.ts"></script>
