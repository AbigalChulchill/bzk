package net.bzk.flow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.Action.Polyglot;
import net.bzk.flow.model.var.VarLv;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.convert.OType;

@Data
public class Condition implements OType {

	public static enum ConKind {

		NONE, AND, OR

	}

	private String clazz;
	private ConKind kind = ConKind.NONE;
	private Condition next ;

	public Condition() {
		clazz = this.getClass().getName();
	}



	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConditionNum extends Condition {

		private String left;
		private String right;
		private NumCheckType type;
		

		
		public static ConditionNum gen() {
			ConditionNum ans = new ConditionNum();
			ans.left = "0";
			ans.right = "0";
			ans.type = NumCheckType.equal;
			return ans;
		}

	}

	public static enum NumCheckType {
		equal, greater, greater_equal, lessthan, lessthan_equal
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConditionTxt extends Condition{
		private String left;
		private String right;
		private TxtCheckType type;
		
		
	}
	
	public static enum TxtCheckType{
		equal,startsWith,endsWith,contains
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConditionInclude extends Condition {
		private Condition include;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConditionCode extends Condition {
		private Polyglot polyglot = Polyglot.js;
		private String code;
	}

}
