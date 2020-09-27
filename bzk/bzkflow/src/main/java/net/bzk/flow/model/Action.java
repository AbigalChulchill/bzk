package net.bzk.flow.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Action extends BzkObj {
	
	private String name;
//	private boolean waitByApi;
	
	
	
	
	

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class RestAction extends Action{
		
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class NodejsAction extends Action{
		private String code;
		private Set<String> installs=new HashSet<String>();
		private Map<String,String> dependencies;
		private Map<String,String> devDependencies;
	}
	
}

