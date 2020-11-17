package net.bzk.flow.run.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.run.action.ActionCall.Uids;

@Service
@Scope("prototype")
public class FastVarQueryer {
	@Inject
	private RunVarService service;

	public static final String PRFIX_BOX = "@";
	public static final String PRFIX_FLOW = "~";

	@Getter
	private Uids uids;

	public FastVarQueryer init(Uids u) {
		uids = u;
		return this;
	}

	/**
	 * 
	 * @param fqe $= box #= flow {empty}= not spect
	 * @return
	 */
	public Object g(String fqe) {
		return f(fqe).get();
	}

	public Optional<Object> f(String fqe) {
		DtoVarQuery dq = new DtoVarQuery();
		FqeStruct fs = checkLvByProfix(fqe);
		dq.setKey(fs.key);
		dq.setPoint(fs.lv);
		dq.setUids(uids);
		return service.findByQuery(dq);
	}

	private FqeStruct checkLvByProfix(String fqe) {
		if (fqe.startsWith(PRFIX_BOX))
			return new FqeStruct(VarLv.run_box, fqe.replace(PRFIX_BOX, ""));
		if (fqe.startsWith(PRFIX_FLOW))
			return new FqeStruct(VarLv.run_flow, fqe.replace(PRFIX_FLOW, ""));
		return new FqeStruct(VarLv.not_specify, fqe);
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FqeStruct {

		private VarLv lv;
		private String key;

	}

}
