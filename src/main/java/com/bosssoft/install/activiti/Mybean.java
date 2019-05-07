package com.bosssoft.install.activiti;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

public class Mybean implements Serializable{
  public String print(String name){
	  return "print"+name;
  }
  
  public void invokeTask(DelegateTask task){
	  System.out.println("流程实例ID："+task.getProcessInstanceId());
	  task.setVariable("setByTask", "setted by DelegateTask, "+task.getVariable("name"));
  }
  
  public void printByKey(DelegateExecution ex){
	  String businessKey=ex.getProcessBusinessKey();
	  System.out.println("businessKey: "+businessKey);
  }
}


