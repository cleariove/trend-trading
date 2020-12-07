package cn.edu.ncu.lxk.trend.service;

import cn.edu.ncu.lxk.trend.pojo.IndexData;
import cn.hutool.core.collection.CollUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "index_data")
public class IndexDataService
{
    @Cacheable(key = "'indexData-code-'+#p0")
    public List<IndexData> get(String code)
    {
        System.out.println();
        return CollUtil.toList();
    }
}
