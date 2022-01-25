package net.bzk.infrastructure.tscurve.peak;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PeakLogicDto {


    public PeakLogicType logicType;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class WaitPeakLogicDto extends PeakLogicDto {
        public double peakMaxWaitSeconds;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroPeakLogicDto extends WaitPeakLogicDto {

        public double baseVal;
        public double amplitudeRate;

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

        public double persistTime;
        public double reversePersistTime;
        public double reverseWaitTime;

        public BiggerPeakLogicDto() {
            setLogicType(PeakLogicType.BIGGER);
        }

    }

}
