package com.bosssoft.install.activiti.cache;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer;
    
    public <T> T getProxy(Class<T> clazz) {
         enhancer.setSuperclass(clazz);
         enhancer.setCallback(this);
         return (T)enhancer.create();
    }
    
    public <T> T getProxy(T t) {
         enhancer.setSuperclass(t.getClass());
         enhancer.setCallback(this);
         return (T)enhancer.create();
    }
    
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        long begin = System.currentTimeMillis();
        System.out.println(obj.getClass().getName() + "." + method.getName());
        Object result = proxy.invokeSuper(obj, args);
        long end = System.currentTimeMillis();
        System.out.println("执行了" + (end - begin) + "毫秒");
        return result;
    }
    
    public CglibProxy() {
        super();
        this.enhancer = new Enhancer();
    }
}
