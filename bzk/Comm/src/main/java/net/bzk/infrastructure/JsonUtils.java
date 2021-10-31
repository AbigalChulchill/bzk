package net.bzk.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
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
		String oStr = "{\\r\\n    \\\"lastAt\\\": \\\"2021-10-31T02:20:30.875000+00:00\\\",\\r\\n    \\\"origQty\\\": 1.128,\\r\\n    \\\"avgPrice\\\": 4530.936976950355,\\r\\n    \\\"orders\\\": [\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_LOSS-SSwh-tkst_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509875807370,\\r\\n            \\\"origQty\\\": 0.127,\\r\\n            \\\"price\\\": 0.0,\\r\\n            \\\"reduceOnly\\\": true,\\r\\n            \\\"side\\\": \\\"BUY\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 4665.11,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"STOP_MARKET\\\",\\r\\n            \\\"updateTime\\\": 1635646830875,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"STOP_MARKET\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-31T02:20:30.875000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_GqwI-bneck-bot_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672234,\\r\\n            \\\"origQty\\\": 0.351,\\r\\n            \\\"price\\\": 4567.45,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768618,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.618000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_bneck-bot-njrD_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672199,\\r\\n            \\\"origQty\\\": 0.273,\\r\\n            \\\"price\\\": 4522.23,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768513,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.513000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_bneck-bot-brEo_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672161,\\r\\n            \\\"origQty\\\": 0.212,\\r\\n            \\\"price\\\": 4477.45,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768406,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.406000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_TrsA-bneck-bot_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672135,\\r\\n            \\\"origQty\\\": 0.165,\\r\\n            \\\"price\\\": 4433.12,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768304,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.304000+00:00\\\"\\r\\n        }\\r\\n    ],\\r\\n    \\\"group\\\": null,\\r\\n    \\\"groupMap\\\": {}\\r\\n}";
		oStr = StringEscapeUtils.unescapeJson(oStr);
		Object rob = JsonUtils.stringToValue(oStr);
		System.out.println(rob);

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
