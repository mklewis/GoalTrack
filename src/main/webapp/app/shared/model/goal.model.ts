export interface IGoal {
  id?: string;
  title?: string | null;
  description?: string | null;
}

export class Goal implements IGoal {
  constructor(public id?: string, public title?: string | null, public description?: string | null) {}
}
