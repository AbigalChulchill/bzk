package net.bzk.infrastructure.obj;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import net.bzk.infrastructure.ex.BzkRuntimeException;

public class ObjUtils {

	private static ObjectMapper JSON_MAPPER = new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.setDateFormat(new StdDateFormat());

	public static String toJsonSting(Object o) {
		try {
			return JSON_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static <T> T toObjByJson(String o, Class<T> clz) {
		try {
			return JSON_MAPPER.readValue(o, clz);
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}
	}
	
	public static <T> T convertByJson(Object o, Class<T> clz) {
		String json = toJsonSting(o);
		return toObjByJson(json,clz);
	}	
	
	public static Map<String, Object> toFlatMap(Object o,String dot) {
		return JsonMap.gen(o).setPathDot(dot).parsePathValueMap();
	}
	
}
