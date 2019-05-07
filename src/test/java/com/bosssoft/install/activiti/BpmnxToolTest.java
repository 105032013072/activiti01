package com.bosssoft.install.activiti;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.bpmn.model.FormDefinition;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.engine.BpmnxTool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.ParticipatorSourceItem;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.diagram.ActivityParticipatorInfo;
import org.activiti.engine.impl.diagram.MutiActivityProperty;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.runtime.OperationType;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.junit.Test;

public class BpmnxToolTest extends BaseTest{
	
	@Test
	public void getTaskForm(){
		FormDefinition formDefinition=bpmnxTool.getTaskFormDefinition("1370018");
		
		System.out.println(formDefinition.getFormPage());
		System.out.println(formDefinition.getFormType());
		
		for (FormProperty formProperty : formDefinition.getFormProperties()) {
			System.out.println(formProperty.getId());
		}
		
	
	}
	
	@Test
	public void deployWithString(){
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream("com/contract/contract.bpmn");
		String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
		
		ProcessDefinition processDefinition=bpmnxTool.deployWithString(result, "04",false);
		System.out.println(processDefinition.getId());
	}
	
	
	
	//部署版本号不递增
	@Test
	public void disableVersionIncreaseTest() throws InterruptedException{
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream("com/finance/finance3.bpmn");
		String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
		ProcessDefinition processDefinition=bpmnxTool.deployWithString(result, "04",true);
		System.out.println(processDefinition.getId()
				+" "+processDefinition.getCategory()
				+" "+processDefinition.getDiagramResourceName()
				+" "+processDefinition.getName()+" "+processDefinition.getVersion());
		
		Thread.sleep(60000);
		ProcessDefinitionEntity	processDefinitionEntity=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
		System.out.println("name："+processDefinitionEntity.getName());
		
		
		Thread.sleep(60000);
		
		
		 inputStream =this.getClass().getClassLoader().getResourceAsStream("com/technology/finance5.bpmn");
		 result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
	     processDefinition=bpmnxTool.deployWithString(result, "05",true);
		 
		Thread.sleep(60000);
		
	    processDefinitionEntity=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
		System.out.println("name："+processDefinitionEntity.getName());
		
		
	}
	
	@Test
	public void getLatestVersionProcessInstance(){
		List<HistoricProcessInstance>  result=bpmnxTool.getLatestVersionProcessInstance("finance");
		
		for (HistoricProcessInstance historicProcessInstance : result) {
			System.out.println(historicProcessInstance.getName());
		}
	}
	
	@Test
	public void getAllParticipator(){
		List<ActivityParticipatorInfo>  list=bpmnxTool.getAllActivitiParticipatories("Process_1:1:745006");
		
		for (ActivityParticipatorInfo activityParticipatorInfo : list) {
			StringBuffer buffer=new StringBuffer();
			buffer.append(activityParticipatorInfo.getActivityId()+"  ");
			buffer.append("类型："+activityParticipatorInfo.getActivityType()+" ");
			if(activityParticipatorInfo.getMutiActivityProperty()!=null){
				
				buffer.append("模式： "+activityParticipatorInfo.getMutiActivityProperty().getMultiMode()+"  ");
				buffer.append("提前完成的条件： "+activityParticipatorInfo.getMutiActivityProperty().getCompletionCondition()+"  ");
			}
			
			List<Participator> participatorList=activityParticipatorInfo.getParticipatorList();
			for (Participator participator : participatorList) {
				buffer.append(participator.getParticipatorName()+"  ");
			}
			System.out.println(buffer.toString());
		}
		
	}
	
	@Test
	public void getActiveParticipator(){
		List<ActivityParticipatorInfo> list=bpmnxTool.getHisAndCurrentActiveParticipatories("760008");
		for (ActivityParticipatorInfo activityParticipatorInfo : list) {
			StringBuffer buffer=new StringBuffer();
			buffer.append(activityParticipatorInfo.getActivityId()+"  ");
			buffer.append("类型："+activityParticipatorInfo.getActivityType()+" ");
			
			buffer.append("所有参与者： ");
			List<Participator> participatorList=activityParticipatorInfo.getParticipatorList();
			for (Participator participator : participatorList) {
				buffer.append(participator.getParticipatorName()+"  ");
			}
			
			
          if(activityParticipatorInfo.getMutiActivityProperty()!=null){//多实例
        	  MutiActivityProperty mutiActivityProperty=activityParticipatorInfo.getMutiActivityProperty();
        	  List<Participator> conditionList=mutiActivityProperty.getApproveList();
        	  buffer.append(" 同意列表： ");
        	  for (Participator participator : conditionList) {
        		  buffer.append(participator.getParticipatorName()+" ");
			  }
        	  
        	  conditionList=mutiActivityProperty.getDisapproveList();
        	  buffer.append(" 不同意列表： ");
        	  for (Participator participator : conditionList) {
        		  buffer.append(participator.getParticipatorName()+" ");
			  }
        	  
        	  conditionList=mutiActivityProperty.getUnCompleteList();
        	  buffer.append(" 未完成： ");
        	  for (Participator participator : conditionList) {
        		  buffer.append(participator.getParticipatorName()+" ");
			  }
        	  
		  }
			System.out.println(buffer.toString());
		}
	}
	
	@Test
	public void testBack(){
		/*List<Task> list=taskService.createTaskQuery().processInstanceId("processInstanceId").taskDefinitionKey("key").list();
		
		String operationType=null;
		if(list.size()>0){
			operationType=list.get(0).getOperationFlag();
		}
		
		if(OperationType.BACK.toString().equals(operationType)||OperationType.WITHDRAW.toString().equals(operationType)){
		   System.out.println("是退回任务");
		}*/
		
		
      List<Task> list=taskService.createTaskQuery().processInstanceId("processInstanceId").taskDefinitionKey("key").list();
	  if(list.size()>0){
		  if(!list.get(0).isNormal()){
			  System.out.println("为回退任务");
		  }
	  }
	}
	
	@Test
	public void testGetSourceList(){
		bpmnxTool.getHisAndCurrentActiveParticipatories("640061");

	}
}
