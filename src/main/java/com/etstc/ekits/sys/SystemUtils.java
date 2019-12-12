package com.etstc.ekits.sys;

/** 系统工具类 */
public interface SystemUtils {

	/** 系统名称,eg.: Windows 10, Linux, Max OS */
	String OS_NAME = System.getProperty("os.name");
	/** 系统位数: 32 或 64 */
	String OS_ARCH_DATA_MODEL = System.getProperty("sun.arch.data.model");
	/** 用户目录 */
	String USER_HOME = System.getProperty("user.home");

	static boolean isWindows() {
		return OS_NAME.matches("(.*)((?i)Windows)(.*)");
	}

	static boolean isLinux() {
		return OS_NAME.matches("(.*)((?i)Linux)(.*)");
	}

	static boolean isMacOS() {
		return OS_NAME.matches("(.*)((?i)Mac)(.*)");
	}

}
