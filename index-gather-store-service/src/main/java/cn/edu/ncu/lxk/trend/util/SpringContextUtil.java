package cn.edu.ncu.lxk.trend.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

//用于给Spring Cache可以在一个方法中调用内部方法，就是防止@Cacheable失效
// 因为Spring实现缓存是通过aop，那么内部调用就不会走缓存
@Component
public class SpringContextUtil implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz)
    {
        return applicationContext.getBean(clazz);
    }
}
