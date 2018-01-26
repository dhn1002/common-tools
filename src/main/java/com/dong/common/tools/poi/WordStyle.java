package com.dong.common.tools.poi;

import com.lowagie.text.*;
import com.lowagie.text.Font;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class WordStyle {
	public static Font NORMAL_FONT = new Font(null,12,Font.NORMAL);
	public static Font HEAD_FONT = new Font(null,18,Font.NORMAL);
	public static Font HEAD_BOLD_FONT = new Font(null,20,Font.BOLD);
	
	private static Table table;
	private static Font font;
	
	/**
	 * 初始化必须的参数，在初始化表格前执行，word不能设置行高
	 * @param table
	 * @param font	字体
	 */
	public static void initStyle(Table table,Font font){
		WordStyle.table = table;
		WordStyle.font = font;
	}
	
	/**
	 * 初始化表格
	 * @param datas	数据
	 * @param cols	合并单元格的数据，参数为{所在行, 所在列, 合并个数}
	 * @param map	自定义样式，参数为{所在行，单元格}
	 */
	public static void initTable(Object[][] datas,int[][] cols,Map<Integer, Cell> map) {
		if (table != null) {
			for(int i = 0; i < datas.length; i++) {
				for(int j = 0; j < datas[i].length; j++) {
					//获取数据
			        String value = getStringValue(datas[i][j]);
			        Paragraph para = new Paragraph(value, font);
			        //创建单元格
			        Cell cell = null;
					try {
						cell = new Cell(para);
					} catch (BadElementException e) {
						e.printStackTrace();
					}
			        //设置单元格公共样式
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					//设置特殊样式
					for (Entry<Integer, Cell> entry : map.entrySet()) { 
						if (i == entry.getKey()) {
							cell = entry.getValue();
						}
					}
					//合并单元格
					for(int a = 0;a < cols.length; a++){
						if (i == cols[a][0]) {
							if (j == cols[a][1]) {
								cell.setColspan(cols[a][2]);
							}
						}
					}
					table.addCell(cell);
				}
			}
		}
	}
	
	/**
	 * 创建自定义样式的单元格
	 * @param value
	 * @param font
	 * @param color
	 * @return
	 */
	public static Cell getCell(String value,Font font,Color color) {
        //创建单元格
        Cell cell = new Cell();
        //设置单元格公共样式
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(color);
        //获取数据
        Paragraph para = new Paragraph(value, font);
        cell.add(para);
        return cell;
	}
	
	/**
	 * 将不同类型对象转换成String
	 */
	public static String getStringValue(Object object) {
		if (object instanceof String) {
			return (String) object;
		}else if(object instanceof Integer){
			return object.toString();
		}else if(object instanceof BigDecimal){
			return object.toString();
		}else if (object instanceof Date) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(object);
		}
		return null;
	}

}
