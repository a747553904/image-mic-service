package com.ddjf.image.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private static Properties props = new Properties();

    static {
        try {
            InputStream in = SpringContextUtil.class.getResourceAsStream("/var.properties");
            props.load(in);
            in.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    @Bean(name = {"config"})
    public Properties globalProperties() {
        return props;
    }
    
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }  
    
    public static Properties getProperties() {
        if(props == null)
            props = (Properties) applicationContext.getBean("config");
        return props;
    }
    
    public static String getProperty(String name) {
        return getProperties().getProperty(name);
    }
    
    public static String getProperty(String name, String defaultValue) {
        return getProperties().getProperty(name, defaultValue);
    }
}
