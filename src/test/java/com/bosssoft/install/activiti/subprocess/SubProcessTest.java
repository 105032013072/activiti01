package com.bosssoft.install.activiti.subprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.bpmn.data.MutiCallActivityOutputInfo;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.bosssoft.install.activiti.BaseTest;

public class SubProcessTest extends BaseTest{
	@Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		.addClasspathResource("com/subProcess/checkprocess.bpmn")
   		.deploy();//完成部署
  		
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		.addClasspathResource("com/subProcess/subProcess_1.bpmn")
   		.deploy();//完成部署
 
   	}
	
	
	@Test
 	public void startProcess() throws InterruptedException {

		
		List<String> list=new ArrayList<String>();
		list.add("aa");
		list.add("bb");
		
		Map<String,Object> var=new HashMap<String, Object>();
		var.put("total", 10);
		var.put("collection", list);
		//var.put("var1", "ee");
		//var.put("var2",new String[]{"aa","ff"});
		//var.put("total", 30);
		
		      
	      ProcessInstance inst=	runtimeService
				.createProcessInstanceBuilder()
				//.processDefinitionKey("subProcess")
				.processDefinitionKey("myProcess")
				.businessKey("business_01")
				.processStarter("u06")
				.setVariables(var)
				.processInstanceName("subProcess_111").start();
		       
		  System.out.println(inst.getId());    
		
 		
 	}
	
	@Test
	public void completeTask(){
		Map<String,Object> var=new HashMap<String, Object>();
		var.put("name_sub", "checkUserTask_two_2");
		var.put("age_sub", 33);
		
		taskService.complete("50012", var, false);
	}
	
	
	@Test 
	public void getVar(){
		Map<String,List<MutiCallActivityOutputInfo>> map=(Map<String, List<MutiCallActivityOutputInfo>>) taskService.getVariable("257505", ActVariable.CALLACTIVITY_VAR_OUTMAP);
	   if(map!=null){
		   for (Entry<String, List<MutiCallActivityOutputInfo>> entry : map.entrySet()) {
			System.out.println("----------"+entry.getKey());
			
			List<MutiCallActivityOutputInfo> list=entry.getValue();
			for (MutiCallActivityOutputInfo mutiCallActivityOutputInfo : list) {
				System.out.println(mutiCallActivityOutputInfo.getSubProcessInstId()+"   "+mutiCallActivityOutputInfo.getVarList());
				
			}
		}
	   }
	
	}
	
	@Test
	public void getHisVar(){
		HistoricVariableInstance historicVariableInstance=histroyService.createHistoricVariableInstanceQuery().processInstanceId("242501").variableName(ActVariable.CALLACTIVITY_VAR_OUTMAP).singleResult();
		Map<String,List<MutiCallActivityOutputInfo>> map=(Map<String, List<MutiCallActivityOutputInfo>>) historicVariableInstance.getValue();
		if(map!=null){
			   for (Entry<String, List<MutiCallActivityOutputInfo>> entry : map.entrySet()) {
				System.out.println("----------"+entry.getKey());
				
				List<MutiCallActivityOutputInfo> list=entry.getValue();
				for (MutiCallActivityOutputInfo mutiCallActivityOutputInfo : list) {
					System.out.println(mutiCallActivityOutputInfo.getSubProcessInstId()+"   "+mutiCallActivityOutputInfo.getVarList());
					
				}
			}
		   }
		
	}
	
	@Test
	public void setVar(){
		
	   List<MutiCallActivityOutputInfo> list=new ArrayList<MutiCallActivityOutputInfo>();
	   list.add(new MutiCallActivityOutputInfo("123"));
	   Map<String,List<MutiCallActivityOutputInfo>> map=new HashMap<String, List<MutiCallActivityOutputInfo>>();
	   map.put("one", list);
	   taskService.setVariable("172503", "test", map);
		
		//taskService.setVariable("35005", "subVar", "sub_hello");
	}
	
	@Test
	public void deleteDeployment() {
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery()
				.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId());

			processEngine.getRepositoryService().deleteDeploymentCascade(deployment.getId());
		    //repositoryService.deleteDeployment(deployment.getId(), false);
		}
	}
}
