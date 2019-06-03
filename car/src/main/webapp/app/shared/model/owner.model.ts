import { ICar } from 'app/shared/model/car.model';

export interface IOwner {
  id?: number;
  firstname?: string;
  lastname?: string;
  birthyear?: number;
  cars?: ICar[];
}

export class Owner implements IOwner {
  constructor(public id?: number, public firstname?: string, public lastname?: string, public birthyear?: number, public cars?: ICar[]) {}
}
