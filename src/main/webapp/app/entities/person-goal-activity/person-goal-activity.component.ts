import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPersonGoalActivity } from '@/shared/model/person-goal-activity.model';

import PersonGoalActivityService from './person-goal-activity.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class PersonGoalActivity extends Vue {
  @Inject('personGoalActivityService') private personGoalActivityService: () => PersonGoalActivityService;
  private removeId: string = null;

  public personGoalActivities: IPersonGoalActivity[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPersonGoalActivitys();
  }

  public clear(): void {
    this.retrieveAllPersonGoalActivitys();
  }

  public retrieveAllPersonGoalActivitys(): void {
    this.isFetching = true;
    this.personGoalActivityService()
      .retrieve()
      .then(
        res => {
          this.personGoalActivities = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IPersonGoalActivity): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePersonGoalActivity(): void {
    this.personGoalActivityService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('goalTrackerApp.personGoalActivity.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPersonGoalActivitys();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
