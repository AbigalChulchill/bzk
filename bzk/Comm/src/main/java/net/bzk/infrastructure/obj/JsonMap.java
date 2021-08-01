package net.bzk.infrastructure.obj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import net.minidev.json.JSONUtil;

@SuppressWarnings("serial")
public class JsonMap extends ConcurrentHashMap<String, Object> {

	public static ObjectMapper jsonMapper = new ObjectMapper();
	private String pathDot = ".";
	private static final String ALL_SELECT = "__ALL__";

	public JsonMap() {
		super();
	}

	public JsonMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public JsonMap(int initialCapacity) {
		super(initialCapacity);
	}

	public JsonMap(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	public boolean isJsonMap(String k) {
		return get(k) instanceof Map;
	}

	public JsonMap setPathDot(String dot) {
		pathDot = dot;
		return this;
	}

	public JsonMap getMap(String k) {
		Map child = (Map) get(k);
		if (child instanceof JsonMap)
			return (JsonMap) child;
		JsonMap ans = new JsonMap();
		for (Object ck : child.keySet()) {
			if (!(ck instanceof String))
				throw new BzkRuntimeException("this key must be string");
			ans.put(ck.toString(), child.get(ck));
		}
		return ans;
	}

	public void merge(Collection<PathVal> pvs) {
		pvs.forEach(pv -> {
			putByPath(pv.getPath(), pv.findVal());
		});
	}

	public Collection<PathVal> parsePathValue() {
		return parsePathValueMap().entrySet().stream().map(f -> new BasePV(f.getKey(), f.getValue()))
				.collect(Collectors.toList());
	}

	public Map<String, Object> parsePathValueMap() {
		Map<String, Object> ans = new HashMap<>();
		parsePathValueRecursively(StringUtils.EMPTY, this, ans);
		return ans;
	}

	public String hashUid() {
		Set<String> set = new TreeSet<String>(keySet());
		StringBuilder sb = new StringBuilder();
		set.forEach(k -> sb.append("@" + k + ":" + get(k) + "@"));
		String sha1 = CommUtils.sha1(sb.toString());
		int count = Math.min(16, sha1.length());
		return sha1.substring(0, count);
	}

	private void parsePathValueRecursively(String _path, JsonMap _childMap, Map<String, Object> ans) {
		for (String k : _childMap.keySet()) {
			Object v = _childMap.get(k);
			String path = _path + k;
			if (_childMap.isJsonMap(k)) {
				parsePathValueRecursively(path + pathDot, _childMap.getMap(k), ans);
			} else {
				ans.put(path, v);
			}
		}
	}

	/**
	 * @param exp {@link "https://github.com/json-path/JsonPath"}
	 * @return
	 */
	public Object findByJsonPath(String exp) {
		return JsonUtils.findByJsonPath(this, exp);
	}

	public Object getByPath(String path) {
		return findByPath(path, false, (jm, k) -> jm.get(k));
	}

	@SuppressWarnings("unchecked")
	public void putByPath(String path, Object o) {
		findByPath(path, true, (jm, k) -> {
			try {
				if(o == null) {
					jm.remove(k);
					return null;
				}
				jm.put(k, o);
			} catch (Exception e) {
				throw new BzkRuntimeException(e);
			}
			return null;
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object findByPath(String path, boolean createPath, ChildAction<Map, String> ca) {
		String[] ps = path.split("\\" + pathDot);
		if (ps.length <= 0) {
			return ca.accept(this, path);
		}
		Map jm = this;
		if (StringUtils.equals(path,ALL_SELECT)) return jm;
		int i = 0;

		while (i < ps.length - 1) {
			if (createPath && (!jm.containsKey(ps[i]) || jm.get(ps[i]) == null)) {
				jm.put(ps[i], new JsonMap());
			}
			if (isJsonPathExp(ps[i])) {
				return findByJsonPath(ps, i, jm);
			}
			if (jm == null)
				return null;
			jm = (Map) jm.get(ps[i].trim());

			i++;
		}
		if (jm == null)
			return null;
		return ca.accept(jm, ps[i]);
	}

	private Object findByJsonPath(String[] ps, int sti, Map jm) {
		String orgQ = "";
		for (int i = sti; i < ps.length; i++) {
			if (i != sti)
				orgQ += pathDot;
			orgQ += ps[i];
		}
		return JsonUtils.findByJsonPath(jm, orgQ);
	}

	private boolean isJsonPathExp(String path) {
		if (StringUtils.isBlank(path))
			return false;
		return path.startsWith("$");
	}

	@FunctionalInterface
	public interface ChildAction<M, K> {
		Object accept(M map, K key);
	}

	public interface PathVal {
		String getPath();

		Object findVal();
	}

	public static class BasePV implements PathVal {

		private String path;
		private Object value;

		public BasePV(String path, Object value) {
			this.path = path;
			this.value = value;
		}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public Object findVal() {
			return value;
		}

	}

	public <T> T toObj(Class<T> clz) {
		return ObjUtils.convertByJson(this, clz);
	}

	public static JsonMap gen(Object o) {
		if (o == null)
			return new JsonMap();
		if (o instanceof Map) {
			JsonMap ans = new JsonMap();
			ans.putAll((Map) o);
			return ans;
		}
//		return mapper.map(o, JsonMap.class);
		return ObjUtils.convertByJson(o, JsonMap.class);
	}

	public static class JsonMapList extends ArrayList<JsonMap> {
	}

}