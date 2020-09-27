import { ChronoUnit } from './enums';

export class Entry {
  public clazz: string;
  public boxUid: string;
}

export class FixedRateEntry extends Entry {

  public period: number;
  public initUnit: ChronoUnit;
  public initialDelay: number;

}
