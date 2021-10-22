package net.bzk.flow.model.parse;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.infrastructure.convert.OType;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@SuppressWarnings("serial")
public class OTypeDeserializer<T extends OType> extends StdDeserializer<T> {
	private ObjectMapper mapper;

	public OTypeDeserializer(ObjectMapper m) {
		super(OType.class);
		mapper = m;
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			TreeNode tn = p.readValueAsTree();
			TreeNode clzn = tn.get("clazz");
			String className = clzn.toString().replace("\"", "");
			Class tc = Class.forName(className);
//			System.out.println("deserialize:" + tn.toString()+" className:"+tc);
			T ans = (T) mapper.readValue(tn.toString(), tc);
			return ans;
		} catch (ClassNotFoundException e) {
			throw new BzkRuntimeException();
		}
	}

//	public static boolean hasChild(Class<? extends BzkObj> bc, Set<Class<? extends BzkObj>> bcs) {
//		return bcs.stream().anyMatch(c ->  bc!=c &&  bc.isAssignableFrom(c));
//	}

}
