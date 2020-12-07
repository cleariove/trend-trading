package cn.edu.ncu.lxk.trend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
//标志断路由
@EnableHystrix
//表示启用缓存
@EnableCaching
public class IndexGatherStoreApplication
{
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }

    public static void main(String[] args)
    {
        int port = 8001;
        int eurekaServerPort = 8761;
        int redisPort = 6379;
        if (NetUtil.isUsableLocalPort(eurekaServerPort))
        {
            System.err.println("检查到"+eurekaServerPort+"端口未启动，eureka服务未启动！");
            System.exit(1);
        }
        if (NetUtil.isUsableLocalPort(redisPort))
        {
            System.err.println("检查到"+redisPort+"端口未启动，redis服务未启动！");
            System.exit(1);
        }
        //这里可以方便部署到服务器后，启动服务前通过添加参数修改运行端口号
        if (args != null && args.length != 0)
        {
            for (String s:args)
            {
                if (s.startsWith("port="))
                {
                    //使用hutool工具包
                    String s1 = StrUtil.subAfter(s,"port=",true);
                    if (NumberUtil.isNumber(s1))
                        port = Convert.toInt(s1);
                }
            }
        }

        if (!NetUtil.isUsableLocalPort(port))
        {
            System.err.println("端口被占用,无法启动数据采集服务");
            System.exit(1);
        }
        new SpringApplicationBuilder(IndexGatherStoreApplication.class)
                .properties("server.port="+port)
                .run(args);
    }
}
