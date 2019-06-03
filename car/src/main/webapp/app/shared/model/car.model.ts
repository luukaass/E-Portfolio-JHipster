import { Moment } from 'moment';
import { IOwner } from 'app/shared/model/owner.model';

export interface ICar {
  id?: number;
  model?: string;
  company?: string;
  dateOfProduction?: Moment;
  owner?: IOwner;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public model?: string,
    public company?: string,
    public dateOfProduction?: Moment,
    public owner?: IOwner
  ) {}
}
