package cn.edu.ncu.lxk.trend;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableEurekaClient
public class TrendTradingBackTestViewApplication
{
//    @Bean
//    public Sampler getSampler()
//    {
//        return Sampler.ALWAYS_SAMPLE;
//    }

    public static void main(String[] args)
    {
        int port = 0;
        int defaultPort = 8041;
        int eurekaServerPort = 8761;
        if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.println("检查到"+eurekaServerPort+"端口未启动，eureka服务未启动！");
            System.exit(1);
        }
        if (args != null && args.length != 0)
        {
            for (String s : args)
            {
                if (s.startsWith("port="))
                {
                    String portStr = StrUtil.subAfter(s,"port=",true);
                    if (NumberUtil.isNumber(portStr))
                        port = Convert.toInt(portStr);
                }
            }
        }

        if (port == 0)
        {
            Future<Integer> task = ThreadUtil.execAsync(()->
            {
                int p = 0;
                System.out.printf("请于5秒钟内输入端口号, 推荐 %d ,超过5秒将默认使用 %d ",defaultPort,defaultPort);
                Scanner scanner = new Scanner(System.in);
                while (true)
                {
                    String s = scanner.nextLine();
                    if (!NumberUtil.isNumber(s))
                    {
                        System.err.println("请输入整数");
                    }
                    else
                    {
                        p = Convert.toInt(s);
                        scanner.close();
                        break;
                    }
                }
                return p;
            });
            try
            {
                task.get(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException | ExecutionException | TimeoutException e)
            {
                port = defaultPort;
            }
        }

        if (!NetUtil.isUsableLocalPort(port))
        {
            System.err.printf("端口%d被占用了，无法启动%n", port );
            System.exit(1);
        }
        new SpringApplicationBuilder(TrendTradingBackTestViewApplication.class).
                properties("server.port=" + port).
                run(args);
    }
}
