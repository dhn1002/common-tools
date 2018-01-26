package com.dong.common.tools.datatable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTableParameter
{
    private int sEcho;
    private int iDisplayStart;
    private int iDisplayLength;
    private int iColumns;
    private List<String> mDataProps;
    private List<Boolean> bSortables;
    private int iSortingCols;
    private List<Integer> iSortCols;
    private List<String> iSortColsName;
    private List<String> sSortDirs;

    public int getsEcho()
    {
        return this.sEcho;
    }

    public void setsEcho(int sEcho)
    {
        this.sEcho = sEcho;
    }

    public int getiDisplayStart()
    {
        return this.iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart)
    {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength()
    {
        return this.iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength)
    {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiColumns()
    {
        return this.iColumns;
    }

    public void setiColumns(int iColumns)
    {
        this.iColumns = iColumns;
    }

    public List<String> getmDataProps()
    {
        return this.mDataProps;
    }

    public void setmDataProps(List<String> mDataProps)
    {
        this.mDataProps = mDataProps;
    }

    public List<Boolean> getbSortables()
    {
        return this.bSortables;
    }

    public void setbSortables(List<Boolean> bSortables)
    {
        this.bSortables = bSortables;
    }

    public List<Integer> getiSortCols()
    {
        return this.iSortCols;
    }

    public void setiSortCols(List<Integer> iSortCols)
    {
        this.iSortCols = iSortCols;
    }

    public List<String> getsSortDirs()
    {
        return this.sSortDirs;
    }

    public void setsSortDirs(List<String> sSortDirs)
    {
        this.sSortDirs = sSortDirs;
    }

    public int getiSortingCols()
    {
        return this.iSortingCols;
    }

    public void setiSortingCols(int iSortingCols)
    {
        this.iSortingCols = iSortingCols;
    }

    public DataTableParameter(int sEcho, int iDisplayStart, int iDisplayLength, int iColumns, List<String> mDataProps, List<Boolean> bSortables, int iSortingCols, List<Integer> iSortCols, List<String> sSortDirs, List<String> iSortColsName)
    {
        this.sEcho = sEcho;
        this.iDisplayStart = iDisplayStart;
        this.iDisplayLength = iDisplayLength;
        this.iColumns = iColumns;
        this.mDataProps = mDataProps;
        this.bSortables = bSortables;
        this.iSortingCols = iSortingCols;
        this.iSortCols = iSortCols;
        this.sSortDirs = sSortDirs;
        this.iSortColsName = iSortColsName;
    }

    private static Map<String, Object> covertJsonStringToHashMap(String jsonParam)
    {
        JSONArray jsonArray = JSONArray.parseArray(jsonParam);
        Map<String, Object> map = new HashMap();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            map.put(jsonObj.getString("name"), jsonObj.get("value"));
        }
        return map;
    }

    public static DataTableParameter getDataTableParameterByJsonParam(String jsonParam)
    {
        Map<String, Object> map = covertJsonStringToHashMap(jsonParam);
        int sEcho = ((Integer)map.get("sEcho")).intValue();
        int iDisplayStart = ((Integer)map.get("iDisplayStart")).intValue();
        int iDisplayLength = ((Integer)map.get("iDisplayLength")).intValue();
        int iColumns = ((Integer)map.get("iColumns")).intValue();
        int iSortingCols = ((Integer)map.get("iSortingCols")).intValue();

        List<String> mDataProps = new ArrayList();
        List<Boolean> bSortables = new ArrayList();
        for (int i = 0; i < iColumns; i++)
        {
            String dataProp = (String)map.get(new StringBuilder("mDataProp_").append(i).toString());
            Boolean sortable = (Boolean)map.get("bSortable_" + i);
            mDataProps.add(dataProp);
            bSortables.add(sortable);
        }
        List<Integer> iSortCols = new ArrayList();
        List<String> sSortDirs = new ArrayList();
        List<String> iSortColsName = new ArrayList();
        for (int i = 0; i < iSortingCols; i++)
        {
            Integer sortCol = (Integer)map.get("iSortCol_" + i);
            String sortColName = (String)mDataProps.get(sortCol.intValue());
            String sortDir = (String)map.get("sSortDir_" + i);
            iSortCols.add(sortCol);
            sSortDirs.add(sortDir);
            iSortColsName.add(sortColName);
        }
        return new DataTableParameter(sEcho, iDisplayStart, iDisplayLength, iColumns, mDataProps, bSortables, iSortingCols, iSortCols, sSortDirs, iSortColsName);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("DataTableParameter [sEcho=");
        builder.append(this.sEcho);
        builder.append(", iDisplayStart=");
        builder.append(this.iDisplayStart);
        builder.append(", iDisplayLength=");
        builder.append(this.iDisplayLength);
        builder.append(", iColumns=");
        builder.append(this.iColumns);
        builder.append(", mDataProps=");
        builder.append(this.mDataProps);
        builder.append(", bSortables=");
        builder.append(this.bSortables);
        builder.append(", iSortingCols=");
        builder.append(this.iSortingCols);
        builder.append(", iSortCols=");
        builder.append(this.iSortCols);
        builder.append(", iSortColsName=");
        builder.append(this.iSortColsName);
        builder.append(", sSortDirs=");
        builder.append(this.sSortDirs);
        builder.append("]");
        return builder.toString();
    }

    public List<String> getiSortColsName()
    {
        return this.iSortColsName;
    }

    public void setiSortColsName(List<String> iSortColsName)
    {
        this.iSortColsName = iSortColsName;
    }
}

