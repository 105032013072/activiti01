package com.bosssoft.install.activiti;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.consign.ConsignDefType;
import org.activiti.engine.impl.persistence.entity.AssociatedProcdefEntity;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.impl.persistence.entity.SubsequentProcdefEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.junit.Test;

import com.mysql.fabric.xmlrpc.base.Data;

public class RegionRepostoryTest extends BaseTest{

	
	public  static final String  ORGCODE_1="code1" ;
	public  static final String  ORGCODE_2="code2" ;
	
	//==========model
    /**
     * 保存model
     */
	@Test
	public void modelSaveTest(){
		//repositoryService.saveModel(null, , "1");
		repositoryService.saveModel(null, getString("com/regionTest/process-1.bpmnx"), "1", null);
		repositoryService.saveModel(null, getString("com/regionTest/process-1.bpmnx"), "1", null);
		
		
	}
	
	/**
	 * 未部署的流程更新
	 */
	@Test
	public void modelUpdate(){
		//查询code1的流程，更新
		Model model=repositoryService.createModelQuery().modelKey("Process_1").modelOrgCode(ORGCODE_1).singleResult();
		repositoryService.saveModel(model.getId(), getString("com/regionTest/process-1-update.bpmnx"), "1", ORGCODE_1);
	}
	
	/**
	 * 部署流程
	 */
	@Test
	public void modelDeploy(){
		//code1的流程model部署,model中有orgCode，所以部署时可以不需要传递机构代码
		Model model=repositoryService.createModelQuery().modelKey("Process_1").modelOrgCode(ORGCODE_1).singleResult();
		repositoryService.saveModelAndDeploy(model.getId(), getString("com/regionTest/process-1.bpmnx"), "1");
		
		//code2的model删除，然后直接保存并且部署，需要传递机构代码
		Model model2=repositoryService.createModelQuery().modelKey("Process_1").modelOrgCode(ORGCODE_2).singleResult();
		repositoryService.deleteModel(model2.getId(), true);
		repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-1-update.bpmnx"), "1", ORGCODE_2);
	}
	
	/**
	 * 强制更新流程定义
	 */
	@Test
	public void processDefinitionUpdate(){
		Model model=repositoryService.saveModel(null, getString("com/regionTest/process-1-update.bpmnx"), "1", ORGCODE_1);
		repositoryService.saveModelAndUpdateProcessDefinition(model.getId(), getString("com/regionTest/process-1-update.bpmnx"));
		//Model model=repositoryService.createModelQuery().modelKey("Process_1").modelVersion(2).modelOrgCode(ORGCODE_1).singleResult();
	}
	
	/**
	 * 设置默认流程
	 */
	@Test
	public void setDefaultProcess(){
		Model model=repositoryService.createModelQuery().modelKey("Process_1").modelVersion(1).modelOrgCode(ORGCODE_2).singleResult();
		repositoryService.setDefaultProcess(model.getId());
		
		Model model2=repositoryService.createModelQuery().modelKey("Process_1").latestVersion().modelOrgCode(ORGCODE_2).singleResult();
	    repositoryService.setDefaultProcess(model2.getId());
	}
	
	//=============多种类型流程model保存 部署
	@Test
	public void  saveMutiType(){
		/*repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-A.bpmnx"), "1");
		repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-A.bpmnx"), "1");*/
		
		
		repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-B.bpmnx"), "1", ORGCODE_1);
		repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-B.bpmnx"), "1", ORGCODE_2);
		
		
		repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-C.bpmnx"), "1", ORGCODE_1);
		repositoryService.saveModelAndDeploy(null, getString("com/regionTest/process-C.bpmnx"), "1", ORGCODE_2);
	}
	
	
	
	//======定义代理关系
	@Test
	public void consignTest(){
		repositoryService
		.createProcessConsignDefinition()
		.consignDefinitionId("87501")
		.consignor("u01")
		.addConsignee(new Participator("u02", "u02_name", IdentityEnum.USER, null))
		.consignDefType(ConsignDefType.PART)
		.addExcludeProcess("Process_1", ORGCODE_1)
		.startTime("2019-06-11")
    	.endTime("2019-8-21")
		.save();
		

		repositoryService
		.createProcessConsignDefinition()
		.consignor("u02")
		.addConsignee(new Participator("u03", "u03_name", IdentityEnum.USER, null))
		.consignDefType(ConsignDefType.ALL)
		.startTime("2019-06-11")
    	.endTime("2019-8-21")
		.save();
		
	}
	
	
	//=====流程关联关系
	@Test
	public void associatedProcess(){
		/*AssociatedProcdefEntity associatedProcdefEntity=new AssociatedProcdefEntity();
		associatedProcdefEntity.setAssociatedBizKey("key1");
		associatedProcdefEntity.setOrder(1);
		associatedProcdefEntity.setOrgCode(ORGCODE_1);
        associatedProcdefEntity.setProcessDefinitionKey("Process_A");
        
        List<SubsequentProcdefEntity> list=new ArrayList<SubsequentProcdefEntity>();
        SubsequentProcdefEntity sub1=new SubsequentProcdefEntity();
        sub1.setOrgCode(ORGCODE_1);
        sub1.setProcessDefinitionKey("Process_B");
        list.add(sub1);
        
		repositoryService.saveAssociatedProcdef(associatedProcdefEntity, list);*/
		
		AssociatedProcdefEntity associatedProcdefEntity=new AssociatedProcdefEntity();
		associatedProcdefEntity.setAssociatedBizKey("key1");
		associatedProcdefEntity.setOrder(1);
		associatedProcdefEntity.setOrgCode(ORGCODE_2);
        associatedProcdefEntity.setProcessDefinitionKey("Process_A");
        
        List<SubsequentProcdefEntity> list=new ArrayList<SubsequentProcdefEntity>();
        SubsequentProcdefEntity sub1=new SubsequentProcdefEntity();
        sub1.setOrgCode(ORGCODE_2);
        sub1.setProcessDefinitionKey("Process_C");
        list.add(sub1);
        
		repositoryService.saveAssociatedProcdef(associatedProcdefEntity, list);
		
	}
	
	
	//=====query
	@Test
	public void deploymentQuery(){
		List<Deployment> list=   repositoryService.createDeploymentQuery().list();
		System.out.println("=====all");
		for (Deployment deployment : list) {
			System.out.println(deployment.getId()+"  "+deployment.getOrgCode());
		}
		
		list=  repositoryService.createDeploymentQuery().deploymentOrgCode(ORGCODE_1).list();
		System.out.println("=====ORGCODE_1");
		for (Deployment deployment : list) {
			System.out.println(deployment.getId()+"  "+deployment.getOrgCode());
		}
		
		
		list=  repositoryService.createDeploymentQuery().deploymentOrgCode(ORGCODE_2).list();
		System.out.println("=====ORGCODE_1");
		for (Deployment deployment : list) {
			System.out.println(deployment.getId()+"  "+deployment.getOrgCode());
		}
	}
	
	@Test
	public void modelQuery() {
		List<Model> list = repositoryService.createModelQuery().list();
		System.out.println("===all");
		for (Model model : list) {
			System.out.println(model.getId() + "  " + model.getProcessDefinitionId() + "  " + model.getOrgCode());
		}

		list = repositoryService.createModelQuery().modelOrgCode(ORGCODE_1).list();
		System.out.println("===code1  all");
		for (Model model : list) {
              System.out.println(model.getId()+" "+model.getVersion());
		}
		
		String modelId=null;
		ModelEntity modelEntity = (ModelEntity) repositoryService.createModelQuery().modelOrgCode(ORGCODE_1).defaultProcess().deployed().singleResult();
		if(modelEntity!=null) modelId=modelEntity.getId();
		System.out.println("code1 default : "+modelId);
		
		 modelEntity = (ModelEntity) repositoryService.createModelQuery().modelOrgCode(ORGCODE_2).defaultProcess().deployed().singleResult();
		if(modelEntity!=null) modelId=modelEntity.getId();
		System.out.println("code2 default : "+modelId);
		
		
		
		 modelEntity = (ModelEntity) repositoryService.createModelQuery().modelOrgCode(ORGCODE_1).latestVersion().singleResult();
		if(modelEntity!=null) modelId=modelEntity.getId();
		System.out.println("code1 latestVersion : "+modelId);
		
		 modelEntity = (ModelEntity) repositoryService.createModelQuery().modelOrgCode(ORGCODE_2).latestVersion().singleResult();
		if(modelEntity!=null) modelId=modelEntity.getId();
		System.out.println("code2 latestVersion : "+modelId);
		
	}
	
	@Test
	public void processDefinitionQuery(){
		String processDefinitionKey="Process_1";
		List<ProcessDefinition> list=	repositoryService.createProcessDefinitionQuery().list();
		System.out.println("=====all");
		for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId()+"  "+processDefinition.getOrgCode());
		}
		
		System.out.println("===orgCode1");
		 list=	repositoryService.createProcessDefinitionQuery()
				 .processDefinitionKey(processDefinitionKey).processDefinitionOrgCode(ORGCODE_1).list();
		 for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId()+"  "+processDefinition.getOrgCode());
		}
		 
		 System.out.println("===orgCode2");
		 list=	repositoryService.createProcessDefinitionQuery()
				 .processDefinitionKey(processDefinitionKey).processDefinitionOrgCode(ORGCODE_2).list();
		 for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId()+"  "+processDefinition.getOrgCode());
		}
		 
		String processDefinitionId=null;
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery()
				.processDefinitionOrgCode(ORGCODE_1).latestVersion().singleResult();
		 if(processDefinition!=null){
			 processDefinitionId=processDefinition.getId();
		 }
		 System.out.println("code1 latestVersion: "+processDefinitionId);
		 
		 
		processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionOrgCode(ORGCODE_2).latestVersion().singleResult();
		if (processDefinition != null) {
			processDefinitionId = processDefinition.getId();
		}
		System.out.println("code2 latestVersion: " + processDefinitionId);
		
		
		processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionOrgCode(ORGCODE_1).processDefinitionVersion(1).singleResult();
		if (processDefinition != null) {
			processDefinitionId = processDefinition.getId();
		}
		System.out.println("code1 version1: " + processDefinitionId);
		
		processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionOrgCode(ORGCODE_2).processDefinitionVersion(1).singleResult();
		if (processDefinition != null) {
			processDefinitionId = processDefinition.getId();
		}
		System.out.println("code2 version1: " + processDefinitionId);
			 
		 
	}
	@Test
	public void getAssociated(){
		List<ProcessDefinition>  list=repositoryService.getExecutableSubsequentProcess("135001", "key1");
		System.out.println("=====code2");
		for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId()+" "+processDefinition.getOrgCode());
		}
		
		
		list=repositoryService.getExecutableSubsequentProcess("135008", "key1");
		System.out.println("=====code1");
		for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId()+" "+processDefinition.getOrgCode());
		}
	}
	
    
	
	
	private   String getString(String classLoadPath){
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream(classLoadPath);
		String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
		return result;
	}
	
	
}
