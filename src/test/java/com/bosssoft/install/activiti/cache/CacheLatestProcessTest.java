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

public class CacheLatestProcessTest extends CacheBaseTest{

	private String processDefinitionKey="process_1";
	
	@Before
	public void before() throws Exception{
		//初始化流程类别
	   repositoryService.saveCategory(null, "测试", null, false);
	}
	
	@Test
	public void getLatestProcess() throws Exception{
		//部署一个的流程
		Model model=super.createAndDeployModel("cacheTest/process_1_version1.bpmnx");
		
		//版本为1的流程为最高版本流程
		assertEquals("process_1_version1", model.getName());
		String expectedValue=model.getProcessDefinitionId();
		
		//第一次查询最新流程 会从数据库中获取
		System.out.println("=====================最新流程：version_1  第一次查询  from db");
		String latesProcessDefinitionId=repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, latesProcessDefinitionId);
		
		//第二次查询  会从缓存中获取
		System.out.println("=====================最新流程：version_1  第二次查询 from cache");
		latesProcessDefinitionId=repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, latesProcessDefinitionId);
		
		
	    //再次部署流程，最新版本的流程为版本2
		super.createAndDeployModel("cacheTest/process_1_version2.bpmnx");
		model=repositoryService.createModelQuery().modelKey(processDefinitionKey).modelVersion(2).deployed().singleResult();
		assertEquals("process_1_version2", model.getName());
		expectedValue=model.getProcessDefinitionId();
		
		//再次查询最新流程  从数据库中获取
		System.out.println("=====================最新流程：version_2   第一次查询 from db");
		latesProcessDefinitionId=repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(model.getProcessDefinitionId(), latesProcessDefinitionId);
		
		//重新查询，从缓存中获取
		System.out.println("=====================最新：version_2   第二次查询  from cache");
		latesProcessDefinitionId=repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(model.getProcessDefinitionId(), latesProcessDefinitionId);
		
		
	}
	
	@Test
	public void deleteProcess() throws Exception{
		Model modelVersionOne=super.createAndDeployModel("cacheTest/process_1_version1.bpmnx");
		Model latestModel=super.createAndDeployModel("cacheTest/process_1_version2.bpmnx");
		String expectedValue=latestModel.getId();
		
		
		// 第一次查询最新流程 会从数据库中获取
		System.out.println("=====================最新流程：version_2   第一次查询  from db");
		String latesProcessDefinitionId = repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, latesProcessDefinitionId);

		// 第二次查询 会从缓存中获取
		System.out.println("=====================最新流程：version_2   第二次查询   from cache");
		latesProcessDefinitionId = repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, latesProcessDefinitionId);
		
		//删除版本为1的model  （非最新流程）
		repositoryService.deleteModel(modelVersionOne.getId());
		System.out.println("=====================删除非最新流程流程后  查询  from cache");
		latesProcessDefinitionId = repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertEquals(expectedValue, latesProcessDefinitionId);
		
		//删除最新流程
		repositoryService.deleteModel(expectedValue);
		System.out.println("=====================删除最新流程流程后  查询  from db");
		latesProcessDefinitionId = repositoryService.getLatestProcessDefinitionId(processDefinitionKey);
		assertNull(latesProcessDefinitionId);
	}
	
	@Test
	public void startProcessForLatestProcess() throws Exception{

		Model modelVersionOne=super.createAndDeployModel("cacheTest/process_1_version1.bpmnx");
		Model latestModel=super.createAndDeployModel("cacheTest/process_1_version2.bpmnx");
		String expectedValue=latestModel.getId();
		
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
