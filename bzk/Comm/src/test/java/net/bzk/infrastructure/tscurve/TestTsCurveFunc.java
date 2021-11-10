package net.bzk.infrastructure.tscurve;

import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.peak.PeakLogicDto;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestTsCurveFunc {

    @Value("classpath:tscurve/serial-test-data-90d.json")
    private Resource d90Data;
    @Value("classpath:tscurve/serial-test-data-20210928-20190930.json")
    private Resource y2Data;
    @Value("classpath:tscurve/serial-test-data-bais-20211006-20191107.json")
    private Resource biasY1Data;

    /*
     * from(bucket: \"quote\")\r\n  |> range(start: 2020-10-29T15:34:30Z, stop:2021-10-29T15:34:30Z)\r\n  |> filter(fn: (r) =>\r\n    r._measurement == \"daily\" and\r\n    r.valmean == \"open\" and\r\n    r.symbol == \"BTC\"\r\n  )
     */
    @Value("classpath:tscurve/serial-test-data-bais-2020-10-29T15-34-30Z_2021-10-29T15-34-30Z.json")
    private Resource y1Data;


    @Test
    public void testPeakFinder() {

        double peakMaxWaitSeconds = 60 * 60 * 24 * 3;
        double macroAmplitudeRate = 0;
        Map map = loadMap(d90Data);
        PeakLogicDto.MacroPeakLogicDto md = new PeakLogicDto.MacroPeakLogicDto();
        md.setPeakMaxWaitSeconds(peakMaxWaitSeconds);
        md.setAmplitudeRate(macroAmplitudeRate);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
        var mFirst = ans.getTrendInfo().getNearMax();
        TsHowBig.Dto dto = TsHowBig.Dto.builder()
                .bigger(true)
                .targetKey(mFirst.getKey())
                .build();
        var bAns = TsCurveFunc.getInstance().howBigger(map, JsonUtils.toJson(dto));
        System.out.println(bAns);

    }

    @Test
    public void test2YPeakFinder() {

        double peakMaxWaitSeconds = 60 * 60 * 1.5;
        Map map = loadMap(y2Data);
        PeakLogicDto.MicroPeakLogicDto md = new PeakLogicDto.MicroPeakLogicDto();
        md.setPeakMaxWaitSeconds(peakMaxWaitSeconds);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
        var mFirst = ans.getTrendInfo().getNearMin();
        TsHowBig.Dto dto = TsHowBig.Dto.builder()
                .bigger(ans.getTrendInfo().getNearPeakType() == TsPeakFinder.PointType.MAXED)
                .targetKey(mFirst.getKey())
                .build();
        var bAns = TsCurveFunc.getInstance().howBigger(map, JsonUtils.toJson(dto));
        System.out.println(bAns);

    }

    @Test
    public void test1YPeakFinder() {

        double persistTime = 60 * 60 * 24 * 15;
        Map map = loadMap(y1Data);
        PeakLogicDto.BiggerPeakLogicDto md = new PeakLogicDto.BiggerPeakLogicDto();
        md.setPersistTime(persistTime);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
        var allmap = ans.getTrendInfo().getAllList();
        var sortPoints = TsCurveUtils.sortPoints(allmap);
        System.out.println(sortPoints);
        var sortMinPoints = TsCurveUtils.sortPoints( ans.getTrendInfo().getMinList());
        System.out.println(sortMinPoints);
    }

    @Test
    public void testY1BiasPeakFinder() {

        double peakMaxWaitSeconds = 60 * 60 * 24 * 4;
        Map map = loadMap(biasY1Data);
        PeakLogicDto.MacroPeakLogicDto md = new PeakLogicDto.MacroPeakLogicDto();
        md.setPeakMaxWaitSeconds(peakMaxWaitSeconds);
        md.setAmplitudeRate(0.1);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);

        System.out.println(ans);

        var keys = TsCurveUtils.sortTimeKeys(ans.getTrendInfo().getAllList().keySet());
        List<TsCurveUtils.Point> ps = keys.stream().map(k -> ans.getTrendInfo().getAllList().get(k)).collect(Collectors.toList());
        System.out.println(ps);

        var cResult = TsCurveFunc.getInstance().calcCycle(JsonUtils.toJson(ans.getTrendInfo()));
        System.out.println(cResult);
        double d = cResult.getAvgRadius() / (60 * 60 * 24);
        System.out.println(d);

    }

    @Test
    public void testConD() {
        Map map = loadMap(d90Data);
        var ans = TsCurveFunc.getInstance().conD(map, TsContinuousDirection.Mode.UNIFORM_SLOPE.toString(), 3);
        System.out.println(ans);
    }

    private Map loadMap(Resource r) {
        final String str = CommUtils.loadBy(r);
        System.out.println(str);
        Map map = JsonUtils.loadByJson(str, Map.class);
        return map;
    }

}
