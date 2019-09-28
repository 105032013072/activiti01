package com.bosssoft.install.activiti.multiSchemaVerification;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class MyClassLoader {
	 
	    private final static ConcurrentHashMap<String,MyURLClassLoader> LOADER_CACHE = new ConcurrentHashMap<>();
	    
//	    private MyURLClassLoader urlClassLoader;


	    public void loadJar(String jarPath) throws MalformedURLException {
	        MyURLClassLoader urlClassLoader = LOADER_CACHE.get(jarPath);
	        if(urlClassLoader!=null){
	            return;
	        }
	        urlClassLoader = new MyURLClassLoader();

	        URL jarUrl = new URL("jar:file:/"+jarPath+"!/");
	        urlClassLoader.addURLFile(jarUrl);
	        LOADER_CACHE.put(new File(jarPath).getName(),urlClassLoader);
	    }
	    

	    public Class loadClass(String jarName,String name) throws ClassNotFoundException {
	        MyURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
	        if(urlClassLoader==null){
	            return null;
	        }
	        return urlClassLoader.loadClass(name);
	    }
}
