package cn.edu.ncu.lxk.trend.client;

import cn.edu.ncu.lxk.trend.pojo.IndexData;
import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexDataClientFeignHystrix implements IndexDataClient
{
    @Override
    public List<IndexData> getIndexData(String code)
    {
        return CollUtil.toList(new IndexData("0000-00-00",0));
    }
}
