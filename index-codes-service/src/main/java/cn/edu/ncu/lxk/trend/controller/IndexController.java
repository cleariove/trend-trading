package cn.edu.ncu.lxk.trend.controller;

import cn.edu.ncu.lxk.trend.config.IPConfiguration;
import cn.edu.ncu.lxk.trend.pojo.Index;
import cn.edu.ncu.lxk.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController
{
    @Autowired
    private IndexService indexService;

    @Autowired
    private IPConfiguration ipConfiguration;

    //允许跨域，跨域就是浏览器会阻止其它网站的脚本的执行，是浏览器对JavaScript施加的安全限制
    //就比如baidu.com不能访问google.com的
    //加上这个注解就可以实现访问不是同一个域的文件
    @CrossOrigin
    @GetMapping("/codes")
    public List<Index> get()
    {
        System.out.println("当前端口号为:"+ipConfiguration.getServerPort());
        return indexService.get();
    }
}
