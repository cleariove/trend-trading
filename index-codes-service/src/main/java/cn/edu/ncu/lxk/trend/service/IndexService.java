package cn.edu.ncu.lxk.trend.service;

import cn.edu.ncu.lxk.trend.pojo.Index;
import cn.hutool.core.collection.CollUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "indexes")
public class IndexService
{
    @Cacheable(key = "'all_codes'")
    public List<Index> get()
    {
        return CollUtil.toList(new Index("000000","无效指数代码"));
    }
}
