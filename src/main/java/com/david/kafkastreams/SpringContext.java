package com.david.kafkastreams;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class allows us to call a Spring Bean from a POJO
 * This class is used by the FootballTeamAggregation POJO so we can use the Spring repo to get the expected count
 * This was used as a workaround as the Kafka Streams framework instantiates a new instance of FootballTeamAggregation
 * upon each new key detected - so we need some way to get access to the DB through the Spring repo
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
