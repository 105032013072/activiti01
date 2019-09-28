package com.bosssoft.install.activiti.multiSchemaVerification;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MutilClassLoader extends URLClassLoader{

	public MutilClassLoader(String path) throws MalformedURLException  {
		super(new URL[] {}, findParentClassLoader()); 
		addPathToURL(path);
	}

	public void addPathToURL(String path) throws MalformedURLException{
		URL jarUrl = new URL("jar:file:/"+path+"!/");
		addURL( jarUrl);
	}
	
	
	private static ClassLoader findParentClassLoader() {
		
		ClassLoader parent = MutilClassLoader.class.getClassLoader();
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        return parent;
	}

	

	

	

}
