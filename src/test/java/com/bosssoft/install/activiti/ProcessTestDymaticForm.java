package com.bosssoft.install.activiti;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;

import groovy.ui.SystemOutputInterceptor;
public class ProcessTestDymaticForm extends BaseTest{

	
	private String filename = "/Users/henryyan/work/workspaces/eclipse/activiti-5.9/src/main/resources/diagrams/form/DymaticForm.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("DymaticForm").latestVersion().singleResult();
		FormService formService = activitiRule.getFormService();
		StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
		assertNull(startFormData.getFormKey());
		
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "HenryYan");
		
		ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), formProperties);
		assertNotNull(processInstance);
		
		// 杩愯鏃跺彉閲�
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
		assertEquals(variables.size(), 1);
		Set<Entry<String, Object>> entrySet = variables.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		
		// 鍘嗗彶璁板綍
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricDetail> list = historyService.createHistoricDetailQuery().formProperties().list();
		assertEquals(1, list.size());
		
		// 鑾峰彇绗竴涓妭鐐�
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("First Step", task.getName());
		
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		assertNotNull(taskFormData);
		assertNull(taskFormData.getFormKey());
		List<FormProperty> taskFormProperties = taskFormData.getFormProperties();
		assertNotNull(taskFormProperties);
		/*for (FormProperty formProperty : taskFormProperties) {
			System.out.println(ToStringBuilder.reflectionToString(formProperty));
		}*/
		formProperties = new HashMap<String, String>();
		formProperties.put("setInFirstStep", "01/12/2012");
		formService.submitTaskFormData(task.getId(), formProperties);
		
		// 鑾峰彇绗簩涓妭鐐�
		task = taskService.createTaskQuery().taskName("Second Step").singleResult();
		assertNotNull(task);
		taskFormData = formService.getTaskFormData(task.getId());
		assertNotNull(taskFormData);
		List<FormProperty> formProperties2 = taskFormData.getFormProperties();
		assertNotNull(formProperties2);
		assertEquals(1, formProperties2.size());
		assertNotNull(formProperties2.get(0).getValue());
		assertEquals(formProperties2.get(0).getValue(), "01/12/2012");
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
