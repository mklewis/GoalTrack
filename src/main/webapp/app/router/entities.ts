import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Goal = () => import('@/entities/goal/goal.vue');
// prettier-ignore
const GoalUpdate = () => import('@/entities/goal/goal-update.vue');
// prettier-ignore
const GoalDetails = () => import('@/entities/goal/goal-details.vue');
// prettier-ignore
const Person = () => import('@/entities/person/person.vue');
// prettier-ignore
const PersonUpdate = () => import('@/entities/person/person-update.vue');
// prettier-ignore
const PersonDetails = () => import('@/entities/person/person-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/goal',
    name: 'Goal',
    component: Goal,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/goal/new',
    name: 'GoalCreate',
    component: GoalUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/goal/:goalId/edit',
    name: 'GoalEdit',
    component: GoalUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/goal/:goalId/view',
    name: 'GoalView',
    component: GoalDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/person',
    name: 'Person',
    component: Person,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/person/new',
    name: 'PersonCreate',
    component: PersonUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/person/:personId/edit',
    name: 'PersonEdit',
    component: PersonUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/person/:personId/view',
    name: 'PersonView',
    component: PersonDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
