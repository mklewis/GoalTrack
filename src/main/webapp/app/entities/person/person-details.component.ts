import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPerson } from '@/shared/model/person.model';
import PersonService from './person.service';

@Component
export default class PersonDetails extends Vue {
  @Inject('personService') private personService: () => PersonService;
  public person: IPerson = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.personId) {
        vm.retrievePerson(to.params.personId);
      }
    });
  }

  public retrievePerson(personId) {
    this.personService()
      .find(personId)
      .then(res => {
        this.person = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
