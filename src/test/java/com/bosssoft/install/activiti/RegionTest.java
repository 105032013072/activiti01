package com.bosssoft.install.activiti;

import org.junit.Test;

public class RegionTest  extends BaseTest{

	@Test
	public void hisTest(){
		histroyService.createHistoricActivityInstanceQuery().activityOrgCode(orgCode);
		histroyService.createHistoricProcessInstanceQuery().processInstanceOrgCode(orgCode);
		histroyService.createHistoricTaskInstanceQuery().taskOrgCode(orgCode);
	
	}
	
	@Test
	public void reTest(){
		
		repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds)
		repositoryService.createDeploymentQuery().deploymentOrgCode(orgCode);
		repositoryService.createAssociatedProcdefQuery().associatedProcdefOrgCode(orgCode);
		repositoryService.createConsignDefineExcludeQuery().orgCode(orgCode);
		repositoryService.createModelQuery().modelOrgCode(orgCode)
		repositoryService.createProcessDefinitionQuery().processDefinitionOrgCode(orgCode)
		repositoryService.createSubsequentProcdefQuery().subsequentOrgCode(orgCode)
	}
	
	@Test
	public void runTest(){
		runtimeService.createExecutionQuery().executionOrgCode(orgCode);
		runtimeService.createProcessInstanceQuery();
		runtimeService.startProcessInstanceByKeyAndTenantId("", "", "");
		taskService.createTaskQuery().taskOrgCode(orgCode)
		managementService.createJobQuery().jobOrgCode()
		
	}
	
}
