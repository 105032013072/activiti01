package com.bosssoft.install.activiti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Persion implements Serializable{
  
	public Persion(String name){
		this.name=name;
	}
		
	private String name;
	
	private List<String> list=new ArrayList<String>();
	
	protected byte[] bytes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	
    
	
	
}
