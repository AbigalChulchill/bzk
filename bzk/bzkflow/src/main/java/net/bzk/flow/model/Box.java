package net.bzk.flow.model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.var.VarMap;

@Data
@EqualsAndHashCode(callSuper = false)
public class Box extends BzkObj {
	private String name = "Box";
	private List<Action> actions = new CopyOnWriteArrayList<>();
	private List<Link> links = new CopyOnWriteArrayList<>();
	private VarMap vars = new VarMap();
	private List<String> taskSort = new CopyOnWriteArrayList<>();

	@JsonIgnore
	public Optional<Action> findAction(String aUid) {
		return actions.stream().filter(a -> StringUtils.equals(aUid, a.getUid())).findFirst();

	}

}
