/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import GoalComponent from '@/entities/goal/goal.vue';
import GoalClass from '@/entities/goal/goal.component';
import GoalService from '@/entities/goal/goal.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
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
  describe('Goal Management Component', () => {
    let wrapper: Wrapper<GoalClass>;
    let comp: GoalClass;
    let goalServiceStub: SinonStubbedInstance<GoalService>;

    beforeEach(() => {
      goalServiceStub = sinon.createStubInstance<GoalService>(GoalService);
      goalServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<GoalClass>(GoalComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          goalService: () => goalServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      goalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllGoals();
      await comp.$nextTick();

      // THEN
      expect(goalServiceStub.retrieve.called).toBeTruthy();
      expect(comp.goals[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should load a page', async () => {
      // GIVEN
      goalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(goalServiceStub.retrieve.called).toBeTruthy();
      expect(comp.goals[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      goalServiceStub.retrieve.reset();
      goalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(goalServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.goals[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      goalServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      comp.removeGoal();
      await comp.$nextTick();

      // THEN
      expect(goalServiceStub.delete.called).toBeTruthy();
      expect(goalServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
