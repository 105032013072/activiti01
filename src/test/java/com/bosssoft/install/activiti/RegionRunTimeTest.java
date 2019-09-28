package com.bosssoft.install.activiti;

import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class RegionRunTimeTest extends BaseTest{

	public  static final String  ORGCODE_1="code1" ;
	public  static final String  ORGCODE_2="code2" ;
	
	//===startProcess
	@Test
	public void startProcess(){
		runtimeService.createProcessInstanceBuilder()
		              .processDefinitionKey("Process_1")
		              .processStarter("u01")
		              .orgCode(ORGCODE_2)
		              .start();
		
		/*runtimeService.createProcessInstanceBuilder()
        .processDefinitionKey("Process_A")
        .processStarter("u01")
        .orgCode(ORGCODE_1)
        .start();*/
	}
	
	
	//======query
	@Test
	public void executionQuery(){
		List<Execution> list=runtimeService.createExecutionQuery().list();
		System.out.println("====all");
		for (Execution execution : list) {
			System.out.println(execution.getId()+"  "+((ExecutionEntity)execution).getOrgCode());
		}
		
		
		list=runtimeService.createExecutionQuery().executionOrgCode(ORGCODE_1).list();
		System.out.println("====orgCode1");
		for (Execution execution : list) {
			System.out.println(execution.getId()+"  "+((ExecutionEntity)execution).getOrgCode());
		}
		
		list=runtimeService.createExecutionQuery().executionOrgCode(ORGCODE_2).list();
		System.out.println("====orgCode2");
		for (Execution execution : list) {
			System.out.println(execution.getId()+"  "+((ExecutionEntity)execution).getOrgCode());
		}
	}
	
	@Test
	public void processQuery(){
		List<ProcessInstance> list=	runtimeService.createProcessInstanceQuery().list();
		System.out.println("===all");
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId()+"  "+((ExecutionEntity)processInstance).getOrgCode());
		}
		
		list=	runtimeService.createProcessInstanceQuery().processInstanceOrgCode(ORGCODE_1).list();
		System.out.println("===code1");
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId()+"  "+((ExecutionEntity)processInstance).getOrgCode());
		}
		
		list=	runtimeService.createProcessInstanceQuery().processInstanceOrgCode(ORGCODE_2).list();
		System.out.println("===code2");
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId()+"  "+((ExecutionEntity)processInstance).getOrgCode());
		}
		
	}
	
	@Test
	public void taskQuery(){
		List<Task> list=taskService.createTaskQuery().list();
		System.out.println("====all");
		for (Task task : list) {
			System.out.println(task.getId()+" "+task.getTaskDefinitionKey()+"  "+task.getOrgCode());
		}
		
		list=taskService.createTaskQuery().taskOrgCode(ORGCODE_1).list();
		System.out.println("====code1");
		for (Task task : list) {
			System.out.println(task.getId()+" "+task.getTaskDefinitionKey()+"  "+task.getOrgCode());
		}
		
		
		list=taskService.createTaskQuery().taskOrgCode(ORGCODE_2).list();
		System.out.println("====code2");
		for (Task task : list) {
			System.out.println(task.getId()+" "+task.getTaskDefinitionKey()+"  "+task.getOrgCode());
		}
		
	}
	
}
