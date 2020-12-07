package cn.edu.ncu.lxk.trend.pojo;

public class IndexData
{
    private String date;
    private float closePoint;

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
