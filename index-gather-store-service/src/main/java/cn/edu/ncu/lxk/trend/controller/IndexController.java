package cn.edu.ncu.lxk.trend.controller;


import cn.edu.ncu.lxk.trend.pojo.Index;
import cn.edu.ncu.lxk.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController
{
    @Autowired
    private IndexService indexService;

//    @GetMapping("/getCodes")
//    public List<Index> get()
//    {
//        return indexService.fetchIndicesFromThirdPart();
//    }

    @GetMapping("/freshCodes")
    public List<Index> fresh()
    {
        return indexService.fresh();
    }

    @GetMapping("/getCodes")
    public List<Index> get()
    {
        return indexService.get();
    }

    @GetMapping("/removeCodes")
    public String remove()
    {
        indexService.remove();
        return "删除成功！";
    }
}
