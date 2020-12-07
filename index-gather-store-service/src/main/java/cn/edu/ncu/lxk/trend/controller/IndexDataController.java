package cn.edu.ncu.lxk.trend.controller;

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

    @GetMapping("/freshIndexData/{code}")
    public String fresh(@PathVariable("code") String code)
    {
        indexDataService.fresh(code);
        return "更新成功";
    }

    @GetMapping("/getIndexData/{code}")
    public List<IndexData> get(@PathVariable("code") String code)
    {
        return indexDataService.get(code);
    }

    @GetMapping("/removeIndexData/{code}")
    public String remove(@PathVariable("code")String code)
    {
        indexDataService.remove(code);
        return "删除成功";
    }
}
