package net.bzk.flow.api.dto;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import net.bzk.flow.enums.VarLv.VarKey;

@Data
public class VarKeyReflect {

	private String srcKey;
	private VarKey toKey;

	public static boolean hasSrcKey(Collection<VarKeyReflect> list, String srcKey) {
		return list.stream().anyMatch(vkr -> StringUtils.equals(srcKey, vkr.srcKey));
	}
	
	public static Optional<VarKeyReflect> findBySrcKey(Collection<VarKeyReflect> list, String srcKey) {
		return list.stream().filter(vkr -> StringUtils.equals(srcKey, vkr.srcKey)).findFirst();
	}

}
