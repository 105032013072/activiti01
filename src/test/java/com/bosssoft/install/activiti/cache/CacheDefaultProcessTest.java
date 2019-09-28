package com.bosssoft.install.activiti.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.activiti.engine.repository.Category;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CacheDefaultProcessTest extends CacheBaseTest{

	private String processDefinitionKey="process_1";
	
	@Before
	public void before() throws Exception{
		//初始化流程类别
	   repositoryService.saveCategory(null, "测试", null, false);
		
		//部署同一个流程定义key的多个版本的流程
	   Model model=super.createAndDeployModel("cacheTest/process_1_version1.bpmnx");
	   super.createAndDeployModel("cacheTest/process_1_version2.bpmnx");
	   super.createAndDeployModel("cacheTest/process_1_version3.bpmnx");
	}
	
	@Test
	public void getDefaultProcess(){
		
		//版本为1的流程设置默认流程
		Model model=repositoryService.createModelQuery().modelKey(processDefinitionKey).modelVersion(1).deployed().singleResult();
		repositoryService.setDefaultProcess(model.getId());
		
		//第一次查询默认流程 会从数据库中获取
		System.out.println("=====================默认流程：version_1   第一次查询  from db");
		String defaultProcessDefinitionId=repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(model.getProcessDefinitionId(), defaultProcessDefinitionId);
		
		//第二次查询  会从缓存中获取
		System.out.println("=====================默认流程：version_1   第二次查询 from cache");
		defaultProcessDefinitionId=repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(model.getProcessDefinitionId(), defaultProcessDefinitionId);
		
		
	    //重新设置默认流程 为版本为2
		
		model=repositoryService.createModelQuery().modelKey(processDefinitionKey).modelVersion(2).deployed().singleResult();
		repositoryService.setDefaultProcess(model.getId());
		
		//再次查询默认流程  从数据库中获取
		System.out.println("=====================默认流程：version_2   第一次查询 from db");
		defaultProcessDefinitionId=repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(model.getProcessDefinitionId(), defaultProcessDefinitionId);
		
		//重新查询，从缓存中获取
		System.out.println("=====================默认流程：version_2   第二次查询  from cache");
		defaultProcessDefinitionId=repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(model.getProcessDefinitionId(), defaultProcessDefinitionId);
		
		
	}
	
	@Test
	public void deleteProcess(){
		
		// 版本为1的流程设置默认流程
		Model model = repositoryService.createModelQuery().modelKey(processDefinitionKey).modelVersion(1).deployed().singleResult();
		String expectedValue=model.getId();
		repositoryService.setDefaultProcess(model.getId());

		// 第一次查询默认流程 会从数据库中获取
		System.out.println("=====================默认流程：version_1   第一次查询  from db");
		String defaultProcessDefinitionId = repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, defaultProcessDefinitionId);

		// 第二次查询 会从缓存中获取
		System.out.println("=====================默认流程：version_1   第二次查询   from cache");
		defaultProcessDefinitionId = repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, defaultProcessDefinitionId);
		
		//删除版本为2的model  （非默认流程）
		model = repositoryService.createModelQuery().modelKey(processDefinitionKey).modelVersion(2).deployed().singleResult();
		repositoryService.deleteModel(model.getId());
		
		System.out.println("=====================删除非默认流程流程后  查询  from cache");
		defaultProcessDefinitionId = repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, defaultProcessDefinitionId);
		
		//删除默认流程
		System.out.println("=====================删除默认流程流程后  查询  from db");
		repositoryService.deleteModel(expectedValue);
		defaultProcessDefinitionId = repositoryService.getDefaultProcessDefinitionId(processDefinitionKey);
		assertNull(defaultProcessDefinitionId);
	}
	
	@Test
	public void startProcessForDefaultProcess(){

		// 版本为1的流程设置默认流程
		Model model = repositoryService.createModelQuery().modelKey(processDefinitionKey).modelVersion(1).deployed().singleResult();
		String expectedValue = model.getProcessDefinitionId();
		repositoryService.setDefaultProcess(model.getId());
		
	    //第一次发起流程  从db中查询获取默认流程Id
		System.out.println("=====================第一次发起流程  查询  from db");
		ProcessInstance inst=	runtimeService
				.createProcessInstanceBuilder()
				.processDefinitionKey(processDefinitionKey)
				.start(); 
		assertEquals(expectedValue, inst.getProcessDefinitionId());
		
		
		System.out.println("=====================第二次发起流程  查询  from cache");
		 inst=	runtimeService
				.createProcessInstanceBuilder()
				.processDefinitionKey(processDefinitionKey)
				.start(); 
		assertEquals(expectedValue, inst.getProcessDefinitionId());
	}
	
	@After
	public void tearDown() throws Exception{
	    //删除model
		List<Model> list=repositoryService.createModelQuery().list();
		for (Model model : list) {
			repositoryService.deleteModel(model.getId(), true);
		}
		
		//删除类别
		List<Category> caList=repositoryService.createCategoryQuery().list();
		for (Category category : caList) {
			repositoryService.deleteCategory(category.getId());
		}
	}
	
}
