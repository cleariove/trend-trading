package cn.edu.ncu.lxk.trend.client;

import cn.edu.ncu.lxk.trend.pojo.IndexData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// value的值代表eureka中服务的名称
// fallback表示访问失败时，调用给定类的回退方法，可以用来抛出异常或给出默认返回数据
// 这里给定的类必须
//      要实现FeignClient注解标记的接口（在这里也就是IndexDataClient）
//      要交给Spring管理(即@Component)
// 底层依赖hystrix
@FeignClient(value = "INDEX-DATA-SERVICE",fallback = IndexDataClientFeignHystrix.class)
@Primary
public interface IndexDataClient
{
    @GetMapping("/data/{code}")
    List<IndexData> getIndexData(@PathVariable("code") String code);

}
