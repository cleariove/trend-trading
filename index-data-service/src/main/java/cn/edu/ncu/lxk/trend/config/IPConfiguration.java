package cn.edu.ncu.lxk.trend.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//同index-codes-service
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
