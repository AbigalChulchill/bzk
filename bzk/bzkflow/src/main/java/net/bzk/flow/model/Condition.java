package net.bzk.flow.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.model.var.VarLv;
import net.bzk.infrastructure.convert.OType;

@Data
public class Condition implements OType {

	public static enum ConKind {

		NONE, AND, OR

	}

	private String clazz;
	private ConKind kind;
	private Condition next;

	public Condition() {
		clazz = this.getClass().getName();
	}

	@Data
	public static class Val implements OType {
		private String clazz;

		public Val() {
			clazz = this.getClass().getName();
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class RefVal extends Val {
		private DtoVarQuery query;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class TxtVal extends Val {
		private String val;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConditionNum extends Condition {

		private Val left;
		private Val right;
		private NumCheckType type;

	}

	public static enum NumCheckType {
		equal, greater, greater_equal, lessthan, lessthan_equal
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConditionTxt extends Condition{
		private Val left;
		private Val right;
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

}
