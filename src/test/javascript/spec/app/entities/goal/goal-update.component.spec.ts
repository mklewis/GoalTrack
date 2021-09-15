/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import GoalUpdateComponent from '@/entities/goal/goal-update.vue';
import GoalClass from '@/entities/goal/goal-update.component';
import GoalService from '@/entities/goal/goal.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Goal Management Update Component', () => {
    let wrapper: Wrapper<GoalClass>;
    let comp: GoalClass;
    let goalServiceStub: SinonStubbedInstance<GoalService>;

    beforeEach(() => {
      goalServiceStub = sinon.createStubInstance<GoalService>(GoalService);

      wrapper = shallowMount<GoalClass>(GoalUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          goalService: () => goalServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 'ABC' };
        comp.goal = entity;
        goalServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(goalServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.goal = entity;
        goalServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(goalServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGoal = { id: 'ABC' };
        goalServiceStub.find.resolves(foundGoal);
        goalServiceStub.retrieve.resolves([foundGoal]);

        // WHEN
        comp.beforeRouteEnter({ params: { goalId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.goal).toBe(foundGoal);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
