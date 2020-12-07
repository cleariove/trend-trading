package cn.edu.ncu.lxk.trend;


import cn.hutool.core.util.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//springboot的启动类
@SpringBootApplication
//表示是个eureka注册服务中心
@EnableEurekaServer
public class EurekaServerApplication
{

    public static void main(String[] args)
    {
        //这是默认的端口，后面的子项目都会访问这个端口
        int port = 8761;
        //使用hutool工具包判断端口是否被占用
        if (!NetUtil.isUsableLocalPort(port))
        {
            System.err.println("端口被占用，无法启动eureka服务");
            System.exit(1);
        }
        //启动这个模块
        new SpringApplicationBuilder(EurekaServerApplication.class)
                .properties("server.port="+port).run(args);
    }
}
