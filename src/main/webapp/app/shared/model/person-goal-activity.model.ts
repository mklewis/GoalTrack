import { IGoal } from '@/shared/model/goal.model';
import { IPerson } from '@/shared/model/person.model';

import { Status } from '@/shared/model/enumerations/status.model';
export interface IPersonGoalActivity {
  id?: string;
  targetDate?: Date | null;
  startDate?: Date | null;
  endDate?: Date | null;
  status?: Status | null;
  goal?: IGoal | null;
  person?: IPerson | null;
}

export class PersonGoalActivity implements IPersonGoalActivity {
  constructor(
    public id?: string,
    public targetDate?: Date | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public status?: Status | null,
    public goal?: IGoal | null,
    public person?: IPerson | null
  ) {}
}
