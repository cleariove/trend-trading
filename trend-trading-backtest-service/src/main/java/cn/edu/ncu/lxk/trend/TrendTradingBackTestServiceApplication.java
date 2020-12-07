package cn.edu.ncu.lxk.trend;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class TrendTradingBackTestServiceApplication
{
//    @Bean
//    public Sampler getSampler()
//    {
//        return Sampler.ALWAYS_SAMPLE;
//    }

    public static void main(String[] args)
    {
        int port = 0;
        int defaultPort = 8051;
        int eurekaServerPort = 8761;
        if (NetUtil.isUsableLocalPort(eurekaServerPort))
        {
            System.err.println("检查到"+eurekaServerPort+"端口未启动，eureka服务未启动！");
            System.exit(1);
        }
        if (args != null && args.length != 0)
        {
            for (String s:args)
            {
                if (s.startsWith("port="))
                {
                    String s1 = StrUtil.subAfter(s,"port=",true);
                    if (NumberUtil.isNumber(s1))
                        port = Convert.toInt(s1);
                }
            }
        }
        if (port == 0)
        {
            Future<Integer> future = ThreadUtil.execAsync(()->
            {
                int p = 0;
                System.out.println("请再5秒钟内输入端口号，否则默认使用" + defaultPort);
                Scanner scanner = new Scanner(System.in);
                while (true)
                {
                    String temp = scanner.nextLine();
                    if (NumberUtil.isNumber(temp))
                    {
                        p = Convert.toInt(temp);
                        scanner.close();
                        break;
                    }
                    else
                        System.err.println("请输入整数");
                }
                return p;
            });
            try
            {
                port = future.get(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException | TimeoutException | ExecutionException e)
            {
                System.out.println("系统将使用默认端口");
                port = defaultPort;
            }
        }
        if (! NetUtil.isUsableLocalPort(port))
        {
            System.err.println("端口" + port + "被占用，无法启动模拟回测微服务");
            System.exit(1);
        }
        new SpringApplicationBuilder(TrendTradingBackTestServiceApplication.class).
                properties("server.port=" + port).
                run(args);
    }
}
