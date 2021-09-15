import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGoal, Goal } from '@/shared/model/goal.model';
import GoalService from './goal.service';

const validations: any = {
  goal: {
    title: {},
    description: {},
  },
};

@Component({
  validations,
})
export default class GoalUpdate extends Vue {
  @Inject('goalService') private goalService: () => GoalService;
  public goal: IGoal = new Goal();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.goalId) {
        vm.retrieveGoal(to.params.goalId);
      }
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

  public retrieveGoal(goalId): void {
    this.goalService()
      .find(goalId)
      .then(res => {
        this.goal = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
