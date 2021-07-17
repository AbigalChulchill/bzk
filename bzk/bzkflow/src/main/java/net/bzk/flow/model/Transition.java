package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.List;

import net.bzk.flow.infra.PolyglotEngine;
import net.bzk.flow.run.service.RunLogService;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.var.VarLv.VarKey;
import net.bzk.flow.run.service.FastVarQueryer;

@Data
public class Transition {

    private String toBox = "";
    private String endTag = "TODO Why";
    private boolean failEnd = false;
    private List<VarKey> endResultKeys = new ArrayList<>();
    private String resultCode = "";

    @JsonIgnore
    public boolean isEnd() {
        return StringUtils.isBlank(toBox);
    }

    @JsonIgnore
    public String setupEndTag(FastVarQueryer varQueryer) {
        endTag = BzkFlowUtils.replaceTextNotNull(varQueryer, endTag);
        return endTag;
    }

    @JsonIgnore
    public Object packResult(RunLogService logUtils, FastVarQueryer varQueryer) {
        String rcode = BzkFlowUtils.replaceTextNotNull(varQueryer, resultCode);
        var pe = PolyglotEngine.FlowPolyglotEngine.builder().logUtils(logUtils).varQueryer(varQueryer).build();
        return pe.parseScriptbleText(rcode, Object.class);
    }

}
