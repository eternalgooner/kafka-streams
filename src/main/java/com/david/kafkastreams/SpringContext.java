package com.david.kafkastreams;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class enables calling a Spring Bean from a POJO through the Spring Context
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setContext(applicationContext);
    }

    private static synchronized void setContext(ApplicationContext applicationContext) {
        SpringContext.context = applicationContext;
    }
}
