package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.List;

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

	@JsonIgnore
	public boolean isEnd() {
		return StringUtils.isBlank(toBox);
	}

	@JsonIgnore
	public String setupEndTag(FastVarQueryer varQueryer) {
		String tag = BzkFlowUtils.replaceText(varQueryer, endTag);
		if (StringUtils.isNotBlank(tag)) {
			endTag = tag;
		}
		return endTag;
	}

}
