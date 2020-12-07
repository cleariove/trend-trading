package cn.edu.ncu.lxk.trend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//表示springboot项目
@SpringBootApplication
//表示eureka的客户端
@EnableEurekaClient
public class ThirdPartIndexDataApplication
{
    public static void main( String[] args )
    {
        int port = 1234;
        int eurekaServerPort = 8761;
        if (NetUtil.isUsableLocalPort(eurekaServerPort))
        {
            System.err.println("检查到"+eurekaServerPort+"端口未启动，eureka服务未启动！");
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
            System.err.println("端口被占用,无法启动第三方数据服务");
            System.exit(1);
        }
        new SpringApplicationBuilder(ThirdPartIndexDataApplication.class)
                .properties("server.port="+port)
                .run(args);
    }
}
