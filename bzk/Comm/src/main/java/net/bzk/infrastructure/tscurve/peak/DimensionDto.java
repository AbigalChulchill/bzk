package net.bzk.infrastructure.tscurve.peak;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class DimensionDto {

    double peakMaxWaitSeconds;
    Dimension dimension;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroDimensionDto extends DimensionDto {

        double baseVal;
        double amplitudeRate;

        public MacroDimensionDto() {
            setDimension(Dimension.MACRO);
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MicroDimensionDto extends DimensionDto {


        public MicroDimensionDto() {
            setDimension(Dimension.MICRO);
        }

    }

}
