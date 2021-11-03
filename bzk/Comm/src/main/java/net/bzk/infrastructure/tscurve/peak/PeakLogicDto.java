package net.bzk.infrastructure.tscurve.peak;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PeakLogicDto {

    double peakMaxWaitSeconds;
    PeakLogicType dimension;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroPeakLogicDto extends PeakLogicDto {

        double baseVal;
        double amplitudeRate;

        public MacroPeakLogicDto() {
            setDimension(PeakLogicType.MACRO);
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MicroPeakLogicDto extends PeakLogicDto {


        public MicroPeakLogicDto() {
            setDimension(PeakLogicType.MICRO);
        }

    }

}
