package com.dong.common.tools.poi;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class PdfStyle {
	public static Font NORMAL_FONT = new Font(getChineseFont(), 12,Font.NORMAL);
	public static Font HEAD_FONT = new Font(getChineseFont(), 18,Font.NORMAL);
	public static Font HEAD_BOLD_FONT = new Font(getChineseFont(), 20,Font.BOLD);
	
	private static PdfPTable table;
	private static int rowHeight;
	private static Font font;
	
	/**
	 * 初始化必须的参数，在初始化表格前执行
	 * @param table
	 * @param rowHeight 行高
	 * @param font	字体
	 */
	public static void initStyle(PdfPTable table,int rowHeight,Font font){
		PdfStyle.table = table;
		PdfStyle.rowHeight = rowHeight;
		PdfStyle.font = font;
	}
	
	/**
	 * 初始化表格
	 * @param datas	数据
	 * @param cols	合并单元格的数据，参数为{所在行, 所在列, 合并个数}
	 * @param map	自定义样式，参数为{所在行，单元格}
	 */
	public static void initTable(Object[][] datas,int[][] cols,Map<Integer, PdfPCell> map) {
		if (table != null) {
			for(int i = 0; i < datas.length; i++) {
				for(int j = 0; j < datas[i].length; j++) {
					PdfPCell pdfPCell = new PdfPCell();
					//设置单元格公共样式
					pdfPCell.setMinimumHeight(rowHeight);//设置表格行高
					pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//填充数据
					String value = PdfStyle.getStringValue(datas[i][j]);
					Paragraph para = new Paragraph(value, font);
					pdfPCell.setPhrase(para);
					//设置特殊样式
					for (Entry<Integer, PdfPCell> entry : map.entrySet()) { 
						if (i == entry.getKey()) {
							pdfPCell = entry.getValue();
						}
					}
					//合并单元格
					for(int a = 0;a < cols.length; a++){
						if (i == cols[a][0]) {
							if (j == cols[a][1]) {
								pdfPCell.setColspan(cols[a][2]);
							}
						}
					}
					table.addCell(pdfPCell);
				}
			}
		}
	}
	
	/**
	 * 创建自定义样式的单元格
	 * @param value
	 * @param rowHeight
	 * @param font
	 * @param color
	 * @return
	 */
	public static PdfPCell getPdfpCell(String value,int rowHeight,Font font,BaseColor color) {
		PdfPCell pdfPCell = new PdfPCell();
        //设置单元格公共样式
        pdfPCell.setMinimumHeight(rowHeight);//设置表格行高
        pdfPCell.setBackgroundColor(color);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //填充数据
        Paragraph para = new Paragraph(value, font);
        pdfPCell.setPhrase(para);
        return pdfPCell;
	}
	
	/**
	 * 将不同类型对象转换成String
	 */
	public static String getStringValue(Object object) {
		if (object instanceof String) {
			return (String) object;
		}else if(object instanceof Integer){
			return Integer.toString((int) object);
		}else if(object instanceof BigDecimal){
			return object.toString();
		}else if (object instanceof Date) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(object);
		}
		return null;
	}
	
	/**
	 * 得到中文字体
	 */
	public static BaseFont getChineseFont() {
		BaseFont bfChinese;
		try {
			bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			return bfChinese;
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
