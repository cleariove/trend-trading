package cn.edu.ncu.lxk.trend.pojo;

import java.io.Serializable;

//对应codes.json文件
//表示公司
//对象需要序列化才可以存储至Redis中
public class Index implements Serializable
{
    private String code;//公司的编号
    private String name;//公司的名称

    public Index()
    {
    }

    public Index(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
