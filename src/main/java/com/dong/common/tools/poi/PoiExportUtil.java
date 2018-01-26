package com.dong.common.tools.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author dhn
 *
 */
public class PoiExportUtil {
	
	/**
	 * 创建空白单元格
	 * @param cellStyle 风格
	 * @param row
	 * @param size 数量
	 */
	public static void createEmptyCell(HSSFCellStyle cellStyle,HSSFRow row,int size){
		for (int i = 0; i < size; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
	}
	
	/**
	 * 创建带有边框的空白单元格
	 * @param wb
	 * @param row
	 * @param size 单元格数量
	 */
	public static void createEmptyCell(HSSFWorkbook wb,HSSFRow row,int size){
		createEmptyCell(getCellStyle(wb),row,size);
	}
	
	/**创建带有边框的单元格
	 * @param wb
	 * @param row
	 * @param colNum 单元格列号
	 * @param value 单元格内容
	 * @return
	 */
	public static HSSFCell createBorderCell(HSSFWorkbook wb,HSSFRow row,int colNum,String value) {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellValue(value);
		HSSFCellStyle style = PoiExportUtil.getCellStyle(wb);
		cell.setCellStyle(style);
		return cell;
	}
	
	/**
	 * 通过多个字符串创建单元格
	 * @param cellStyle
	 * @param row
	 * @param array
	 */
	public static void createCellByStrings(HSSFCellStyle cellStyle,HSSFRow row,String... array){
		for (int i = 0; i < array.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(array[i]);
			cell.setCellStyle(cellStyle);
		}
	}
	
	/**
	 * 通过多个字符串创建单元格（带边框居中风格）
	 * @param wb
	 * @param row
	 * @param array
	 */
	public static void createCellByStrings(HSSFWorkbook wb,HSSFRow row,String... array){
		HSSFCellStyle style = getCellStyle(wb);
		for (int i = 0; i < array.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(array[i]);
			cell.setCellStyle(style);
		}
	}
	
	/**
	 * 通过实体类创建单元格,限制列数
	 * @param row
	 * @param model 实体
	 * @param cellStyle 风格
	 * @param startCol 开始列
	 * @param endCol 结束列
	 */
	public static void createCellByModel(HSSFRow row,Object model,HSSFCellStyle cellStyle,int startCol,int endCol){
		 // 获取实体类的所有属性，返回Field数组 
	     Field[] fields = model.getClass().getDeclaredFields();
	     int size = endCol-startCol+1;
	     if (endCol == 0) {
	    	 size = fields.length;
		}
	     // 遍历所有属性 
	     for (int i = 0; i < size; i++) {
	    	 // 获取属性的名字
	    	 String name = fields[i].getName();	
	    	 // 将属性的首字符大写
	    	 name = name.substring(0, 1).toUpperCase() + name.substring(1);  
	    	// 获取属性的类型  
             String type = fields[i].getGenericType().toString(); 
	    	// 调用getter方法获取属性值 
             if(type.equals("class java.lang.String") || type.equals("class java.lang.Integer") || type.equals("class java.lang.Short") || type.equals("class java.lang.Double") || type.equals("class java.lang.Boolean") || type.equals("class java.util.Date")){
            	 String value = null;
            	 try {
            		 Method m = model.getClass().getMethod("get" + name);
            		 if (m.invoke(model) != null) {
            			 value = m.invoke(model).toString();
					}
            	 } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
            			 | InvocationTargetException e) {
            		 e.printStackTrace();
            	 }
            	 //单元格
            	 HSSFCell cell = row.createCell(i+startCol);
//            	 HSSFRichTextString richValue = new HSSFRichTextString(value);
            	 cell.setCellValue(value);
            	 cell.setCellStyle(cellStyle);
             }
		}
	}
	
	public static void createCellByModel(HSSFRow row,Object model){
		createCellByModel(row, model, null,0,0);
	}
	
	/**
	 * 通过实体列表创建单元格
	 * @param sheet
	 * @param modelList 数据
	 * @param cellStyle 风格
	 * @param startRow 开始行序号
	 * @param startCol 开始列序号
	 * @param endCol 结束列序号，0为不限制
	 * @param rowHeight 行高
	 */
	public static void createCellByModelList(HSSFCellStyle cellStyle,HSSFSheet sheet,List<?> modelList,int startRow,Integer startCol,Integer endCol,int rowHeight) {
		for (int i = 0; i < modelList.size(); i++) {
			HSSFRow row = sheet.getRow(i+startRow);
			if (row == null) {
				row = sheet.createRow(i+startRow);
				if (rowHeight != 0) {
					row.setHeightInPoints(rowHeight);
				}
			}
			
			Object model = modelList.get(i);
			createCellByModel(row,model,cellStyle,startCol,endCol);
		}
	}
	
	/**
	 * 通过实体列表创建单元格
	 * @param wb
	 * @param sheet
	 * @param modelList
	 * @param startRow 开始行序号
	 */
	public static void createCellByModelList(HSSFWorkbook wb,HSSFSheet sheet,List<?> modelList,int startRow) {
		createCellByModelList(getCellStyle(wb),sheet, modelList, startRow, 0,0,0);
	}
	
	/**通过实体列表创建单元格
	 * @param wb
	 * @param sheet
	 * @param modelList
	 */
	public static void createCellByModelList(HSSFWorkbook wb,HSSFSheet sheet,List<?> modelList) {
		createCellByModelList(getCellStyle(wb),sheet, modelList, 0, 0, 0, 0);
	}
	
	
	/**
	 * 单元格样式
	 * @param wb
	 * @param fontSize 字体大小
	 * @param hasBorder 是否有边框
	 * @param isCenter 是否居中
	 * @param isBold 是否加粗
	 * @param bgColor 背景色
	 * @return
	 */
	public static HSSFCellStyle getCellStyle(HSSFWorkbook wb,int fontSize,boolean hasBorder,boolean isCenter,boolean isBold,Short bgColor) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		if (hasBorder) {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setWrapText(true);
		}
		if (isCenter) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		}
		if (bgColor != null) {
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(bgColor);
		}
		Font ztFont = wb.createFont();
		if (fontSize != 0) {
			ztFont.setFontHeightInPoints((short)fontSize);
		}
        if (isBold) {
        	ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
        cellStyle.setFont(ztFont);
        return cellStyle;
	}
	
	/**
	 * 单元格样式
	 * @param wb
	 * @param fontSize 字体大小
	 * @param hasBorder 是否有边框
	 * @param isCenter 是否居中
	 * @return
	 */
	public static HSSFCellStyle getCellStyle(HSSFWorkbook wb,int fontSize,boolean hasBorder,boolean isCenter) {
		return getCellStyle(wb,fontSize,hasBorder,isCenter,false,null);
	}
	
	/**
	 * 单元格样式
	 * @param wb
	 * @param fontSize 字体大小
	 * @return
	 */
	public static HSSFCellStyle getCellStyle(HSSFWorkbook wb,int fontSize) {
        return getCellStyle(wb,fontSize,true,true,false,null);
	}
	
	/**
	 * 单元格样式
	 * 带有边框，居中，字体大小不变
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle getCellStyle(HSSFWorkbook wb) {
        return getCellStyle(wb,0,true,true,false,null);
	}
	
	/**
	 * 设置字体和是否加粗
	 * @param wb
	 * @param style
	 * @param fontSize
	 * @param isBold
	 */
	public static void setFont(HSSFWorkbook wb,HSSFCellStyle style,int fontSize,boolean isBold) {
		Font font = wb.createFont();
		if (fontSize != 0) {
			font.setFontHeightInPoints((short)fontSize);
		}
        if (isBold) {
        	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		style.setFont(font);
	}
	
	/**
	 * 自适应列宽
	 */
	public static void autoColWidth(Sheet sheet2,int colsSize,int maxColWidth) {
		// 获取当前列的宽度，然后对比本列的长度，取最大值
		for (int columnNum = 0; columnNum < colsSize; columnNum++) {
			int columnWidth = sheet2.getColumnWidth(columnNum) / 256;
			for (int rowNum = 0; rowNum < sheet2.getLastRowNum()+1; rowNum++) {
				Row currentRow = sheet2.getRow(rowNum);
				if (currentRow != null) {
					if (currentRow.getCell(columnNum) != null) {
						Cell currentCell = currentRow.getCell(columnNum);
						int length = currentCell.toString().length();
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			if (columnWidth > maxColWidth) {
				columnWidth = maxColWidth;
			}
			sheet2.setColumnWidth(columnNum, columnWidth * 256);
		}
	}
	
	public static void autoColWidth(HSSFSheet sheet,int colsSize) {
		autoColWidth(sheet,colsSize,50);
	}
	
	/**
	 * 自适应行高（不适合合并单元格）
	 */
	public static void autoRowHeight(Sheet sheet,int beginRow,int endRow) {
		for (int rowNum = beginRow; rowNum < endRow; rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row != null) {
				double maxAutoHeight = row.getHeightInPoints();	//最大高度
				String value;
				double colWidth;
				double autoHeight;
				//获得最大长度的值
				for (int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++) {
					value = row.getCell(colNum).getStringCellValue();
					colWidth = (sheet.getColumnWidth(colNum)/256);
					autoHeight= getExcelCellAutoHeight(value, colWidth,row.getHeightInPoints());
					if (autoHeight > maxAutoHeight) {
						maxAutoHeight = autoHeight;
					}
				}
				if (maxAutoHeight > row.getHeightInPoints()) {
					row.setHeightInPoints((float) maxAutoHeight); 
				}
			}
		}
	}
	
	/**
	 * 自适应行高（不适合合并单元格）
	 */
//	public static void autoRowHeight(Sheet sheet,int beginRow,int endRow) {
//		for (int rowNum = beginRow; rowNum < endRow; rowNum++) {
//			Row row = sheet.getRow(rowNum);
//			if (row != null) {
//				double oldHeight = row.getHeightInPoints();	
//				int enterCnt = 0;  
//				for (int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++) {
//					String value = row.getCell(colNum).getStringCellValue();
//					int rwsTemp = value.split("\n").length;	//行数
//					if (rwsTemp > enterCnt) {  
//                        enterCnt = rwsTemp;  
//                    } 
//				}
//				if (enterCnt > 0) {
//					row.setHeightInPoints((float) (enterCnt*oldHeight)); 
//				}
//			}
//		}
//	}
	
	public static void autoRowHeight(HSSFSheet sheet,int beginRow) {
		autoRowHeight(sheet,beginRow,sheet.getLastRowNum()+1);
	}
	
	public static void autoRowHeight(HSSFSheet sheet) {
		autoRowHeight(sheet,0);
	}
	
	/**
	 * 获取自适应长度
	 * @param str 单元格内容
	 * @param fontCountInline 一列占的字符长/2（汉字数）
	 * @param defaultRowHeight 原来的行高
	 * @return
	 */
	private static double getExcelCellAutoHeight(String str, double fontCountInline,double defaultRowHeight) {
		double defaultCount = 0.00f;
        for (int i = 0; i < str.length(); i++) {
        	double ff = getregex(str.substring(i, i + 1));
            defaultCount = defaultCount + ff;
        }
        return Math.ceil((defaultCount / fontCountInline)) * defaultRowHeight;//计算
    }

    /**
     * 计算字符长度
     */
    private static double getregex(String charStr) {
        
        if(charStr.equals(" "))
        {
            return 1.00;
        }
        // 判断是否为字母或字符
        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
            return 1.00;
        }
        // 判断是否为中文
        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
            return 2.00;
        }
        //全角符号 及中文
        if (Pattern.compile("[\\u0391-\\uFFE5]").matcher(charStr).matches()) {
            return 2.00;
        }
        return 1.00;

    }
    
    
	/**
	 * 导入
	 * 通过列获得实体类
	 * @param rows
	 * @param t
	 * @return
	 */
	public static <E> List<E> getModelsByRows(List<Row> rows,Class<E> t){
		List<E> models = new ArrayList<E>();
		// 获取实体类的所有属性，返回Field数组 
		Field[] fields = t.getDeclaredFields();
		Row row;
		for (int i = 2; i < rows.size(); i++) {
			row = rows.get(i);
			//实例化对象
			E obj = null;
			try {
				obj=t.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			// 遍历所有属性 
			for (int j = 0; j < fields.length; j++) {
				String name = fields[j].getName();	// 获取属性的名字
				name = name.substring(0, 1).toUpperCase() + name.substring(1);  // 将属性的首字符大写
				String type = fields[j].getGenericType().toString(); // 获取属性的类型  
				if(type.equals("class java.lang.String") || type.equals("class java.lang.Integer") || type.equals("class java.lang.Short") || type.equals("class java.lang.Double") || type.equals("class java.lang.Boolean") || type.equals("class java.util.Date")){
					try {
						if (row.getCell(j) != null) {
							fields[j].setAccessible(true); 	//可访问私有变量
							fields[j].set(obj, getCellValue(row.getCell(j)));	//给属性赋值
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			models.add(obj);
		}
	     return models;
	}
	
	public static String getCellValue(Cell cell) {
		Object result = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				result = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_ERROR:
				result = cell.getErrorCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				break;
			default:
				break;
			}
		}
		return result.toString();
	}
	
	
	/**
	 * @param response
	 * @param fileName
	 * @param wb
	 * @throws IOException
	 */
	public static void write(HttpServletResponse response, String fileName,HSSFWorkbook wb) throws IOException {
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO8859-1"));
		response.setContentType("application/msexcel");
		wb.write(response.getOutputStream());
	}
	
}
