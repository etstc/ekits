package com.etstc.ekits.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 * 
 * @author vipx.o
 *
 */
public interface ZipUtils {
	/** 压缩 */
	static void zip(File source, File target) throws IOException {
		if (source == null || !source.exists()) {
			throw new RuntimeException("zip: Source File is null or not exists");
		} else if (target == null) {
			throw new RuntimeException("zip: Target File is null");
		} else if (target.getAbsolutePath().contains(source.getAbsolutePath())) {
			throw new RuntimeException("zip: Target zip File in Source Directory");
		}
		if (!target.exists()) {
			if (!target.getParentFile().exists()) {
				target.getParentFile().mkdirs();
			}
			target.createNewFile();
		}
		try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target, false)))) {
			String root = null;
			if (source.isFile()) {
				root = source.getParent();
			} else {
				root = source.getAbsolutePath();
			}
			zip(root, source, zos);
		}
	}

	/** 压缩 */
	static void zip(String rooDir, File source, ZipOutputStream zos) throws IOException {
		if (source == null || zos == null) {
			return;
		}
		if (rooDir == null && (rooDir = source.getParent()) == null) {
			rooDir = "";
		}

		if (source.isFile()) {
			String name = source.getAbsolutePath();
			if (name.startsWith(rooDir)) {
				int start = rooDir.length();
				if (!rooDir.endsWith(File.separator)) {
					start += File.separator.length();
				}
				name = name.substring(start);
			}
			ZipEntry ze = new ZipEntry(name);
			zos.putNextEntry(ze);

			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source))) {
				int b = -1;
				while ((b = bis.read()) != -1) {
					zos.write(b);
				}
				zos.closeEntry();
			}
		} else {
			File[] children = source.listFiles();
			for (File f : children) {
				zip(rooDir, f, zos);
			}
		}
	}

	/** 解压缩 */
	static void unzip(InputStream zis, File targetDir) throws IOException {
		if (zis == null) {
			throw new RuntimeException("unzip: Source Input Stream is null");
		} else if (targetDir == null) {
			throw new RuntimeException("unzip: Target Directory is null");
		}
		try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(zis))) {
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				File path = new File(targetDir, ze.getName());
				if (ze.isDirectory()) {
					if (!path.exists()) {
						path.mkdirs();
					}
				} else {
					try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path, false))) {
						int b = -1;
						while ((b = zin.read()) != -1) {
							bos.write(b);
						}
						zin.closeEntry();
					}

				}
			}
		}
	}
}
