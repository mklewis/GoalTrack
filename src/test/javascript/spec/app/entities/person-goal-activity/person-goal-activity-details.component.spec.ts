/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PersonGoalActivityDetailComponent from '@/entities/person-goal-activity/person-goal-activity-details.vue';
import PersonGoalActivityClass from '@/entities/person-goal-activity/person-goal-activity-details.component';
import PersonGoalActivityService from '@/entities/person-goal-activity/person-goal-activity.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('PersonGoalActivity Management Detail Component', () => {
    let wrapper: Wrapper<PersonGoalActivityClass>;
    let comp: PersonGoalActivityClass;
    let personGoalActivityServiceStub: SinonStubbedInstance<PersonGoalActivityService>;

    beforeEach(() => {
      personGoalActivityServiceStub = sinon.createStubInstance<PersonGoalActivityService>(PersonGoalActivityService);

      wrapper = shallowMount<PersonGoalActivityClass>(PersonGoalActivityDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { personGoalActivityService: () => personGoalActivityServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPersonGoalActivity = { id: 'ABC' };
        personGoalActivityServiceStub.find.resolves(foundPersonGoalActivity);

        // WHEN
        comp.retrievePersonGoalActivity('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.personGoalActivity).toBe(foundPersonGoalActivity);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPersonGoalActivity = { id: 'ABC' };
        personGoalActivityServiceStub.find.resolves(foundPersonGoalActivity);

        // WHEN
        comp.beforeRouteEnter({ params: { personGoalActivityId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.personGoalActivity).toBe(foundPersonGoalActivity);
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
