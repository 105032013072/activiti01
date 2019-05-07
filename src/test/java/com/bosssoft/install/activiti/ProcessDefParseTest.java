package com.bosssoft.install.activiti;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FormDefinition;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.FormValue;
import org.activiti.bpmn.model.InvocationProperty;
import org.activiti.bpmn.model.RemindProperty;
import org.activiti.bpmn.model.TimeEffectiveDefinition;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.authority.AuthorityItem;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ActivityEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.RunTrail;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.notification.NotificationType;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ProcessDefParseTest extends BaseTest{
  

	@Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		//.addClasspathResource("com/subProcess/subProcess_1.bpmn")
   		.addClasspathResource("com/contract/process-test.bpmnx")
 //  .addClasspathResource("com/technology/finance4.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		//.addClasspathResource("com/contract/DymaticForm2.bpmn")
   	//	.addClasspathResource("com/technology/finance5.bpmn")
   	   // .enableDuplicateFiltering()
   		
   		.deploy();//完成部署 
   	} 
	

	@Test
 	public void startProcess() throws InterruptedException {

		
 		/*identityService.setAuthenticatedUserId("u03");
 	
 		ProcessInstance in=runtimeService.startProcessInstanceByKey("finance", "business_01");
 		
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "报销审批_mike_02");*/
		
		
		/*identityService.setAuthenticatedUserId("u03");
		
		ProcessInstanceBuilder  processInstanceBuilder=runtimeService.createProcessInstanceBuilder().processDefinitionKey("finance").businessKey("business_01").processInstanceName("报销审批_mike_02");
		processInstanceBuilder.start();
		
		Thread.sleep(600000000);*/
		Map<String,Object> processVar=new HashMap<String, Object>();
		processVar.put("key1", "key1");
		processVar.put("key2", false);
		    
	ProcessInstance inst=	runtimeService
				.createProcessInstanceBuilder()
				//.processDefinitionKey("Process_1")
				//.processDefinitionKey("Process_1")
				.processDefinitionId("Process_1:1:70005")
				.businessKey("key1")
				.processStarter("u01")
				.addVariable("condition", processVar)
				.processInstanceName("测试流程-2018-12-13")
				.start(); 
	
		  System.out.println(inst.getId());  
		  
		  //test
		  
		 
		  
		 /* BpmnParse bpmnParse=  Context.getProcessEngineConfiguration().getBpmnParser()
          .createParse()
          .sourceInputStream(inputStream)
          .setSourceSystemId(resourceName)
          .deployment(deployment)
          .name(resourceName);*/
		  
		  
		  BpmnParse bpmnParse=  Context.getProcessEngineConfiguration().getBpmnParser()
		          .createParse()
		         // .sourceInputStream()
		          .setSourceSystemId("'")
		        //  .deployment(deployment)
		          .name("");
		  
		  
		  bpmnParse.execute();
	        
	     
		  
		  
		  
 		
 	}
	
	/**
	 * 获取流程配置
	 * @throws InterruptedException 
	 */
	@Test
	public void getProcessFormPage() throws InterruptedException{
		
		ProcessDefinitionEntity	result=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("Process_1:1:277527");
		System.out.println("分类："+result.getCategory());
		System.out.println("name："+result.getName());
		
		StringBuffer buffer=new StringBuffer();
		FormDefinition formDefinition=result.getFormDefinition();
		System.out.println("--------表单----------");
		if(formDefinition!=null){
			//表单信息
			System.out.println("表单页："+formDefinition.getFormPage());
			System.out.println("表单类型: "+formDefinition.getFormType());
			for (FormProperty formProperty : formDefinition.getFormProperties()) {
			    buffer=new StringBuffer();
				buffer.append(formProperty.getId()+"  ");
				System.out.println();
				for (FormValue  formValue: formProperty.getFormValues()) {
					buffer.append("["+formValue.getId()+" "+formValue.getName()+" ]");
				}
				System.out.println(buffer.toString());
			}
		
		}
		
		//时效
		System.out.println("------时效-----");
		TimeEffectiveDefinition info=result.getTimeEffectiveDefinition();
		if(info!=null){
			 buffer=new StringBuffer();
			buffer.append(info.getCalendar()+" "+info.getTimeDuration());
			for (RemindProperty remindConfiguration : info.getRemindProperties()) {
				
			}
		}
		
		
		System.out.println("是否使用全局的通知配置："+result.getIsUseGlobalNotification());
		
		//通知渠道
		buffer=new StringBuffer();
		for (ChannelType channelType : result.getGlobalChannels()) {
			buffer.append(channelType.toString()+",");
		}
		System.out.println("通知渠道："+buffer.toString());
		
		//全局的通知配置
		buffer=new StringBuffer();
		for (NotificationType notificationType : result.getGlobalNotificationTypes()) {
			buffer.append(notificationType.toString()+" ");
		}
		System.out.println("全局的通知配置: "+buffer.toString());
		
		//权限
		buffer=new StringBuffer();
		
		for (AuthorityItem authorityItem : result.getAuthorityItems()) {
			buffer.append(authorityItem.getKey().toString()+" ");
		}
		System.out.println("权限配置: "+buffer.toString());
		
		//事件监听器
		buffer=new StringBuffer("事件监听器");
		List<InvocationProperty> listeners=	result.getBpmnxListeners();
		for (InvocationProperty invocationProperty : listeners) {
			String type=invocationProperty.getType();
			 buffer=new StringBuffer(" 类型："+type+"  ");
			 
			 if("HTTP".equals(type)){
				 buffer.append(invocationProperty.getUrl()+"  "+invocationProperty.getMethod()+"  "+invocationProperty.isSynchronous()+"  "+invocationProperty.getEvent());
			 }else if("RPC".equals(type)){
				buffer.append(invocationProperty.getExcutionId());
			 }
			 System.out.println(buffer);
		}
		
	
	    
	}
	
	/**
	 * 获取任务配置
	 */
	@Test
	public void getTaskDefinition(){
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("Process_1:1:50006");
		
		ActivityImpl activityImpl=processDefinitionEntity.findActivity("A");
		List<PvmTransition> outList=	activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : outList) {
			System.out.println(pvmTransition.getId());
			System.out.println(pvmTransition.getProperty("name"));
		}
        
		//ActivityImpl activityImpl = (ActivityImpl) processDefinitionEntity.findActivity("StartEvent_1");
		//System.out.println(activityImpl.getProperty("documentation"));
	    /*UserTaskActivityBehavior activityBehavior =(UserTaskActivityBehavior)
				 ( (ParallelMultiInstanceBehavior)
				 activityImpl.getActivityBehavior()).getInnerActivityBehavior();*/
		//UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activityImpl.getActivityBehavior();
		
		

		TaskDefinition taskDefinition = processDefinitionEntity.getTaskDefinitions().get("D");
		//System.out.println("document: "+taskDefinition.getDescriptionExpression().getExpressionText());
		
		
		for (ActivityEntity activityEntity : taskDefinition.getBackActivities()) {
			System.out.println(activityEntity.getActivityId());
		}
		
		//任务时效
		System.out.print("时效：");
		TimeEffectiveDefinition  timeEffectiveDefinition=taskDefinition.getTimeEffectiveDefinition();
		if(timeEffectiveDefinition!=null){
			System.out.println(timeEffectiveDefinition.getTimeDuration());
		}
		
		//获取任务表单
		FormDefinition formDefinition= taskDefinition.getFormDefinition();
		if(formDefinition!=null){
			System.out.println("表单页："+formDefinition.getFormPage());
			System.out.println("表单类型："+formDefinition.getFormType());
			
			System.out.println("任务办理人："+taskDefinition.getAssigneeExpression());
			System.out.println("任务候选人："+taskDefinition.getCandidateExpressions());
		}
		

		//是否自动完成
		System.out.println(taskDefinition.getIsAutoComplet());
		
		//知会
		List<Participator> cc=taskDefinition.getCarbonCopy();
	    for (Participator participator : cc) {
			System.out.println("知会："+participator.getParticipatorName());
		}
	    
	    
	  //全局的通知配置
	    StringBuffer buffer = new StringBuffer();
		for (NotificationType notificationType : taskDefinition.getNotificationTypes()) {
			buffer.append(notificationType.toString() + " ");
		}
		System.out.println("任务通知配置: " + buffer.toString());
		
		// 权限
		buffer = new StringBuffer();

		for (AuthorityItem authorityItem : taskDefinition.getAuthorityItems()) {
			buffer.append(authorityItem.getKey().toString() + " ");
		}
		System.out.println("权限配置: " + buffer.toString());
	
		// 事件监听器
		buffer = new StringBuffer("事件监听器");
		/*List<String> listeners = taskDefinition.getBpmnxListeners();
		for (String id : listeners) {
			buffer.append(id + " ");
		}*/
		System.out.println(buffer);
	}
 	
 	@Test
	public void deleteDeployment() {
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery()
				.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId());

			processEngine.getRepositoryService().deleteDeploymentCascade(deployment.getId());
		    //repositoryService.deleteDeployment(deployment.getId(), false);
		}
 		
 		/*processEngine.getRepositoryService().deleteDeployment("1130001",true);*/
	}
 	
 	/**
 	 * 删除流程实例
 	 */
 	@Test
 	public void deleteProcessInstance(){
 	   runtimeService.deleteProcessInstance("542501", "delete");
 	   histroyService.deleteHistoricProcessInstance("542501");
 		
 	}
 	
 	
 	@Test
 	public void jsonTest(){
 		Participator p1=new Participator("u01", "小明", IdentityEnum.USER, null);
 		Participator p2=new Participator("u02", "小雨", IdentityEnum.USER, null);
 		List<String> strList=new ArrayList<String>();
 		strList.add(JSONObject.toJSONString(p1));
 		strList.add(JSONObject.toJSONString(p2));
 		System.out.println("-----type1");
 		System.out.println(JSONObject.toJSONString(strList));
 		
 		List<Participator> pList=new ArrayList<Participator>();
 		pList.add(p1);
 		pList.add(p2);
 		System.out.println("-------type2");
 		System.out.println(JSONObject.toJSONString(pList));
 		
 		
 	}
 	
 	@Test
 	public void processStart() throws InterruptedException{
 		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream("com/finance/finance3.bpmn");
		String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
		//ProcessDefinition processDefinition=bpmnxTool.deployWithString(result, "04",false);
	
		//无法启动
		try{
			runtimeService
			.createProcessInstanceBuilder()
			.processDefinitionKey("finance")
			.businessKey("business_01")
			.processStarter("u05")
			.processInstanceName("报销审批_XXX").start();
		}catch(Exception e){
			logger.error(e.toString());
		}
		 
		
		
		Thread.sleep(60000);
		
		
		 inputStream =this.getClass().getClassLoader().getResourceAsStream("com/technology/finance5.bpmn");
		 result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
	     processDefinition=bpmnxTool.deployWithString(result, "05",false);
		 
		Thread.sleep(60000);
		

		//可以启动
		 runtimeService
			.createProcessInstanceBuilder()
			.processDefinitionKey("finance")
			.businessKey("business_01")
			.processStarter("u05")
			.processInstanceName("报销审批_XXX").start();
	
		 List<HistoricTaskInstance> list=	 histroyService.createHistoricTaskInstanceQuery()
		 .processInstanceId("processInstanceId")
		 .taskValid()
		 .finished()
		 .list();
		
		 HistoricTaskInstance historicTaskInstance=list.get(0);
		 historicTaskInstance.getTaskDefinitionKey();
		 historicTaskInstance.getName();
 	}
 	
 	@Test
 	public void variableTest(){
 		List<ProcessInstance> list=	runtimeService
 				                .createProcessInstanceQuery()
 				                .terminator("u01")
 				                .terminalActivityImplId("A")
 				                .withTerminalInfo()
 				                .processDefinitionKey("Process_1")
 				                .list();
 		for (ProcessInstance processInstance : list) {
 			ExecutionEntity executionEntity=(ExecutionEntity)processInstance;
 			System.out.print(executionEntity.getTerminator()+"   ");
 			for (ActivityEntity ActivityEntity : executionEntity.getTerminalActivityImplList()) {
				System.out.print(ActivityEntity.getActivityName()+"   ");
			}
 			
			System.out.println(processInstance.getProcessInstanceId());
		}
 		
 		List<Execution> list=	runtimeService.createExecutionQuery().processDefinitionKey("Process_1").list();
 		for (Execution execution : list) {
			System.out.println(execution.getId());
		}
 	}
 
 	@Test
 	public void termination(){
 		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("processDefinitionId");
 				
 		
 		TaskDefinition taskDefinition = processDefinitionEntity.getTaskDefinitions().get("taskDefinitionKey");
		if(taskDefinition!=null){
		    Boolean isAutoComplete=taskDefinition.getIsAutoComplet();
		    System.out.println(isAutoComplete);
		}
		
		repositoryService.createProcessDefinitionQuery().list();
 	}
 
 	/**
 	 * 根据流程定义获取运行轨迹
 	 */
 	@Test
 	public void getRunTrail(){
 		List<RunTrail>  result= runtimeService.getProcessRunTrail("260008");
 		StringBuffer buffer=new StringBuffer();
 		for (RunTrail runTrail : result) {
 			buffer.append(runTrail.getSeqId()+" , ");
		}
 		
 		System.out.println(buffer);
 	}
 	
 	/**
 	 * 获取某个节点的可回退节点
 	 */
 	@Test
 	public void getAllowableBackActivities(){
 		List<ActivityEntity> list=	runtimeService.getAllowableBackActivities("265062", "F");
 		StringBuffer buffer=new StringBuffer();
 		for (ActivityEntity activityEntity : list) {
			buffer.append(activityEntity.getActivityId()).append(", ");
		}
 		
 		System.out.println(buffer.toString());
 	}
 	
 	@Test
 	public void runActivie(){
 		/*ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("Process_1:1:1250305");
 		ActivityImpl  act= processDefinitionEntity.findActivity("I");
 		//ActivityImpl  act= processDefinitionEntity.findActivity("StartEvent_0iu627r");
 		System.out.println(act.getParent());
 		System.out.println(act.getParentActivity());
 		System.out.println(act);*/
 		
 		String processDefinitionId="Process_1:1:1250305";
 		
 		BpmnModel  model=	repositoryService.getBpmnModel(processDefinitionId);
 		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
 		System.out.println(model);
 		System.out.println(processDefinitionEntity);
 		
 		repositoryService.deleteDeployment("1255001", true);
 		

 		BpmnModel  model2=	repositoryService.getBpmnModel(processDefinitionId);
 		//repositoryService.deleteDeployment("", cascade);
 		System.out.println(model2);
 		
 		ProcessDefinitionEntity processDefinitionEntity2 = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
 		System.out.println(processDefinitionEntity2);
 		
 		
 		
 	}
 	
 	@Test
 	public void endManualProcess(){
 		runtimeService.endManualProcess("265062");
 	}
 	
 	@Test
 	public void completeTask2(){
 	 // System.out.println("删除前："+repositoryService.getBpmnModel("Process_1:1:287504"));
 		/*ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("Process_1:1:750006");*/
 		
 		 /*((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("Process_1:1:750006");*/

 		
 		/*ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("Process_1:1:897510");*/
 		
 		//runtimeService.deleteProcessInstance("932501", "delete");
 		
 		//runtimeService.terminateProcess("40001");
 		
 		//获取嵌套子流程中的元素
 		ProcessDefinitionEntity	result=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("Process_sub:1:85010");
 		ActivityImpl activityImpl=result.findActivity("1");
 		System.out.println(result);
 		System.out.println(activityImpl.getParentActivity());
 	}
}
