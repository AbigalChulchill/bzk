# archived flow script

## dif stop limit
```javascript
const difPeak = ${cs.difPeak};
const difPoints = $tsFunc.toPoints(${cs.dif});
const difTH = ${cs.difTH};
const k5mLow = ${cs.k5m.low};
const k5mHigh = ${cs.k5m.high};
const positions = ${position};

function getThVal(){
    var key;
    for (var k in difTH) {
        return difTH[k];
    }
}

function getPositionSide(){
    return difPeak.trendInfo.state == 'FALL' ? 'SHORT':'LONG';
}

const positionSide = getPositionSide();
const stopPositionSide = positionSide == 'LONG' ? 'SHORT' : 'LONG';
const tarPostion = positions[stopPositionSide];
$bzk.put('tarPostion',JSON.stringify(tarPostion));


const nearPeak = difPeak.trendInfo.nearPeak;


function getStopPrice(){

    if(positionSide=='LONG'){
        return k5mLow[nearPeak.key];
    }else if(positionSide == 'SHORT'){
        return k5mHigh[nearPeak.key];
    }
    throw new Error('getStopPrice not support='+positionSide);
}

function genOrderObjJson(){
    const peakPrice=getStopPrice();
    return JSON.stringify({
        // "targetPrice":peakPrice,
        "stopPrice": -1,
        // "gapRate":-1,
        "attachName":'DifTrace',
        "attach":{
            "stopPrice":peakPrice,
            "nearPeak":nearPeak
        }
    });
}

$bzk.putObj('positionSide',positionSide);
$bzk.put('difNearPeak',JSON.stringify( nearPeak));
$bzk.putObj('~thVal',getThVal());
$bzk.put('~orderLimitBody',genOrderObjJson());
true;
```
