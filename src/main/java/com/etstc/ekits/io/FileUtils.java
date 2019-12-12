package com.etstc.ekits.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 文件工具类
 * 
 * @author vipx.o
 *
 */
public interface FileUtils {

	/** 重命名 */
	static boolean rename(File parent, String oldName, String newName) {
		File oldFile = new File(parent, oldName);
		File newFile = new File(parent, newName);
		return oldFile.renameTo(newFile);
	}

	/** 字符串写入文件 */
	static void writeTo(String content, File targetFile) throws IOException {
		if (targetFile == null || content == null) {
			return;
		}
		if (!targetFile.exists()) {
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			targetFile.createNewFile();
		}
		try (OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(targetFile, false), "utf-8")) {
			fos.append(content);
		}
	}

	/** 流内容写入文件 */
	static void writeTo(InputStream source, File targetFile) throws IOException {
		if (targetFile == null || source == null) {
			return;
		}
		if (!targetFile.exists()) {
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			targetFile.createNewFile();
		}
		try (FileOutputStream fos = new FileOutputStream(targetFile, false)) {
			int b = -1;
			while ((b = source.read()) != -1) {
				fos.write(b);
			}
		}
	}

	/** 读取文件内容写入流 */
	static void copyTo(File sourceFile, OutputStream target) throws IOException {
		if (sourceFile == null || target == null) {
			return;
		}
		if (!sourceFile.exists() || !sourceFile.isFile()) {
			return;
		}
		try (FileInputStream fis = new FileInputStream(sourceFile)) {
			int b = -1;
			while ((b = fis.read()) != -1) {
				target.write(b);
			}
		}
	}

	/** 读文件返回字符串 */
	static String readString(File sourceFile) throws Exception {
		if (sourceFile == null) {
			return null;
		}
		if (!sourceFile.exists() || !sourceFile.isFile()) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
			String buff = null;
			while ((buff = br.readLine()) != null) {
				builder.append(buff).append("\r\n");
			}
		}
		return builder.toString();
	}

	/** 读取jar包内的文件 */
	static String readJarInnerFile(String sourcePath, Class<?> clazz) throws IOException {
		InputStream is = null;
		StringBuilder builder = new StringBuilder();
		try {
			is = clazz.getResourceAsStream("/" + sourcePath);
			if (is == null) {
				is = clazz.getResourceAsStream(sourcePath);
			}
			if (is != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String buff = null;
				while ((buff = reader.readLine()) != null) {
					builder.append(buff).append("\r\n");
				}
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return builder.toString();
	}

	/** jar包内的文件保存 */
	static void saveJarInnerFile(File targetFile, String sourcePath, Class<?> clazz) throws IOException {
		InputStream is = null;
		try {
			is = clazz.getResourceAsStream("/" + sourcePath);
			if (is == null) {
				is = clazz.getResourceAsStream(sourcePath);
			}
			FileUtils.writeTo(is, targetFile);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}