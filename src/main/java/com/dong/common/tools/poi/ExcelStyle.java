package com.dong.common.tools.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelStyle {
	
	/**自定义单元格样式
	 * @param wb
	 * @param fontSize 字体大小
	 * @param isBold 是否加粗
	 * @param bgColor 背景色
	 * @return
	 */
	public static HSSFCellStyle getCellStyle(HSSFWorkbook wb,short fontSize,boolean isBold,Short bgColor) {
		HSSFCellStyle cellStyle = getNormalCellStyle(wb);
		if (bgColor != null) {
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	//设置填充方式
			cellStyle.setFillForegroundColor(bgColor);	//设置背景颜色
		}
		Font ztFont = wb.createFont();
        ztFont.setFontHeightInPoints(fontSize);
        if (isBold) {
        	ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
        cellStyle.setFont(ztFont);
        return cellStyle;
	}
	
	
	/**
	 * 单元格样式（12号字体，居中）
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle getNormalCellStyle(HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellStyle.setWrapText(true);	//设置自动换行
		Font ztFont = wb.createFont();     
        ztFont.setFontHeightInPoints((short)12);    //字体大小  
        cellStyle.setFont(ztFont);
		return cellStyle;
	}
	
	/**
	 * 首标题样式（灰色背景，加粗20号字体，居中）
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle getHeadBoldStyle(HSSFWorkbook wb){
		HSSFCellStyle headStyle = getNormalCellStyle(wb);
		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	//设置填充方式
		headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);	//设置背景颜色
		//titleStyle.setFillForegroundColor((short) 13);// 设置背景色    
		Font ztFont = wb.createFont();
        ztFont.setFontHeightInPoints((short)20);
        ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(ztFont);
        return headStyle;
	}
	
	/**
	 * 标题样式（灰色背景，加粗18号字体，居中）
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle getHeadStyle(HSSFWorkbook wb) {
		HSSFCellStyle titleStyle = getNormalCellStyle(wb);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);	//设置背景颜色
        Font fontStyle = wb.createFont();
        fontStyle.setFontHeightInPoints((short)18);
        titleStyle.setFont(fontStyle);
        return titleStyle;
	}
	
	/**
	 * 日期样式（yyyy-MM-dd hh:mm:ss）
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle getDateStyle(HSSFWorkbook wb){
		HSSFCellStyle dateStyle = getNormalCellStyle(wb);
		HSSFDataFormat format = wb.createDataFormat();
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm:ss"));
		return dateStyle;
	}
	
	/**
	 * 日期样式（yyyy-MM-dd）
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle getSimDateStyle(HSSFWorkbook wb){
		HSSFCellStyle dateStyle = getNormalCellStyle(wb);
		HSSFDataFormat format = wb.createDataFormat();
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
		return dateStyle;
	}
	
	 /**
	  * 拆分合并单元格
	 * @param sheet
	 * @param row 所在行
	 * @param column 所在列
	 */
	public static void removeMergedRegion(HSSFSheet sheet,int row ,int column)    
     {    
          int sheetMergeCount = sheet.getNumMergedRegions();//获取所有的单元格   
          int index = 0;//用于保存要移除的那个单元格序号  
          for (int i = 0; i < sheetMergeCount; i++) {   
           CellRangeAddress ca = sheet.getMergedRegion(i); //获取第i个单元格  
           int firstColumn = ca.getFirstColumn();    
           int lastColumn = ca.getLastColumn();    
           int firstRow = ca.getFirstRow();    
           int lastRow = ca.getLastRow();    
           if(row >= firstRow && row <= lastRow)    
           {    
            if(column >= firstColumn && column <= lastColumn)    
            {    
               index = i;  
            }    
           }    
          }  
          sheet.removeMergedRegion(index);//移除合并单元格  
     }
	
	/**
	 * 初始化表格
	 * @param sheet
	 * @param datas	数据
	 * @param cellStyle	单元格样式
	 * @param rowHeight	行高
	 */
	public static void initTable(HSSFSheet sheet,Object[][] datas,HSSFCellStyle cellStyle,int rowHeight) {
		for(int i = 0; i < datas.length; i++) {
			HSSFRow row = sheet.createRow(i);	//创建行
			row.setHeightInPoints(rowHeight);			//行高
		    for(int j = 0; j < datas[i].length; j++) {
		    	HSSFCell cell = row.createCell(j);		//创建单元格
				cell.setCellStyle(cellStyle);		//设置单元格样式
				cell.setCellValue(PdfStyle.getStringValue(datas[i][j]));	//填充单元格数据
		    }
	    }
	}
	 
}
