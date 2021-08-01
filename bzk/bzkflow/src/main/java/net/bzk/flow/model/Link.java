package net.bzk.flow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Link extends BzkObj {

	private String name;
	private Condition condition;
	private Transition transition = new Transition();
	private boolean enable = true;
	
//    @JsonIgnore
//	public boolean isEnd() {
//		return transition.isEnd();
//	}

}
