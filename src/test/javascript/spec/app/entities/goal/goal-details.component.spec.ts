/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import GoalDetailComponent from '@/entities/goal/goal-details.vue';
import GoalClass from '@/entities/goal/goal-details.component';
import GoalService from '@/entities/goal/goal.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Goal Management Detail Component', () => {
    let wrapper: Wrapper<GoalClass>;
    let comp: GoalClass;
    let goalServiceStub: SinonStubbedInstance<GoalService>;

    beforeEach(() => {
      goalServiceStub = sinon.createStubInstance<GoalService>(GoalService);

      wrapper = shallowMount<GoalClass>(GoalDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { goalService: () => goalServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGoal = { id: 'ABC' };
        goalServiceStub.find.resolves(foundGoal);

        // WHEN
        comp.retrieveGoal('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.goal).toBe(foundGoal);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGoal = { id: 'ABC' };
        goalServiceStub.find.resolves(foundGoal);

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
