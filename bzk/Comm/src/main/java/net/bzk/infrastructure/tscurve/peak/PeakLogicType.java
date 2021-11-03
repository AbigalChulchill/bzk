package net.bzk.infrastructure.tscurve.peak;

import net.bzk.infrastructure.JsonUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

public enum PeakLogicType {
    MACRO, MICRO , BIGGER;

    public TsPeakLogic genLogic() {
        switch (this) {
            case MACRO:
                return new TsPeakLogic.MacroLogic();
            case MICRO:
                return new TsPeakLogic.MicroLogic();
        }
        throw new NotImplementedException(this + "not support");
    }

    public Class<? extends PeakLogicDto> getDtoClass(){
        switch (this) {
            case MACRO:
                return PeakLogicDto.MacroPeakLogicDto.class;
            case MICRO:
                return PeakLogicDto.MicroPeakLogicDto.class;
        }
        throw new NotImplementedException(this + "not support");
    }

    public PeakLogicDto genDto(Map<String, Object> m) {
        var c = getDtoClass();
        return JsonUtils.toByJson(m, c);
    }

}
