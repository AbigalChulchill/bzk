package net.bzk.flow.api.dto;

import lombok.Data;
import net.bzk.infrastructure.JsonUtils.DataType;

@Data
public class ResultVal {
	
	private Object val;
	private DataType type;

}
