package net.bzk.infrastructure.tscurve;

import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.dto.Point;
import net.bzk.infrastructure.tscurve.peak.PeakLogicDto;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Value("classpath:tscurve/serial-test-data-dif-rebound.json")
    private Resource difReboundData;

    /*
     * from(bucket: \"quote\")\r\n  |> range(start: 2020-10-29T15:34:30Z, stop:2021-10-29T15:34:30Z)\r\n  |> filter(fn: (r) =>\r\n    r._measurement == \"daily\" and\r\n    r.valmean == \"open\" and\r\n    r.symbol == \"BTC\"\r\n  )
     */
    @Value("classpath:tscurve/serial-test-data-bais-2020-10-29T15-34-30Z_2021-10-29T15-34-30Z.json")
    private Resource y1Data;

    /*
     * from(bucket: \"quote\")\r\n  |> range(start: 2021-11-12T06:32:34Z, stop:2021-11-13T06:32:34Z)\r\n   |> filter(fn: (r) =>\r\n    r._measurement == \"realtime\" and\r\n    r.symbol == \"BTC\" and\r\n    r._field == \"price\"\r\n  )
     */
    @Value("classpath:tscurve/serial-test-data-20211112-20211113.json")
    private Resource d1Data;

    @Value("classpath:tscurve/serial-test-data-support-2y.json")
    private Resource support2yData;
    @Value("classpath:tscurve/serial-test-data-2y-all.json")
    private Resource y2AllData;

    @Test
    public void testSupportPoints() {
        double peakMaxWaitSeconds = 60 * 5;
        double macroAmplitudeRate = 0;
        Map supportMap = loadMap(support2yData);
        Map<String, Double> allData = loadMap(y2AllData);
        Map<String, Double> _supportMap = new HashMap<>();
        for (var kv : supportMap.entrySet()) {
            Map.Entry<String, Integer> _kv = (Map.Entry<String, Integer>) kv;
            _supportMap.put(_kv.getKey(), Double.parseDouble(_kv.getValue() + ""));
        }
        PeakLogicDto.MacroPeakLogicDto md = new PeakLogicDto.MacroPeakLogicDto();
        md.setPeakMaxWaitSeconds(peakMaxWaitSeconds);
        md.setAmplitudeRate(macroAmplitudeRate);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(_supportMap, mdsm);
        System.out.println(ans);
        var maxList= ans.getTrendInfo().getMaxList().values();
        List<Map<String,Object>> outPs = new ArrayList<>();
        for(var k : maxList){
            var val= TsCurveFunc.getInstance().getNearVal(k.key,allData);
            Map<String,Object> rowA = new HashMap();
            rowA.put("price",val);
            rowA.put("persist",k.val);
            rowA.put("key",k.key);
            outPs.add(rowA);
        }
        System.out.println(outPs);

    }

    @Test
    public void testPeakRebound() {

        double peakMaxWaitSeconds = 60 * 5;
        double macroAmplitudeRate = 0;
        Map map = loadMap(difReboundData);
        PeakLogicDto.MacroPeakLogicDto md = new PeakLogicDto.MacroPeakLogicDto();
        md.setPeakMaxWaitSeconds(peakMaxWaitSeconds);
        md.setAmplitudeRate(macroAmplitudeRate);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
    }

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
    public void test1YBiggerPeakFinder() {

        double persistTime = 60 * 60 * 24 * 5;
        Map map = loadMap(y1Data);
        PeakLogicDto.BiggerPeakLogicDto md = new PeakLogicDto.BiggerPeakLogicDto();
        md.setPersistTime(persistTime);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
        var allmap = ans.getTrendInfo().getAllList();
        var sortPoints = TsCurveUtils.sortPoints(allmap);
        System.out.println(sortPoints);
        var sortMinPoints = TsCurveUtils.sortPoints(ans.getTrendInfo().getMinList());
        System.out.println(sortMinPoints);
    }

    @Test
    public void test1DBiggerPeakFinder() {

        double persistTime = 60 * 60 * 3;
        Map map = loadMap(d1Data);
        PeakLogicDto.BiggerPeakLogicDto md = new PeakLogicDto.BiggerPeakLogicDto();
        md.setPersistTime(persistTime);
        md.setReversePersistTime(60 * 30);
        Map mdsm = JsonUtils.toByJson(md, Map.class);
        var ans = TsCurveFunc.getInstance().findPeak(map, mdsm);
        System.out.println(ans);
        var allmap = ans.getTrendInfo().getAllList();
        var sortPoints = TsCurveUtils.sortPoints(allmap);
        System.out.println(sortPoints);
        var sortMinPoints = TsCurveUtils.sortPoints(ans.getTrendInfo().getMinList());
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
        List<Point> ps = keys.stream().map(k -> ans.getTrendInfo().getAllList().get(k)).collect(Collectors.toList());
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
