package com.bosssoft.install.activiti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class CounterSignService implements Serializable{
	
	 private static final long serialVersionUID = 1L;
	
	public List<String> getUsers(ActivityExecution execution){
		List<String> list=new ArrayList<String>();
		
		list.add("r1");
		list.add("r2");
		list.add("r3");
		list.add("r4");
		
	    return list;
	}
	
	public boolean isComplete(ActivityExecution execution){
		//完成会签的次数
		  Integer completeCounter=(Integer)execution.getVariable("nrOfCompletedInstances");
		  System.out.println("完成会签的次数: "+completeCounter);
		  //总循环次数
		  Integer instanceOfNumbers=(Integer)execution.getVariable("nrOfInstances");
		  System.out.println("总循环次数: "+instanceOfNumbers);
		  
		  String solId=(String)execution.getVariable("solId");
		  System.out.println("solId: "+solId);
		  
		  System.out.println("----------------------------");
		  
		  if(completeCounter==instanceOfNumbers-2) return true;
		  else return false;
		
	  
	}
}
