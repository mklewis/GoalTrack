import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import GoalService from '@/entities/goal/goal.service';
import { IGoal } from '@/shared/model/goal.model';

import PersonService from '@/entities/person/person.service';
import { IPerson } from '@/shared/model/person.model';

import { IPersonGoalActivity, PersonGoalActivity } from '@/shared/model/person-goal-activity.model';
import PersonGoalActivityService from './person-goal-activity.service';

const validations: any = {
  personGoalActivity: {
    targetDate: {},
    startDate: {},
    endDate: {},
    status: {},
  },
};

@Component({
  validations,
})
export default class PersonGoalActivityUpdate extends Vue {
  @Inject('personGoalActivityService') private personGoalActivityService: () => PersonGoalActivityService;
  public personGoalActivity: IPersonGoalActivity = new PersonGoalActivity();

  @Inject('goalService') private goalService: () => GoalService;

  public goals: IGoal[] = [];

  @Inject('personService') private personService: () => PersonService;

  public people: IPerson[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.personGoalActivityId) {
        vm.retrievePersonGoalActivity(to.params.personGoalActivityId);
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
    if (this.personGoalActivity.id) {
      this.personGoalActivityService()
        .update(this.personGoalActivity)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('goalTrackerApp.personGoalActivity.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.personGoalActivityService()
        .create(this.personGoalActivity)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('goalTrackerApp.personGoalActivity.created', { param: param.id });
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
      this.personGoalActivity[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.personGoalActivity[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.personGoalActivity[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.personGoalActivity[field] = null;
    }
  }

  public retrievePersonGoalActivity(personGoalActivityId): void {
    this.personGoalActivityService()
      .find(personGoalActivityId)
      .then(res => {
        res.targetDate = new Date(res.targetDate);
        res.startDate = new Date(res.startDate);
        res.endDate = new Date(res.endDate);
        this.personGoalActivity = res;
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
    this.personService()
      .retrieve()
      .then(res => {
        this.people = res.data;
      });
  }
}
