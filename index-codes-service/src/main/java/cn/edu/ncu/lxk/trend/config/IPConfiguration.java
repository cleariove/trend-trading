package cn.edu.ncu.lxk.trend.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


//这是spring的事件监听机制
// 使用了观察者模式
// 通过实现ApplicationListener接口完成事件的监听
// 通过实现ApplicationEvent接口完成事件spring内置了多个事件
// 比如ContextRefreshedEvent--->所有bean初始化完成的事件
// WebServerInitializedEvent--->springboot项目启动
//--------------------------------------------------------------------------------
//这个微服务会做成集群，集群就是
//原来数据微服务只有这一个springboot
// 现在做同样数据微服务的
// 有两个 springboot
// 他们提供的功能一模一样，只是端口不一样
// 这样就形成了集群。
//简单来说就是多台服务器实现同一个服务
@Component
public class IPConfiguration implements ApplicationListener<WebServerInitializedEvent>
{
    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent)
    {
        this.serverPort = webServerInitializedEvent.getWebServer().getPort();
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }
}
