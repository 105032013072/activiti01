package com.bosssoft.install.activiti.cache;

import java.io.FileInputStream;
import java.io.InputStream;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Model;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheProcessDefinitionTest extends CacheBaseTest{
	
	private String processDefinitionId;
	
	private String modelId;
	
	@Before
	public void before() throws Exception{
		//先部署流程定义 
		Model model=super.createAndDeployModel("cacheTest/process_1_version1.bpmnx");
		processDefinitionId=model.getProcessDefinitionId();
		modelId=model.getId();
	}

	
	@Test
	public void getProcessDefinition() throws Exception{
	   //获取流程定义对象    从缓存中获取
	   System.out.println("==========查询流程定义对象    from cache");
	   ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
	}
	
	@Test
	public void updateProcessDefinition() throws Exception{
		String newProcessDef=readAsString("cacheTest/process_1_version1_update.bpmnx");
		repositoryService.saveModelAndUpdateProcessDefinition(modelId, newProcessDef);
		
		System.out.println("==========强制更新之后 查询流程定义对象  第一次    from db");
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(processDefinitionId);
	    assertEquals("process_1_update", processDefinitionEntity.getName());
		
		System.out.println("==========强制更新之后 查询流程定义对象  第二次    from cache");
		 processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(processDefinitionId);
		 
		 assertEquals("process_1_update", processDefinitionEntity.getName());
	}
	
	@Test
	public void deleteProcessDefinition(){
		System.out.println("==========查询流程定义对象    from cache");
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		
		//删除流程定义
		repositoryService.deleteProcessDefinition(processDefinitionId, true);
		
		System.out.println("删除流程定义之后  查询流程定义对象  from db");
		processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		assertNull(processDefinitionEntity);
	}
	
	/**
	 * 缓存序列化和反序列化的测试  主要针对redis
	 */
	@Test
	public void cacheSerializeTest(){
		
	}
}
