package cn.edu.ncu.lxk.trend.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class FreshUtil
{
    public static void main(String[] args)
    {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json; charset=UTF-8");
        System.out.println("配置拉取中");

        // 请求这个接口则表示view:8041这个服务通知index-config-server去git拉取最新数据
        // 这份数据返回给view：8041后，为了同步view集群中的其它机器会将消息发给mq
        // mq将数据推给其它集群
        HttpRequest request = HttpUtil.createPost("http://localhost:8041/actuator/bus-refresh");
        request.addHeaders(header);
        HttpResponse response = request.execute();

        String result = response.body();
        System.out.println(result);
    }
}
