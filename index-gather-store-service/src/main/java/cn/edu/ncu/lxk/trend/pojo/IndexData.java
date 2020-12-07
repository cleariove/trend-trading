package cn.edu.ncu.lxk.trend.pojo;

import java.io.Serializable;

//具体的某一家公司的具体某天数据
public class IndexData implements Serializable
{
    private String date;//日期

    private float closePoint;//收盘点

    public IndexData()
    {

    }

    public IndexData(String date, float closePoint)
    {
        this.date = date;
        this.closePoint = closePoint;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public float getClosePoint()
    {
        return closePoint;
    }

    public void setClosePoint(float closePoint)
    {
        this.closePoint = closePoint;
    }
}
