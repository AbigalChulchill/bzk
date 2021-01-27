package net.bzk.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.bzk.infrastructure.ex.BzkRuntimeException;

public class CommUtils {

	public static final Base64 base64 = new Base64();

	public static <T> boolean hasChild(Class<? extends T> bc, Set<Class<? extends T>> bcs) {
		return bcs.stream().anyMatch(c -> bc != c && bc.isAssignableFrom(c));
	}

	public static <T> T orElse(T t, T nt) {
		return t != null ? t : nt;
	}

	public static <T> T orOneElse(List<T> t, T nt) {
		return t != null && t.size() > 0 ? t.get(0) : nt;
	}

	public static <T> T orElse(Supplier<T> t, T nt) {
		try {
			return orElse(t.get(), nt);
		} catch (Exception e) {
			return nt;
		}
	}

	public static void pl(Object o) {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

		System.out.println(String.format("%s.%s (%s) : %s", className, methodName, lineNumber, o));
	}

	public static String serializeToString(Serializable object) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);) {
			oos.writeObject(object);
			oos.close();
			return new String(base64.encode(baos.toByteArray()));
		}
	}

	public static String encodeBase64(String code) {
		return base64.encodeToString(code.getBytes());
	}

	public static String decodeBase64(String code) {
		try {
			return new String(base64.decode(code), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new BzkRuntimeException("node decode :" + code);
		}
	}

	public static String sha1(Serializable o, int max) {
		try {
			String sos = serializeToString(o);
			String sha1 = sha1(sos, max);
			return sha1;
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static String sha1(String input) {
		return DigestUtils.sha1Hex(input);
	}

	public static String sha1(String input, int max) {
		String sha1 = sha1(input);
		int sl = sha1.length() < max ? sha1.length() : max;
		return sha1.substring(0, sl - 1);
	}

	public static String loadBy(Resource r) {
		try {
			try (InputStream is = r.getInputStream()) {
				StringWriter writer = new StringWriter();
				IOUtils.copy(is, writer, StandardCharsets.UTF_8);
				return writer.toString();
			}
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
