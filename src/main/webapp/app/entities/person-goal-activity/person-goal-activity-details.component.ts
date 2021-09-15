import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPersonGoalActivity } from '@/shared/model/person-goal-activity.model';
import PersonGoalActivityService from './person-goal-activity.service';

@Component
export default class PersonGoalActivityDetails extends Vue {
  @Inject('personGoalActivityService') private personGoalActivityService: () => PersonGoalActivityService;
  public personGoalActivity: IPersonGoalActivity = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.personGoalActivityId) {
        vm.retrievePersonGoalActivity(to.params.personGoalActivityId);
      }
    });
  }

  public retrievePersonGoalActivity(personGoalActivityId) {
    this.personGoalActivityService()
      .find(personGoalActivityId)
      .then(res => {
        this.personGoalActivity = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
