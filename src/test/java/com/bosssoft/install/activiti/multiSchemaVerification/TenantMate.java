package com.bosssoft.install.activiti.multiSchemaVerification;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TenantInfoEntity;

public class TenantMate extends TenantInfoEntity{
  
    private Map<String,byte[]> resources=new HashMap<>();
	
	private String processClassRelativePath;
	

	public String getProcessClassRelativePath() {
		return processClassRelativePath;
	}

	public void setProcessClassRelativePath(String processClassRelativePath) {
		this.processClassRelativePath = processClassRelativePath;
	}

	public void addResource(String name,byte[] data){
		resources.put(name, data);
	}

	public Map<String, byte[]> getResources() {
		return resources;
	}

	public void setResources(Map<String, byte[]> resources) {
		this.resources = resources;
	}
	
	
	
}
