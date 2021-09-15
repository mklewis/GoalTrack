import { Component, Vue, Inject } from 'vue-property-decorator';

import GoalService from '@/entities/goal/goal.service';
import { IGoal } from '@/shared/model/goal.model';

import { IPerson, Person } from '@/shared/model/person.model';
import PersonService from './person.service';

const validations: any = {
  person: {
    firstName: {},
    lastName: {},
    email: {},
    phoneNumber: {},
  },
};

@Component({
  validations,
})
export default class PersonUpdate extends Vue {
  @Inject('personService') private personService: () => PersonService;
  public person: IPerson = new Person();

  @Inject('goalService') private goalService: () => GoalService;

  public goals: IGoal[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.personId) {
        vm.retrievePerson(to.params.personId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.person.goals = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.person.id) {
      this.personService()
        .update(this.person)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('goalTrackerApp.person.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.personService()
        .create(this.person)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('goalTrackerApp.person.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrievePerson(personId): void {
    this.personService()
      .find(personId)
      .then(res => {
        this.person = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.goalService()
      .retrieve()
      .then(res => {
        this.goals = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
