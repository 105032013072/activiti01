package com.bosssoft.install.activiti.cache;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cache.ActivitiCache;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.junit.Test;

import com.bosssoft.install.activiti.BaseTest;

public class CacheTest extends BaseTest{
	
	@Test
	public void processDefinitionTest(){
		//ProcessEngineConfigurationImpl processEngineConfigurationImpl=(ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
		ActivitiCache cache= ((ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration()).getActivitiCache();

		
		ProcessDefinitionEntity	rawProcessDefEntity=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("Process_2:1:527505");
		System.out.println(rawProcessDefEntity.getInitial());
		
		cache.addProcessDefinition(rawProcessDefEntity);
		ProcessDefinitionEntity redisGetProcessDefEntity= cache.getProcessDefinition(rawProcessDefEntity.getId());
		System.out.println(redisGetProcessDefEntity.getInitial());
	}
	
}
