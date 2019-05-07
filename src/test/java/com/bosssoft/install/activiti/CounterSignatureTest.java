package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class CounterSignatureTest extends BaseTest{
    
	@Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		.addClasspathResource("com/finance/finance3.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		.addClasspathResource("com/finance/finance3.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
   	}
    
  
 	@Test
 	public void startProcess() throws InterruptedException {
 	    
 		identityService.setAuthenticatedUserId("u01");
 		
 	
 		
 		Map<String, Object> variable=new HashMap<String, Object>();
 		List<String> list=new ArrayList<String>();
 		list.add("AA");
 		list.add("BB");
 		variable.put("users", list);
 		
 		
 		ProcessInstance in=runtimeService.startProcessInstanceByKey("finance", "business_01", variable);
 		
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "报销审批_mike");

 	}
	
 	
 	@Test
	public void deleteDeployment() {
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery()
				.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId());

			processEngine.getRepositoryService().deleteDeploymentCascade(deployment.getId());
		}
	}
 	
 	@Test
 	public void complet() throws InterruptedException{
 		taskService.complete("4255011");
 		Thread.sleep(6000000);
 		
 	}
 	
 	@Test
 	public void claim(){
 		taskService.claim("4042527", "u1");
 		
 	}
 	
 	@Test
 	public void test(){
 		Map<String,String> map=new HashMap<String, String>();
 		map.put("remid_type", "task");
 		map.put("taskId", "task01");
 		JSONObject j=new JSONObject(map);
 		
 		System.out.println(j.toString());

 	}
}
