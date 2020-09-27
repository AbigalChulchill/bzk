import { PropInfo, PropType } from '../utils/prop-utils';

export class BzkObj {
  @PropInfo({
    title: 'clazz',
    titleI18nKey: 'clazz',
    type: PropType.Label
  })
  public clazz: string;

  @PropInfo({
    title: 'uid',
    titleI18nKey: 'uid',
    type: PropType.Label
  })
  public uid: string;

}
