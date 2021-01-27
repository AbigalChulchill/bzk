import { VarKey } from './var-key';
import { BzkUtils } from './../utils/bzk-utils';
import { OTypeClass } from '../utils/bzk-utils';
import { PropClazz, PropEnums, PropInfo, PropType } from '../utils/prop-utils';
import { BzkObj } from './bzk-obj';
import { DataType, Polyglot, VarLv, ConvertMethod } from './pojo/enums';
import { ModelUpdateAdapter } from './service/model-update-adapter';
import { CommUtils } from '../utils/comm-utils';
import { Constant } from '../infrastructure/constant';
import { ChronoUnit, HttpMethod } from './enums';
import { PlaceholderUtils } from '../utils/placeholder-utils';
import { Type } from 'class-transformer';
import { VarKeyReflect } from '../infrastructure/meta';


export class Action extends BzkObj {
  @PropInfo({
    title: 'name',
    type: PropType.Text,
    updatePredicate: (p, v) => ModelUpdateAdapter.getInstance().onRefleshCart()
  })
  public name = '';
  @PropInfo({
    title: 'Catch Error',
    type: PropType.Boolean,
  })
  public tryErrorble = false;

  public listVarKey(): Array<string> {
    const ans = new Array<string>();
    const json = JSON.stringify(this);
    const ls = PlaceholderUtils.listApachHolderKey(json);
    if (ls && ls.length > 0) ls.forEach(l => ans.push(PlaceholderUtils.trimApachTag(l)));
    return ans;
  }
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
    child: {
      title: 'moudle',
      type: PropType.Text,
      autocompleteList: ['bitfinex-signature'],
      newObj: 'TODO',
    },
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
  exView: 'PolyglotActionComponent'
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

  public resultLv = VarLv.not_specify;

  public resultKey = '';

  public listVarKey(): Array<string> {
    const ans = super.listVarKey();
    ans.push(this.resultKey);
    return ans;
  };

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


@PropClazz({
  title: 'HttpAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.HttpAction'
})
export class HttpAction extends Action {
  @PropInfo({
    title: 'url',
    type: PropType.Text,
    refInfo: {
      clazz: String,
      newObj: ''
    }
  })
  public url = '';
  @PropInfo({
    title: 'method',
    type: PropType.Enum,
    refInfo: {
      clazz: HttpMethod,
      newObj: HttpMethod.GET
    }
  })
  @PropEnums(Object.keys(HttpMethod))
  public method = '"GET"';
  @PropInfo({
    title: 'uriVariables',
    type: PropType.Custom,
    customView: 'JsonEditorComponent',
    customViewFolded: true
  })
  public uriVariables = {};

  @PropInfo({
    title: 'body',
    type: PropType.Custom,
    customView: 'JsonEditorComponent',
    customViewFolded: true,
    refInfo: {
      clazz: Object,
      newObj: {}
    }
  })
  public body = '{}';

  @PropInfo({
    hide: true,
    title: 'headers',
    type: PropType.Map,
    child: {
      title: 'head',
      type: PropType.Text,
      newObj: 'TODO'
    }
  })
  public headers = {};
  @PropInfo({
    title: 'headersFlat',
    type: PropType.Map,
    refInfo: {
      clazz: Object,
      newObj: {}
    },
    child: {
      title: 'head',
      type: PropType.Text,
      newObj: 'TODO'
    }
  })
  public headersFlat = '';
  @PropInfo({
    title: 'return',
    type: PropType.Custom,
    customView: 'VarKeyComponent'
  })
  @Type(() => VarKey)
  public key = new VarKey();

  public listVarKey(): Array<string> {
    const ans = super.listVarKey();
    ans.push(this.key.key);
    return ans;
  }
}




export class KVPair {
  public key = '';
  public val = '';
}
@PropClazz({
  title: 'VarModifyAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$VarModifyAction'
})
export class VarModifyAction extends Action {

  @PropInfo({
    title: 'polyglot',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(Polyglot))
  public polyglot = Polyglot.js;

  @PropInfo({
    title: 'flatData',
    type: PropType.List,
    child: {
      type: PropType.Custom,
      newObj: new KVPair(),
      customView: 'KVPairComponent'
    },
  })
  public flatData = new Array<KVPair>();

  public listVarKey(): Array<string> {
    const ans = super.listVarKey();
    this.flatData.forEach(kv => ans.push(kv.key));
    return ans;
  }

}

@PropClazz({
  title: 'JSONPathAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$JSONPathAction'
})
export class JSONPathAction extends Action {

  @PropInfo({
    title: 'Json Path syntax',
    type: PropType.Text,
  })
  public syntax = '$';

  @PropInfo({
    title: 'source',
    type: PropType.Custom,
    customView: 'VarKeyComponent'
  })
  @Type(() => VarKey)
  public source = new VarKey();
  @PropInfo({
    title: 'target',
    type: PropType.Custom,
    customView: 'VarKeyComponent'
  })
  @Type(() => VarKey)
  public target = new VarKey();

  public listVarKey(): Array<string> {
    const ans = super.listVarKey();
    ans.push(this.source.key);
    ans.push(this.target.key);
    return ans;
  }
}

@PropClazz({
  title: 'SubFlowAction',
  exView: 'SubFlowActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$SubFlowAction'
})
export class SubFlowAction extends Action {
  @PropInfo({
    title: 'polyglot',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(Polyglot))
  public polyglot = Polyglot.js;
  @PropInfo({
    title: 'inputData',
    type: PropType.List,
    child: {
      type: PropType.Custom,
      newObj: new KVPair(),
      customView: 'KVPairComponent'
    },
  })
  @Type(() => KVPair)
  public inputData = new Array<KVPair>();

  @PropInfo({
    title: 'outputReflects',
    type: PropType.List,
    child: {
      type: PropType.Custom,
      newObj: new VarKeyReflect(),
      customView: 'VarKeyReflectComponent'
    },
  })
  @Type(() => VarKeyReflect)
  public outputReflects = new Array<VarKeyReflect>();

  @PropInfo({
    title: 'Ref Flow Uid',
    type: PropType.Label,
  })
  public flowUid = '';

  public listVarKey(): Array<string> {
    const ans = super.listVarKey();

    for (const k of this.outputReflects) {
      ans.push(k.srcKey);
    }
    return ans;
  }


}


@PropClazz({
  title: 'ConvertAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$ConvertAction'
})
export class ConvertAction extends Action {

  @PropInfo({
    title: 'method',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(ConvertMethod))
  public method = ConvertMethod.ToJSONText;
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
  public polyglot = Polyglot.js;
  @PropInfo({
    title: 'source',
    type: PropType.Custom,
    customView: 'VarKeyComponent'
  })
  @Type(() => VarKey)
  public output = new VarKey();
}


@PropClazz({
  title: 'WaitAction',
  exView: 'ActionComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Action$WaitAction'
})
export class WaitAction extends Action {
  @PropInfo({
    title: 'unit',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(ChronoUnit))
  public unit = ChronoUnit.SECONDS;
  @PropInfo({
    title: 'step',
    type: PropType.Number
  })
  public step = 0;

}
