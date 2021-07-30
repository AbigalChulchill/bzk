package net.bzk.flow.model.var;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.bzk.flow.enums.VarLv;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import net.bzk.infrastructure.obj.JsonMap;

@SuppressWarnings("serial")
public class VarMap extends JsonMap {

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

	public static interface VarsDao {
		VarMap getVarMapByUid(String uid);
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class ProcVars {
		private JsonMap sys;
		private VarMap flow;
		private VarMap box;


		public Optional<Object> find(VarLv lv, String key) {
			Object so = sys.getByPath(key);
			Object fo = flow.getByPath(key);
			Object bo = box.getByPath(key);
			if (lv == VarLv.run_sys)
				return Optional.ofNullable(so);
			if (lv == VarLv.run_flow)
				return Optional.ofNullable(fo);
			if (lv == VarLv.run_box)
				return Optional.ofNullable(bo);
			if (lv == VarLv.not_specify) {
				if (bo != null)
					return Optional.of(bo);
				if (fo != null)
					return Optional.of(fo);
				if (so != null)
					return Optional.of(so);
			}
			return Optional.empty();
		}

		public void put(VarLv lv, String key, Object o) {
			if (lv == VarLv.run_sys) {
				sys.putByPath(key, o);
				return;
			}
			if (lv == VarLv.run_flow) {
				flow.putByPath(key, o);
				return;
			}
			if (lv == VarLv.run_box || lv == VarLv.not_specify) {
				box.putByPath(key, o);
				return;
			}
			throw new BzkRuntimeException("not support :" + lv);
		}
	}

}
