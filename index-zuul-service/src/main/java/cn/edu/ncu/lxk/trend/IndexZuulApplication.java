package cn.edu.ncu.lxk.trend;

import brave.sampler.Sampler;
import cn.hutool.core.util.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
//表示这个是一个网关
@EnableZuulProxy
//表示可以发现其它注册到eureka中的服务
@EnableDiscoveryClient
public class IndexZuulApplication
{
//    @Bean
//    public Sampler getSampler()
//    {
//        return Sampler.ALWAYS_SAMPLE;
//    }

    public static void main(String[] args)
    {
        int port = 8031;
        if (! NetUtil.isUsableLocalPort(port))
        {
            System.err.println("端口"+port+"被占用,无法启动网关服务");
            System.exit(1);
        }
        new SpringApplicationBuilder(IndexZuulApplication.class).properties("server.port="+port).run(args);
    }
}
