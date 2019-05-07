package com.bosssoft.install.activiti;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OriginalTest {
	public static Logger logger = LoggerFactory.getLogger(ConsigTest.class);
	public ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	public TaskService taskService = processEngine.getTaskService();
	public RuntimeService runtimeService = processEngine.getRuntimeService();
	public HistoryService histroyService = processEngine.getHistoryService();
	public RepositoryService repositoryService = processEngine.getRepositoryService();
	public IdentityService identityService = processEngine.getIdentityService();
	public ManagementService managementService = processEngine.getManagementService();
	public FormService formService=processEngine.getFormService();
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	
	@Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		//.addClasspathResource("com/finance/finance3.bpmn")
   	//	.addClasspathResource("com/technology/finance4.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		//.addClasspathResource("com/technology/finance4.png")//从classpath的资源中加载，一次只能加载一个文件\
   		.addClasspathResource("com/contract/DymaticForm2.bpmn")
   	   // .enableDuplicateFiltering()
   		//.disableVersionIncrease()
   		.deploy();//完成部署
   		
   		
  
   	}
	
	
	@Test
	public void startProcessTwo() throws Exception {
		
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("DymaticForm").latestVersion().singleResult();
		FormService formService = activitiRule.getFormService();
		StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
		assertNull(startFormData.getFormKey());
		
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "HenryYan");
		
		ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), formProperties);
		assertNotNull(processInstance);
	}
	
	@Test
	public void three(){
	   Map<String, String>	formProperties = new HashMap<String, String>();
		formProperties.put("setInFirstStep", "yy");
		formService.submitTaskFormData("1817506", formProperties);
	}
	
	@Test
	public void four(){
		Task task = taskService.createTaskQuery().taskName("Second Step").singleResult();
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormProperty> formProperties2 = taskFormData.getFormProperties();
		for (FormProperty formProperty : formProperties2) {
			System.out.println(formProperty.getId());
			System.out.println(formProperty.getValue());
		}
	}
}
