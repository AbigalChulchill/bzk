package net.bzk.srv.model.flow;

import java.util.UUID;

import lombok.Data;

@Data
public class BzkObj {

	private String clazz;
	private String uid;

	public BzkObj() {
		clazz = this.getClass().getName();
		UUID uuid = UUID.randomUUID();
		uid = uuid.toString();
	}
}
