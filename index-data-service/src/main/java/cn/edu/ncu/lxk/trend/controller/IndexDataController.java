package cn.edu.ncu.lxk.trend.controller;

import cn.edu.ncu.lxk.trend.config.IPConfiguration;
import cn.edu.ncu.lxk.trend.pojo.IndexData;
import cn.edu.ncu.lxk.trend.service.IndexDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexDataController
{
    @Autowired
    private IndexDataService indexDataService;

    @Autowired
    private IPConfiguration ipConfiguration;

    @GetMapping("/data/{code}")
    public List<IndexData> get(@PathVariable("code") String code)
    {
        System.out.println("当前端口号为:"+ipConfiguration.getServerPort());
        return indexDataService.get(code);
    }
}
