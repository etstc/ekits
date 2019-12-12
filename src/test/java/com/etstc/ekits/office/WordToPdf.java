package com.etstc.ekits.office;

public class WordToPdf {

	public static void main(String[] args) throws Exception {
		String a = "K:/资料/关键技术.docx";
		String b = "K:/test.pdf";
		OfficeTransform.getInstance().toPdf(a, b);
	}

}
