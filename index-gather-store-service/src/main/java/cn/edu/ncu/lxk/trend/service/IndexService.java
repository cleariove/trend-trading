package cn.edu.ncu.lxk.trend.service;

import cn.edu.ncu.lxk.trend.pojo.Index;
import cn.edu.ncu.lxk.trend.util.SpringContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
//redis中的缓存一般加在service层
//表示缓存的名称是indexes，表示这个类所有的查询都先从“indexes”中查询
//如果不存在则会从数据库中查询然后保存至缓存“indexes”中
@CacheConfig(cacheNames = "indexes")
public class IndexService
{
    //用于保存所有公司的编号和名称
    private List<Index> indices;

    //这个是Spring提供的工具类，可以用于调用restful服务
    // 可以实现提供网站的URL，获取返回的URL
    // 这里用于从第三方服务获取数据
    @Autowired
    private RestTemplate restTemplate;

    /*
    //这个注解表示第三方服务未启动时调用的方法
    @HystrixCommand(fallbackMethod = "thirdPartNotConnected")
    //这个注解表示从“all_codes”中查询
    //但是由于这个类上面也配置了一个名称
    //那就相当于保存在“indexes”缓存中的“all_codes”
    //all_codes就是Redis中的键
    //有点类似数据库中，库和表的概念
    //但是这个注解还可以指定将数据进行缓存的条件等等
    @Cacheable(key = "'all_codes'")
    //返回的第三方数据
    public List<Index> fetchIndicesFromThirdPart()
    {
        //读取json数据保存在Map中
        List<Map> maps = restTemplate.getForObject
                ("http://localhost:8090/indexes/codes.json",List.class);
        //这里访问的url是third-part-index-data-project模块的地址
        // 直接读取该模块resources文件夹下面的数据，
        // 不需要加上static，这是springboot帮忙简化过的方式
        return changeToIndex(maps);
    }
     */

    //更新缓存中的数据，如果此时第三方服务断开了，那么就只会有一个空值了
    // 不会把错误信息保存进缓存
    //首先会删除所有数据，再重新进行缓存
    //将刷新，删除，获取，存储分开可以保证各司其职
    // 就是获取部分只负责从缓存读取，不可能进行存储
    //存储部分只负责读入数据
    //刷新的逻辑是，将数据先从第三方服务中读取至这个类中的List里面
    // 然后再将缓存清空
    // 最后在把list成员进行缓存
    //前端不可以直接调用store方法，store方法只可能在刷新时使用
    @HystrixCommand(fallbackMethod = "thirdPartNotConnected")
    public List<Index> fresh()
    {
        indices = fetchIndexFromThirdPart();
        IndexService indexService = SpringContextUtil.getBean(this.getClass());
        indexService.remove();
        return indexService.store();
    }

    //清空所有缓存，这个注解实现的缓存删除功能
    @CacheEvict(allEntries = true)
    public void remove()
    {

    }

    //读取缓存,只负责读取缓存
    // 缓存中没有内容，那么会执行方法体，方法体返回的是一个空值
    // 有内容则返回缓存中all_codes的结果
    // 这样子只要缓存中有数据，第三方即使断了客服端也不会立马丢失数据
    @Cacheable(key = "'all_codes'")
    public List<Index> get()
    {
        return CollUtil.toList();
    }

    //将indices列表中的数据保存至缓存all_codes
    // 和@Cacheable不同的是，它每次都会触发真实方法的调用
    // 也就相当于一定会执行方法体
    @CachePut(key = "'all_codes'")
    public List<Index> store()
    {
        return indices;
    }

    public List<Index> fetchIndexFromThirdPart()
    {
        List<Map> temp =
                    restTemplate.getForObject("http://localhost:1234/indexes/codes.json",List.class);
        return changeToIndex(temp);
    }

    //将Map中的数据转换为Index
    private List<Index> changeToIndex(List<Map> list)
    {
        List<Index> list1 = new ArrayList<>();
        for (Map m:list)
        {
            String code = m.get("code").toString();
            String name = (String) m.get("name");
            list1.add(new Index(code,name));
        }
        return list1;
    }

    //第三方数据服务如果停止则显示一个错误代码
    public List<Index> thirdPartNotConnected()
    {
        System.out.println("thirdPartNotConnected()");
        //hutool工具包
        return CollectionUtil.toList(new Index("000000","无效指数代码"));
    }
}
