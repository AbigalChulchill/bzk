package net.bzk.infrastructure.tscurve.peak;

import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.peak.logic.MacroLogic;
import net.bzk.infrastructure.tscurve.peak.logic.MicroLogic;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

public enum PeakLogicType {
    MACRO, MICRO , BIGGER;

    public TsPeakLogic genLogic() {
        switch (this) {
            case MACRO:
                return new MacroLogic();
            case MICRO:
                return new MicroLogic();
            case BIGGER:
                return new BiggerPeakLogic();
        }
        throw new NotImplementedException(this + "not support");
    }

    public Class<? extends PeakLogicDto> getDtoClass(){
        switch (this) {
            case MACRO:
                return PeakLogicDto.MacroPeakLogicDto.class;
            case MICRO:
                return PeakLogicDto.MicroPeakLogicDto.class;
            case BIGGER:
                return PeakLogicDto.BiggerPeakLogicDto.class;
        }
        throw new NotImplementedException(this + "not support");
    }

    public PeakLogicDto genDto(Map<String, Object> m) {
        var c = getDtoClass();
        return JsonUtils.toByJson(m, c);
    }

}
