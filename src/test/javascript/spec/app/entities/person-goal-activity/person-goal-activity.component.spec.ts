/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import PersonGoalActivityComponent from '@/entities/person-goal-activity/person-goal-activity.vue';
import PersonGoalActivityClass from '@/entities/person-goal-activity/person-goal-activity.component';
import PersonGoalActivityService from '@/entities/person-goal-activity/person-goal-activity.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('PersonGoalActivity Management Component', () => {
    let wrapper: Wrapper<PersonGoalActivityClass>;
    let comp: PersonGoalActivityClass;
    let personGoalActivityServiceStub: SinonStubbedInstance<PersonGoalActivityService>;

    beforeEach(() => {
      personGoalActivityServiceStub = sinon.createStubInstance<PersonGoalActivityService>(PersonGoalActivityService);
      personGoalActivityServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PersonGoalActivityClass>(PersonGoalActivityComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          personGoalActivityService: () => personGoalActivityServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      personGoalActivityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllPersonGoalActivitys();
      await comp.$nextTick();

      // THEN
      expect(personGoalActivityServiceStub.retrieve.called).toBeTruthy();
      expect(comp.personGoalActivities[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      personGoalActivityServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      comp.removePersonGoalActivity();
      await comp.$nextTick();

      // THEN
      expect(personGoalActivityServiceStub.delete.called).toBeTruthy();
      expect(personGoalActivityServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
