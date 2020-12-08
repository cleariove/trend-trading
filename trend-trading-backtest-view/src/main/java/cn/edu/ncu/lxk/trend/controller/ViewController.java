package cn.edu.ncu.lxk.trend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController
{
    @Value("${version}")
    private String version;

    @GetMapping("/")
    public String view(Model m)
    {
        m.addAttribute("version",version);
        return "view";
    }
}
