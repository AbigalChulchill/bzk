import { PropInfo, PropType } from '../utils/prop-utils';

export class OType {
  @PropInfo({
    title: 'clazz',
    type: PropType.Label
  })
  public clazz = '';
}

export class BzkObj extends OType {


  @PropInfo({
    title: 'uid',
    type: PropType.Label
  })
  public uid = '';


}
