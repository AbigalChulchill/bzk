package net.bzk.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import net.bzk.infrastructure.ex.BzkRuntimeException;
import net.bzk.infrastructure.obj.JsonMap;

public class JsonUtils {

	public static enum DataType {
		string, number, Boolean, NULL, object,JSONVal, array, NotSupport,
	}

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static <T> T toByJson(Object src, Class<T> clz) {
		try {
			String json = JSON_MAPPER.writeValueAsString(src);
			return JSON_MAPPER.readValue(json, clz);
		} catch (Exception e) {
			throw new BzkRuntimeException("toByJson src:" + src + " clz:" + clz, e);
		}
	}

	public static String toJson(Object o) {
		try {
			return JSON_MAPPER.writeValueAsString(o);
		} catch (Exception e) {
			throw new BzkRuntimeException("toJson O:" + o, e);
		}
	}
	
	public static String toPrettyJson(Object o) {
		try {
			return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			throw new BzkRuntimeException("toJson O:" + o, e);
		}
	}

	public static <T> T loadByJson(String json, Class<T> clz) {
		try {
			return JSON_MAPPER.readValue(json, clz);
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static DataType checkDataType(String v) {
		if (StringUtils.isBlank(v))
			return DataType.NULL;
		if (StringUtils.equals("true", v.toLowerCase()))
			return DataType.Boolean;
		if (StringUtils.equals("false", v.toLowerCase()))
			return DataType.Boolean;
		if (CommUtils.isNumeric(v))
			return DataType.number;
		if (isJsonObject(v))
			return DataType.object;
		if (isJsonArray(v))
			return DataType.array;
		if (isJsonVal(v))
			return DataType.JSONVal;		
		if (StringUtils.isNotBlank(v))
			return DataType.string;

		return DataType.NotSupport;

	}

	public static boolean isJsonVal(String v) {
		try {
			loadByJson(v, Object.class);
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	public static boolean isJsonObject(String v) {
		try {
			JSONObject jo = new JSONObject(v);
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	public static String valueToString(Object o) {
		if(o instanceof Number || o instanceof String || o instanceof Boolean ) {
			return o+"";
		}
		return toJson(o);
	}

	public static Object stringToValue(String v) {
		DataType d = checkDataType(v);
		switch (d) {
		case array:
			return JsonUtils.loadByJson(v, List.class);
		case Boolean:
			return Boolean.valueOf(v);
		case NotSupport:
			throw new BzkRuntimeException(v + ": NotSupport");
		case NULL:
			return null;
		case number:
			return Double.parseDouble(v);
		case object:
			return JsonMap.gen(JsonUtils.loadByJson(v, Object.class));
		case JSONVal:
			return JsonUtils.loadByJson(v, Object.class);
		case string:
			return v;
		}
		throw new BzkRuntimeException(v + ": NotSupport:" + d);
	}

	public static boolean isJsonArray(String v) {
		try {
			JSONArray jo = new JSONArray(v);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	public static Object findByJsonPath(Object o, String exp) {
		Object outo = JsonPath.read(JsonUtils.toJson(o), exp);
		String ss = toJson(outo);
		return stringToValue(ss);
	}

	public static void main(String[] args) {
		
		String itest = "\"123\"";
		Object o = stringToValue(itest);
		System.out.println(o.getClass()+" "+o);
		
		Object a = toJson(123);
		System.out.println(a);
		System.out.println(a.getClass());
		String ints = "123";
		a = loadByJson(ints, Integer.class);
		System.out.println(a);
		System.out.println(a.getClass());
		String bc = toJson("{ \"abc\": ");
		a = loadByJson(bc, Object.class);
		System.out.println(bc);
		System.out.println(a);
		System.out.println(a.getClass());
		
		Map m = new HashMap<String,String>();
		m.put("q", "a");
		net.minidev.json.JSONArray ss = JsonPath.read(JsonUtils.toJson(m), "$.q");

		System.out.println(ss);
		
	}

}
