package cn.edu.ncu.lxk.trend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RefreshScope
public class ViewController
{
    @Value("${version:出错}")
    private String version;

    @GetMapping("/")
    public String view(Model m)
    {
        m.addAttribute("version",version);
        return "view";
    }
}
