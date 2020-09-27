package net.bzk.flow.model;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import net.bzk.infrastructure.convert.OType;

@Data
public class BzkObj implements OType {

	private String clazz;
	private String uid;

	public BzkObj() {
		clazz = this.getClass().getName();
		uid = RandomStringUtils.randomAlphanumeric(32);
	}

}
