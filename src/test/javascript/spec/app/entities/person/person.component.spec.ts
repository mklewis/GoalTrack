/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import PersonComponent from '@/entities/person/person.vue';
import PersonClass from '@/entities/person/person.component';
import PersonService from '@/entities/person/person.service';

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
  describe('Person Management Component', () => {
    let wrapper: Wrapper<PersonClass>;
    let comp: PersonClass;
    let personServiceStub: SinonStubbedInstance<PersonService>;

    beforeEach(() => {
      personServiceStub = sinon.createStubInstance<PersonService>(PersonService);
      personServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PersonClass>(PersonComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          personService: () => personServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      personServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllPersons();
      await comp.$nextTick();

      // THEN
      expect(personServiceStub.retrieve.called).toBeTruthy();
      expect(comp.people[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should load a page', async () => {
      // GIVEN
      personServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(personServiceStub.retrieve.called).toBeTruthy();
      expect(comp.people[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      personServiceStub.retrieve.reset();
      personServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(personServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.people[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
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
      personServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      comp.removePerson();
      await comp.$nextTick();

      // THEN
      expect(personServiceStub.delete.called).toBeTruthy();
      expect(personServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
