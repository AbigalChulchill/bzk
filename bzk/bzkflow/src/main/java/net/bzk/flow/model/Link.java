package net.bzk.flow.model;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Link  {
	
	private String toBox;
	private Condition condition;
	private String uid;
	
	public Link() {
		uid = RandomStringUtils.randomAlphanumeric(8);
	}
	

}
