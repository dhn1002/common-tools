package com.dong.common.tools.datatable;

import java.util.List;

/**
 * @author dong
 * @date 2018/1/26
 */
public class DataTable<T>
{
    private List<T> aaData;
    private int iTotalDisplayRecords;
    private int iTotalRecords;
    private int sEcho;

    public DataTable(List<T> list, int totalCount, int records)
    {
        this.aaData = list;
        this.iTotalRecords = totalCount;
        this.iTotalDisplayRecords = records;
    }

    public int getsEcho()
    {
        return this.sEcho;
    }

    public void setsEcho(int sEcho)
    {
        this.sEcho = sEcho;
    }

    public List<T> getAaData()
    {
        return this.aaData;
    }

    public void setAaData(List<T> aaData)
    {
        this.aaData = aaData;
    }

    public int getiTotalDisplayRecords()
    {
        return this.iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords)
    {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getiTotalRecords()
    {
        return this.iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords)
    {
        this.iTotalRecords = iTotalRecords;
    }
}
