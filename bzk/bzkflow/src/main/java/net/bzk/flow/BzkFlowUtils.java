package net.bzk.flow;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.reflections.Reflections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import net.bzk.flow.model.parse.OTypeDeserializer;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.PlaceholderUtils;
import net.bzk.infrastructure.StrPlacer;
import net.bzk.infrastructure.convert.OType;
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

	public static <T> T replaceModel(FastVarQueryer varQueryer, T _m, Class<T> modelClazz) {
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

	public static ObjectMapper getFlowJsonMapper() {
		ObjectMapper ans = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		Reflections reflections = new Reflections("net.bzk");
		Set<Class<? extends OType>> classes = reflections.getSubTypesOf(OType.class);
		List<Class<? extends OType>> incs = classes.stream().filter(c -> CommUtils.hasChild(c, classes))
				.collect(Collectors.toList());
		CommUtils.pl(JsonUtils.toJson(incs));
		for (Class<? extends OType> c : incs) {
			module.addDeserializer(c, new OTypeDeserializer(ans));
		}
		ans.registerModule(module);
		CommUtils.pl("Init getFlowJsonMapper " + ans);
		return ans;
	}

}
