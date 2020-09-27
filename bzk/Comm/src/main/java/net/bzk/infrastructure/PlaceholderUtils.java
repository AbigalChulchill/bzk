package net.bzk.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderUtils {
	
	public static List<String> getByTag(String tag,String plain) {
		String tagr = String.format("%s(.+?)%s", tag,tag);
		Pattern TAG_REGEX = Pattern.compile(tagr, Pattern.DOTALL);
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_REGEX.matcher(plain);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    return tagValues;
	}
	

}
