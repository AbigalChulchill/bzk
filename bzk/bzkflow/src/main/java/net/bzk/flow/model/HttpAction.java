package net.bzk.flow.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.enums.VarLv.VarKey;

@Data
@EqualsAndHashCode(callSuper = false)
public class HttpAction extends Action {
	private String url;
	private Map<String, ?> uriVariables;
	private String body;
	private String method;
	private Headers headers;
	private String headersFlat;

	private VarKey key;
	

	
	@SuppressWarnings("serial")
	public static class Headers extends HashMap<String, List<String>>
			implements MultiValueMap<String, String>, Serializable {

		@Override
		public String getFirst(String key) {
			return get(key).get(0);
		}

		@Override
		public void add(String key, String value) {
			List<String> values = this.computeIfAbsent(key, k -> new LinkedList<>());
			values.add(value);

		}

		@Override
		public void addAll(String key, List<? extends String> values) {
			List<String> currentValues = this.computeIfAbsent(key, k -> new LinkedList<>());
			currentValues.addAll(values);

		}

		@Override
		public void addAll(MultiValueMap<String, String> values) {
			for (Entry<String, List<String>> entry : values.entrySet()) {
				addAll(entry.getKey(), entry.getValue());
			}

		}

		@Override
		public void set(String key, String value) {
			List<String> values = new LinkedList<>();
			values.add(value);
			this.put(key, values);
		}

		@Override
		public void setAll(Map<String, String> values) {
			values.forEach(this::set);
		}

		@Override
		public Map<String, String> toSingleValueMap() {
			LinkedHashMap<String, String> singleValueMap = new LinkedHashMap<>(this.size());
			this.forEach((key, values) -> {
				if (values != null && !values.isEmpty()) {
					singleValueMap.put(key, values.get(0));
				}
			});
			return singleValueMap;
		}

	}

}