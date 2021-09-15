import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import PersonService from '@/entities/person/person.service';
import { IPerson } from '@/shared/model/person.model';

import { IGoal, Goal } from '@/shared/model/goal.model';
import GoalService from './goal.service';

const validations: any = {
  goal: {
    title: {},
    description: {},
    targetDate: {},
    startDate: {},
    endDate: {},
    status: {},
  },
};

@Component({
  validations,
})
export default class GoalUpdate extends Vue {
  @Inject('goalService') private goalService: () => GoalService;
  public goal: IGoal = new Goal();

  @Inject('personService') private personService: () => PersonService;

  public people: IPerson[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.goalId) {
        vm.retrieveGoal(to.params.goalId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.goal.id) {
      this.goalService()
        .update(this.goal)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('goalTrackerApp.goal.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.goalService()
        .create(this.goal)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('goalTrackerApp.goal.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.goal[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.goal[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.goal[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.goal[field] = null;
    }
  }

  public retrieveGoal(goalId): void {
    this.goalService()
      .find(goalId)
      .then(res => {
        res.targetDate = new Date(res.targetDate);
        res.startDate = new Date(res.startDate);
        res.endDate = new Date(res.endDate);
        this.goal = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.personService()
      .retrieve()
      .then(res => {
        this.people = res.data;
      });
  }
}
