package net.bzk.flow;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.bson.BsonDocument;
import org.graalvm.polyglot.Value;
import org.reflections.Reflections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

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
		T ans = replaceModel(varQueryer, aJson, modelClazz);
		return ans == null ? _m : ans;
	}

	public static <T> T replaceModel(FastVarQueryer varQueryer, String aJson, Class<T> modelClazz) {
		String rJson = replaceText(varQueryer, aJson);
		if (StringUtils.isBlank(rJson))
			return null;
		return JsonUtils.loadByJson(rJson, modelClazz);
	}

	public static String replaceText(FastVarQueryer varQueryer, String aJson) {
		List<String> keys = PlaceholderUtils.listStringSubstitutorKeys(aJson);
		if (keys.size() == 0)
			return null;
		StrPlacer sp = PlaceholderUtils.build(aJson);
		for (String k : keys) {
			Optional<Object> fo = varQueryer.f(k);
			String jo = JsonUtils.valueToString(fo.orElse(null));
			sp.place(k, StringEscapeUtils.escapeJson(jo));
		}
		String rJson = sp.replace();
		return rJson;
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

	public static Object fixPolyglotObj(Value function) {
		Object ans = function.as(Object.class);
		if (ans instanceof Map) {
			if (((Map) ans).size() > 0) {
				return toByBson(ans.toString());
			}
			if (function.getArraySize() > 0) {
				String replaceSts = removeListPrefix(ans.toString(), function.getArraySize());
				var m = toByBson(replaceSts);
				return m.get("d");
			}
		}
		return JsonUtils.toByJson(ans, Object.class);
	}

	private static String removeListPrefix(String lstr, long arraySize) {
		String startKs = "(" + arraySize + ")";
		if (!lstr.startsWith(startKs))
			throw new BzkRuntimeException("not startKs:" + startKs + " in " + lstr);
		startKs = Pattern.quote(startKs);
		String rmeds = lstr.replaceFirst(startKs, "");
		return String.format("{d:%s}", rmeds);
	}

	public static Map toByBson(String bs) {
		BsonDocument expected = BsonDocument.parse(bs);
		String json = expected.toJson();
		return JsonUtils.loadByJson(json, Map.class);
	}

}
