package com.etstc.ekits.sys;

import java.lang.reflect.Field;

/** java.library.path 工具类 */
public interface LibraryPathUtils {

	String KEY__USR_PATHS = "usr_paths";
	String KEY__SYS_PATHS = "sys_paths";

	String KEY_PROPERTY__JAVA_LIBRARY_PATH = "java.library.path";

	/** 添加java.library.path */
	static void addLibraryPathAtRuntime(String path) throws Exception {
		if (path == null) {
			return;
		}

		Field userPathsField = ClassLoader.class.getDeclaredField(KEY__USR_PATHS);
		userPathsField.setAccessible(true);
		String[] userPaths = (String[]) userPathsField.get(null);

		StringBuilder b = new StringBuilder();
		if (userPaths != null) {
			for (String t : userPaths) {
				if (path.equals(t)) {
					continue;
				}
				b.append(t).append(";");
			}

		} else {
			b.append(System.getProperty(KEY_PROPERTY__JAVA_LIBRARY_PATH)).append(";");
		}
		b.append(path);

		System.setProperty(KEY_PROPERTY__JAVA_LIBRARY_PATH, b.toString());

		Field sysPathsField = ClassLoader.class.getDeclaredField(KEY__SYS_PATHS);
		sysPathsField.setAccessible(true);
		sysPathsField.set(null, null);
	}
}
