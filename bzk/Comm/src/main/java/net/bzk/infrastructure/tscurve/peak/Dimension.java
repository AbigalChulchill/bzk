package net.bzk.infrastructure.tscurve.peak;

import net.bzk.infrastructure.JsonUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

public enum Dimension {
    MACRO, MICRO;

    public TsPeakDimension genLogic() {
        switch (this) {
            case MACRO:
                return new TsPeakDimension.MacroDimension();
            case MICRO:
                return new TsPeakDimension.MicroDimension();
        }
        throw new NotImplementedException(this + "not support");
    }

    public Class<? extends DimensionDto> getDtoClass(){
        switch (this) {
            case MACRO:
                return DimensionDto.MacroDimensionDto.class;
            case MICRO:
                return DimensionDto.MicroDimensionDto.class;
        }
        throw new NotImplementedException(this + "not support");
    }

    public DimensionDto genDto(Map<String, Object> m) {
        var c = getDtoClass();
        return JsonUtils.toByJson(m, c);
    }

}
