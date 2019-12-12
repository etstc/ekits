package com.etstc.ekits.office;

import java.io.File;

import com.etstc.ekits.io.FileUtils;
import com.etstc.ekits.sys.LibraryPathUtils;
import com.etstc.ekits.sys.SystemUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public final class OfficeTransform {

	private static OfficeTransform instance = null;

	private static final String JACOB_DIR = "jacob";
	private static final String JACOB = "jacob-1.19-x" + SystemUtils.OS_ARCH_DATA_MODEL + ".dll";
	private static final String JACOB_PATH = JACOB_DIR + "/" + JACOB;

	private static volatile boolean inied = false;

	private OfficeTransform() {
		// 禁止外部实例化
		init();
	}

	private void init() {
		if (inied) {
			return;
		}
		synchronized (OfficeTransform.class) {
			if (inied) {
				return;
			}
			if (!SystemUtils.isWindows()) {
				throw new RuntimeException(
						"OfficeTransform Can Only Run On Windows. Current OS:" + SystemUtils.OS_NAME);
			}
			try {
				File local = new File(".");
				File jacobDir;
				if (local.canWrite()) {
					jacobDir = new File(local, JACOB_DIR);
				} else {
					jacobDir = new File(SystemUtils.USER_HOME, JACOB_DIR);
				}
				if (!jacobDir.exists()) {
					jacobDir.mkdirs();
				}
				File jaconFile = new File(jacobDir, JACOB);
				if (!jaconFile.exists()) {
					jaconFile.createNewFile();
					FileUtils.saveJarInnerFile(jaconFile, JACOB_PATH, OfficeTransform.class);
				}
				// 添加到java.library.path
				LibraryPathUtils.addLibraryPathAtRuntime(jaconFile.getParent());
				inied = true;
			} catch (Exception e) {
				throw new RuntimeException("OfficeTransform init Error.", e);
			}
		}
	}

	public static OfficeTransform getInstance() {
		if (instance == null) {
			synchronized (OfficeTransform.class) {
				if (instance == null) {
					instance = new OfficeTransform();
				}
			}
		}
		return instance;
	}

	private static final int wdFormatPDF = 17;// PDF 格式

	public void toPdf(String word, String pdf) {
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			doc = Dispatch.call(docs, "Open", word).toDispatch();
			File tofile = new File(pdf);
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.call(doc, "SaveAs", pdf, wdFormatPDF);
		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			if (doc != null) {
				Dispatch.call(doc, "Close", false);
			}
			if (app != null) {
				app.invoke("Quit", new Variant[] {});
			}
		}
		// 结束后关闭进程
		ComThread.Release();
	}
}
