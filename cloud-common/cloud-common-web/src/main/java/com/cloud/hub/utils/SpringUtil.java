package com.cloud.hub.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author: jaxMine
 * @Date: 2019/12/31 16:54
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = arg0;
        }
    }

    // 获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    // 通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    // 通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        T bean = null;
        try {
            bean = getApplicationContext().getBean(clazz);
        } catch (Exception e) {
            System.out.println("获取bean失败" + clazz.getName());
        }
        return bean;
    }

    // 通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        T bean = null;
        try {
            bean = getApplicationContext().getBean(name, clazz);
        } catch (Exception e) {
            System.out.println("获取bean失败" + clazz.getName());
        }
        return bean;
    }

    /**
     * 注入bean
     * @param beanName
     * @param clazz
     * @param original
     *            bean属性注入
     */
    public static synchronized void setBean(String beanName, Class<?> clazz, Map<String, Object> original) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getApplicationContext()
                .getAutowireCapableBeanFactory();
        if (beanFactory.containsBean(beanName)) {
            return;
        }
        // BeanDefinition beanDefinition = new RootBeanDefinition(clazz);
        GenericBeanDefinition definition = new GenericBeanDefinition();
        // 类class
        definition.setBeanClass(clazz);
        // 属性赋值
        definition.setPropertyValues(new MutablePropertyValues(original));
        // 注册到spring上下文
        beanFactory.registerBeanDefinition(beanName, definition);
    }

    /**
     * 删除bean by beanId
     * @param beanName
     */
    public static void removeBean(String beanName) {
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) getApplicationContext()
                .getAutowireCapableBeanFactory();
        if (acf.containsBean(beanName)) {
            acf.removeBeanDefinition(beanName);
        }
    }

    /**
     * 判断bean是否存在
     * @param beanName
     * @return
     */
    public static boolean containsBean(String beanName) {
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) getApplicationContext()
                .getAutowireCapableBeanFactory();
        return acf.containsBean(beanName);
    }

    /**
     * 静态资源判断
     * @param request
     * @return
     */
    public static boolean isStaticResource(HttpServletRequest request) {
        boolean match = false;
        List<HandlerMapping> handlerMappings = getBean(DispatcherServlet.class).getHandlerMappings();
        for (HandlerMapping handler : handlerMappings) {
            if (handler instanceof SimpleUrlHandlerMapping) {
                try {
                    HandlerExecutionChain handlerExecutionChain= handler.getHandler(request);
                    if (handlerExecutionChain!=null && handlerExecutionChain.getHandler() instanceof ResourceHttpRequestHandler) {
                        request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, request.getRequestURI());
                        ResourceHttpRequestHandler resourceHttpRequestHandler = (ResourceHttpRequestHandler) handlerExecutionChain.getHandler();
                        Method method = ReflectionUtils.findMethod(ResourceHttpRequestHandler.class, "getResource",HttpServletRequest.class);
                        method.setAccessible(true);
                        Object resource = ReflectionUtils.invokeMethod(method, resourceHttpRequestHandler, request);
                        match = resource != null;
                        break;
                    }
                } catch (Exception e) {
                    match = false;
                }
            }
        }
        return match;
    }

}
