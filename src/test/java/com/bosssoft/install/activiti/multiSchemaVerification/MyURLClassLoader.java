package com.bosssoft.install.activiti.multiSchemaVerification;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class MyURLClassLoader extends URLClassLoader{
	private JarURLConnection cachedJarFile = null;

    public MyURLClassLoader() {
        super(new URL[] {}, findParentClassLoader());
    }

    /**
     * 将指定的文件url添加到类加载器的classpath中去，并缓存jar connection，方便以后卸载jar
     * 一个可想类加载器的classpath中添加的文件url
     * @param
     */
    public void addURLFile(URL file) {
        addURL(file);
    }

    /**
     * 定位基于当前上下文的父类加载器
     * @return 返回可用的父类加载器.
     */
    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = MyURLClassLoader.class.getClassLoader();
        if (parent == null) {
            parent = MyURLClassLoader.class.getClassLoader();
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        return parent;
    }

}
