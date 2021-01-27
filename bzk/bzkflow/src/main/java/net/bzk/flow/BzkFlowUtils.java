package net.bzk.flow;

import java.util.List;
import java.util.Optional;

import org.apache.commons.text.StringEscapeUtils;

import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.PlaceholderUtils;
import net.bzk.infrastructure.StrPlacer;
import net.bzk.infrastructure.ex.BzkRuntimeException;

public class BzkFlowUtils {

	public static Exception toJsonSafeExcetption(Exception e) {
		try {
			String s = JsonUtils.toJson(e);
		} catch (Exception exx) {
			return new BzkRuntimeException(e.getMessage());
		}
		return e;
	}
	
	public static <T> T replaceModel(FastVarQueryer varQueryer,T _m,Class<T> modelClazz) {
		String aJson = JsonUtils.toJson(_m);
		List<String> keys = PlaceholderUtils.listStringSubstitutorKeys(aJson);
		if (keys.size() == 0)
			return _m;

		StrPlacer sp = PlaceholderUtils.build(aJson);
		for (String k : keys) {
			Optional<Object> fo = varQueryer.f(k);
//			if (!fo.isPresent())
//				continue;
			String jo = JsonUtils.toJson(fo.orElse(null));
			sp.place(k, StringEscapeUtils.escapeJson(jo));
		}
		String rJson = sp.replace();
		return JsonUtils.loadByJson(rJson, modelClazz);

	}

}
