package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import net.bzk.flow.model.var.VarLv.VarKey;

@Data
public class Transition {
	
	private String toBox ="";
	private String endTag ="TODO Why";
	private List<VarKey> endResultKeys = new ArrayList<>();
	
    @JsonIgnore
	public boolean isEnd() {
		return  StringUtils.isBlank(toBox);
	}

}
