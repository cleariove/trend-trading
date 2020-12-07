package cn.edu.ncu.lxk.trend.service;


import cn.edu.ncu.lxk.trend.pojo.Index;
import cn.edu.ncu.lxk.trend.pojo.IndexData;
import cn.edu.ncu.lxk.trend.util.SpringContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "index_data")
public class IndexDataService
{
    //key代表公司的编号，list代表这个公司所有的具体数据
    private Map<String,List<IndexData>> indexData = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    //参数code代表了公司的编号
    @HystrixCommand(fallbackMethod = "thirdPartNotFound")
    public List<IndexData> fresh(String code)
    {
        indexData.put(code,fetchIndexDataFromThirdPart(code));
        IndexDataService indexDataService = SpringContextUtil.getBean(IndexDataService.class);
        indexDataService.remove(code);
        return indexDataService.store(code);
    }

    //参数code代表了公司的编号
    //#p0代表该方法第一个参数
    @CacheEvict(key = "'indexData-code-'+#p0")
    public void remove(String code)
    {
    }

    //参数code代表了公司的编号
    //#p0代表该方法第一个参数
    @Cacheable(key = "'indexData-code-'+#p0")
    public List<IndexData> get(String code)
    {
        return CollUtil.toList();
    }

    //参数code代表了公司的编号
    //#p0代表该方法第一个参数
    @CachePut(key = "'indexData-code-'+#code")
    public List<IndexData> store(String code)
    {
        return indexData.get(code);
    }


    public List<IndexData> fetchIndexDataFromThirdPart(String code)
    {
        List<Map> maps =
                restTemplate.getForObject("http://localhost:1234/indexes/" + code + ".json", List.class);
        return changeToIndexData(maps);
    }

    private List<IndexData> changeToIndexData(List<Map> list)
    {
        List<IndexData> indexData = new ArrayList<>();
        for (Map m:list)
        {
            String date = m.get("date").toString();
            float closePoint = Convert.toFloat(m.get("closePoint"));
            indexData.add(new IndexData(date,closePoint));
        }
        System.out.println("1."+indexData.size());
        return indexData;
    }

    public List<IndexData> thirdPartNotFound(String code)
    {
        return CollectionUtil.toList(new IndexData("n/a",0));
    }
}
