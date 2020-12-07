package cn.edu.ncu.lxk.trend.config;

import cn.edu.ncu.lxk.trend.job.IndexDataSyncJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration
{
    private static final int interval = 4 * 60;//每四个小时刷新一次

    @Bean
    public JobDetail weatherDataSyncJobDetail()
    {
        return JobBuilder.newJob(IndexDataSyncJob.class)
                .withIdentity("indexDataSyncJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weatherDataSyncTrigger()
    {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(interval).repeatForever();
        return TriggerBuilder.newTrigger().forJob(weatherDataSyncJobDetail())
                .withIdentity("indexDataSyncTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
