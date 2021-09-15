import { IPerson } from '@/shared/model/person.model';

import { Status } from '@/shared/model/enumerations/status.model';
export interface IGoal {
  id?: string;
  title?: string | null;
  description?: string | null;
  targetDate?: Date | null;
  startDate?: Date | null;
  endDate?: Date | null;
  status?: Status | null;
  people?: IPerson[] | null;
}

export class Goal implements IGoal {
  constructor(
    public id?: string,
    public title?: string | null,
    public description?: string | null,
    public targetDate?: Date | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public status?: Status | null,
    public people?: IPerson[] | null
  ) {}
}
