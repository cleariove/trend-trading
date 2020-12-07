package cn.edu.ncu.lxk.trend;


import cn.hutool.core.util.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer
public class IndexZipkinApplication
{
    public static void main(String[] args)
    {
        int port = 9411;
        if (!NetUtil.isUsableLocalPort(port))
        {
            System.err.println("端口"+port+"被占用,无法启动trace服务");
            System.exit(-1);
        }
        new SpringApplicationBuilder(IndexZipkinApplication.class).properties("port="+port).run(args);
    }
}
