package net.bzk.infrastructure.tscurve;

import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.peak.DimensionDto;
import net.bzk.infrastructure.tscurve.peak.TsPeakDimension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestTsCurveFunc {

    @Value("classpath:tscurve/serial-test-data-90d.json")
    private Resource d90Data;

    @Test
    public void testPeakFinder() {

        double peakMaxWaitSeconds = 60 * 60 * 24 * 3;
        double macroAmplitudeRate = 0;
        Map map = loadMap(d90Data);
        DimensionDto.MacroDimensionDto md = new DimensionDto.MacroDimensionDto();
        md.setPeakMaxWaitSeconds(peakMaxWaitSeconds);
        md.setAmplitudeRate(macroAmplitudeRate);
        Map mdsm = JsonUtils.toByJson(md,Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
        var mFirst= ans.getTrendInfo().getNearMax();
        TsHowBig.Dto dto = TsHowBig.Dto.builder()
                .bigger(true)
                .targetKey(mFirst.getKey())
                .build();
        var bAns = TsCurveFunc.getInstance().findBigger(map,JsonUtils.toJson(dto));
        System.out.println(bAns);

    }

    @Test
    public void testConD(){
        Map map = loadMap(d90Data);
        var ans = TsCurveFunc.getInstance().conD(map, TsContinuousDirection.Mode.UNIFORM_SLOPE.toString(),3);
        System.out.println(ans);
    }

    private Map  loadMap(Resource r){
        final String str = CommUtils.loadBy(d90Data);
        System.out.println(str);
        Map map = JsonUtils.loadByJson(str, Map.class);
        return map;
    }

}
