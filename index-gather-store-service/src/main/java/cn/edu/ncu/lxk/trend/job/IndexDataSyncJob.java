package cn.edu.ncu.lxk.trend.job;

import cn.edu.ncu.lxk.trend.pojo.Index;
import cn.edu.ncu.lxk.trend.service.IndexDataService;
import cn.edu.ncu.lxk.trend.service.IndexService;
import cn.hutool.core.date.DateUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class IndexDataSyncJob extends QuartzJobBean
{
    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDataService indexDataService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        System.out.println("启动定时"+ DateUtil.now());
        List<Index> indices = indexService.fresh();
        for (Index i:indices)
        {
            indexDataService.fresh(i.getCode());
        }
        System.out.println("工作完成"+ DateUtil.now());
    }
}
