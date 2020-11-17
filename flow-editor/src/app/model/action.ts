import { ActionComponent } from './view/action/action.component';
import { CommModelUpdater } from './service/comm-model-updater';
import { BzkUtils } from './../utils/bzk-utils';
import { OTypeClass } from '../utils/bzk-utils';
import { PropClazz, PropEnums, PropInfo, PropType } from '../utils/prop-utils';
import { BzkObj } from './bzk-obj';
import { DataType, Polyglot, VarLv } from './pojo/enums';
import { ModelUpdateAdapter } from './service/model-update-adapter';
import { CommUtils } from '../utils/comm-utils';
import { Constant } from '../infrastructure/constant';


export class Action extends BzkObj {
  @PropInfo({
    title: 'name',
    type: PropType.Text,
    updatePredicate: (p, v) => ModelUpdateAdapter.getInstance().onRefleshCart()
  })
  public name = '';

  @PropInfo({
    title: 'clazz',
    type: PropType.Enum,
    updatePredicate: (p, v) => ModelUpdateAdapter.getInstance().onClazzUpdate(p, v)
  })
  @PropEnums(() => BzkUtils.listOtypeKeys(Action))
  public clazz = '';
}

@PropClazz({
  title: 'NodejsAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$NodejsAction'
})
export class NodejsAction extends Action {
  @PropInfo({
    title: 'code',
    type: PropType.MultipleText
  })
  public code = '';
  @PropInfo({
    title: 'installs',
    type: PropType.List,
    listChildType: PropType.Text,
    listChildNewObj: 'TODO',
    autocompleteList: ['bitfinex-signature']
  })
  public installs = new Array<string>();
  public dependencies: Map<string, string>;
  public devDependencies: Map<string, string>;

  public static gen(): NodejsAction {
    const na = new NodejsAction();
    na.uid = CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;
    return na;
  }
}


@PropClazz({
  title: 'PolyglotAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$PolyglotAction'
})
export class PolyglotAction extends Action {
  @PropInfo({
    title: 'code',
    type: PropType.MultipleText
  })
  public code = '';
  @PropInfo({
    title: 'polyglot',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(Polyglot))
  public polyglot = Polyglot.python;
  @PropInfo({
    title: 'resultLv',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(VarLv))
  public resultLv = VarLv.not_specify;
  @PropInfo({
    title: 'resultKey',
    type: PropType.Text
  })
  public resultKey = '';
  @PropInfo({
    title: 'resultType',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(DataType))
  public resultType = DataType.string;
}


@PropClazz({
  title: 'MXparser',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$MXparserAction'
})
export class MXparserAction extends Action {
  @PropInfo({
    title: 'code',
    type: PropType.MultipleText
  })
  public code = '';

  @PropInfo({
    title: 'resultLv',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(VarLv))
  public resultLv = VarLv.not_specify;

  @PropInfo({
    title: 'resultKey',
    type: PropType.Text
  })
  public resultKey = '';

}

