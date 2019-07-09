package com.etstc.ekits.mask;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 掩码工具类
 * 
 * (eg.:13301010011 -> 133****0011)
 * 
 * @author vipx.o
 */
public final class MaskUtils {

	public static final MaskUtils instance = new MaskUtils();
	private final ConcurrentHashMap<String, MaskPattern> cahce = new ConcurrentHashMap<>();

	private MaskUtils() {
		// 单例禁止外部实例化
	}

	/**
	 * 根据定义类型掩码字符串
	 * 
	 * @param typeRegex
	 *            掩码定义类型
	 * @param value
	 *            被掩码字符串
	 * @return String
	 */
	public final String mask(String typeRegex, CharSequence value) {
		return this.mask(typeRegex, value, null);
	}

	/**
	 * 根据定义类型掩码字符串
	 * 
	 * @param typeRegex
	 *            掩码定义类型
	 * @param value
	 *            被掩码字符串
	 * @param maskChar
	 *            用于掩码字符
	 * @return String
	 */
	public final String mask(String typeRegex, CharSequence value, Character maskChar) {
		MaskPattern maskp = cahce.get(typeRegex);
		if (maskp == null) {
			maskp = new MaskPattern(typeRegex);
			cahce.put(typeRegex, maskp);
		}
		return maskp.mask(value, maskChar);
	}

}
