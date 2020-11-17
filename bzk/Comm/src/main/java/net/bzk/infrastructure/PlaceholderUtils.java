package net.bzk.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderUtils {

	public static StrPlacer build(String p) {
		return StrPlacer.build(p);
	}

	/**
	 * ${xxx}
	 * 
	 * @param txt Hi ${name.var}, how are you? I'm ${namevar2}.test ${!namevar2}.
	 *            ${~namev.ar2}.
	 * @return
	 */
	public static List<String> listStringSubstitutorKeys(String txt) {
		return listPlaceHolderKeys(txt, "\\$\\{", "\\}");
	}

	public static List<String> listPlaceHolderKeys(String txt, String start, String end) {
		String pex = String.format("%s([\\w . ! ~]*?)%s", start, end);
		Pattern regx = Pattern.compile(pex, Pattern.DOTALL);
		final List<String> ans = new ArrayList<String>();
		final Matcher matcher = regx.matcher(txt);
		while (matcher.find()) {
			ans.add(matcher.group(1));
		}
		return ans;
	}

}
