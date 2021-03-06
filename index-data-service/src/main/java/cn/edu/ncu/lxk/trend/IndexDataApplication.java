package cn.edu.ncu.lxk.trend;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class IndexDataApplication
{
//    @Bean
//    public Sampler getSampler()
//    {
//        return Sampler.ALWAYS_SAMPLE;
//    }

    public static void main(String[] args)
    {
        int port = 0;
        int defaultPort = 8021;
        int redisPort = 6379;
        int eurekaServerPort = 8761;
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

        if (args != null && args.length != 0)
        {
            for (String s : args)
                if (s.startsWith("port="))
                {
                    String tempPort = StrUtil.subAfter(s, "port=", true);
                    if (NumberUtil.isNumber(tempPort))
                        port = Convert.toInt(tempPort);
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
                    String s = scanner.nextLine();
                    if (NumberUtil.isNumber(s))
                    {
                        p = Convert.toInt(s);
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
            catch (InterruptedException | ExecutionException | TimeoutException e)
            {
                System.out.println("系统将使用默认端口");
                port = defaultPort;
            }
        }
        if (!NetUtil.isUsableLocalPort(port))
        {
            System.err.println("端口" + port + "被占用，无法启动指数数据服务");
            System.exit(1);;
        }
        new SpringApplicationBuilder(IndexDataApplication.class).properties("server.port="+port).run(args);
    }
}
