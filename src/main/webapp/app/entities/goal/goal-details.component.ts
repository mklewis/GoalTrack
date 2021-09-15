import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGoal } from '@/shared/model/goal.model';
import GoalService from './goal.service';

@Component
export default class GoalDetails extends Vue {
  @Inject('goalService') private goalService: () => GoalService;
  public goal: IGoal = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.goalId) {
        vm.retrieveGoal(to.params.goalId);
      }
    });
  }

  public retrieveGoal(goalId) {
    this.goalService()
      .find(goalId)
      .then(res => {
        this.goal = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
