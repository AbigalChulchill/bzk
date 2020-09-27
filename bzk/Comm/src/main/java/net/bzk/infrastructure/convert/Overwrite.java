package net.bzk.infrastructure.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.bzk.infrastructure.ReflectionTool;
import net.bzk.infrastructure.ex.BzkRuntimeException;

public abstract class Overwrite<T, R> implements Function<T, R> {

	@Override
	public R apply(T t) {
		R r = newResultInstance();
		try {
			reflection(t, r);
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}
		convertCustom(t, r);
		return r;
	}

	protected void convertCustom(T t, R r) {

	}

	private void reflection(T t, R r)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		List<String> names = listNames(r);
		for (String n : names) {
			if (ReflectionTool.hasFieldKey(t.getClass(), n)) {

				Object o = ReflectionTool.getValue(t, n);
				if (o != null) {
					ReflectionTool.putFieldValue(r, n, o);
				}
			}
		}
	}

	private List<String> listNames(R r) {
		OverwriteIncludeClass cic = r.getClass().getAnnotation(OverwriteIncludeClass.class);
		if (cic == null) {
			return ReflectionTool.getFieldNamesNegative(r.getClass(), OverwriteFieldSkip.class);
		} else {
			List<String> ans = new ArrayList<>();
			for (Class cs : cic.include()) {
				ans.addAll(ReflectionTool.getFieldNamesNegative(cs, OverwriteFieldSkip.class));
			}
			return ans;
		}

	}

	protected abstract R newResultInstance();

	public static <T, R> R overwrite(T in, R overwrited) {
		Overwrite<T, R> ow = new Overwrite<T, R>() {
			@Override
			protected R newResultInstance() {
				return overwrited;
			}
		};
		return ow.apply(in);
	}

}