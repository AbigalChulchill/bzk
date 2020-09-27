package net.bzk.infrastructure.convert;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author kirin
 *
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface OverwriteFieldSkip {
	
	String[] skipKeys() default StringUtils.EMPTY;

}
