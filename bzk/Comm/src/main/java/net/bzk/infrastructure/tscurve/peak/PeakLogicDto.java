package net.bzk.infrastructure.tscurve.peak;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PeakLogicDto {


    PeakLogicType logicType;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class WaitPeakLogicDto extends PeakLogicDto {
        double peakMaxWaitSeconds;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroPeakLogicDto extends WaitPeakLogicDto {

        double baseVal;
        double amplitudeRate;

        public MacroPeakLogicDto() {
            setLogicType(PeakLogicType.MACRO);
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MicroPeakLogicDto extends WaitPeakLogicDto {

        public MicroPeakLogicDto() {
            setLogicType(PeakLogicType.MICRO);
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class BiggerPeakLogicDto extends PeakLogicDto {

        double persistTime;

        public BiggerPeakLogicDto() {
            setLogicType(PeakLogicType.BIGGER);
        }

    }

}
