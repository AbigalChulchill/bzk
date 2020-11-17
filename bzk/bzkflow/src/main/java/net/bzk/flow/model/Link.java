package net.bzk.flow.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Link extends BzkObj {

	private String name;
	private String toBox;
	private Condition condition;
	private boolean directed;
	private String endTag;

    @JsonIgnore
	public boolean isEnd() {
		return StringUtils.isNotBlank(endTag);
	}

}
