import { IGoal } from '@/shared/model/goal.model';

export interface IPerson {
  id?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  goals?: IGoal[] | null;
}

export class Person implements IPerson {
  constructor(
    public id?: string,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public goals?: IGoal[] | null
  ) {}
}
