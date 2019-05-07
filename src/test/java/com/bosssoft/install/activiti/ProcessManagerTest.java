package com.bosssoft.install.activiti;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ActivityImplConstants;
import org.activiti.engine.FormService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.apache.commons.collections.map.HashedMap;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.helper.TaskFlowUtils;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ActivityEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.DateConventUtil;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.repository.BizComment;
import org.activiti.engine.repository.BusinessComment;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.bosssoft.platform.common.utils.DateUtils;

public class ProcessManagerTest extends BaseTest {

	
	
	
	@Test
	public void getfile() throws IOException{
		
		//根据类别获取流程列表
		List<ProcessDefinition> result=repositoryService.createProcessDefinitionQuery()
				                        .withDeployedProcessDefinition()
				                        .allowStartByUser("u06")
				                        .processDefinitionCategory("04").list();
		
		repositoryService.saveModelAndUpdateProcessDefinition(modelId, processDefinitionStr, categoryId);
		
		//获取流程定义文件的内容
		for (ProcessDefinition processDefinition : result) {
			
			//流程定义的key
			System.out.println(processDefinition.getId());
			
			InputStream inputStream= repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
			
			byte[] bytes = new byte[0];
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			String str = new String(bytes);
			System.out.println(str);
		}
	}
	
	/**
	 * 修改流程类别
	 */
	public void editeProcessCategory(){
		repositoryService.setProcessDefinitionCategory("", "");
		
	}
	
	/**
	 * 修改流程定义的名称
	 */
	@Test
	public void editeProcessName(){
		repositoryService.setProcessDefinitionName("finance:1:1522504", "修改");
		
	}
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteProcess(){
		repositoryService.deleteProcessDefinition("finance:1:67504", true);
	}
	
	
	@Test
	public void editeProcessDefinition() throws Exception{
		InputStream inputStream = new FileInputStream("D:/test/import/contract.bpmn");
		byte[] bytes = new byte[0];
		bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		String str = new String(bytes);
		repositoryService.saveModelAndUpdateProcessDefinition("10060", str);
	}
	
	
	
	private   String getString(String classLoadPath){
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream(classLoadPath);
		String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
		return result;
	}
	
	/*@Test
	public void deployWithZip() {
		InputStream inputStream = this.getClass() // 获取当前class对象
				.getClassLoader() // 获取类加载器
				.getResourceAsStream("all.zip"); // 获取指定文件资源流
		ZipInputStream zipInputStream = new ZipInputStream(inputStream); // 实例化zip输入流对象
		// 获取部署对象
		Deployment deployment = repositoryService // 部署Service
				.createDeployment() // 创建部署
				.name("all流程") // 流程名称
				.addZipInputStream(zipInputStream) // 添加zip是输入流
				.deploy(); // 部署
		System.out.println("流程部署ID:" + deployment.getId());
		System.out.println("流程部署Name:" + deployment.getName());

	}*/
	
	@Test
	public void deleteDeployment() {
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery()
				.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId());

			processEngine.getRepositoryService().deleteDeploymentCascade(deployment.getId());
			//repositoryService.deleteDeployment(deployment.getId(), false);
		}
	}
	
	@Test
	public void myProcess() throws ParseException{
	
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		//查询由xx发起的流程
		List<HistoricProcessInstance> list=histroyService.createHistoricProcessInstanceQuery()
		             //.startedBy("u01")
		              //.processDefinitionCategory("04")//类别
		             //.finished()//已经完成
		             // .unfinished()//未完成
		              //.processDefinitionName("")//流程名称
		             // .startedAfter(DateConventUtil.strToDate("2018-08-21", "yyyy-MM-dd"))
		              .orderByProcessInstanceStartTime()
		              .desc()
		              .asc()
		              //.withActInfo()
		              .list();
		
		
		
		/*List<HistoricProcessInstance> list=histroyService.createHistoricProcessInstanceQuery()
	            //  .startedBy("u01")
	              //.processDefinitionCategory("04")//类别
	             // .finished()//已经完成
	              //.unfinished()//未完成
	              //.processDefinitionName("")//流程名称
	              //.variableValueEquals(value)
	              .withActInfo()
				.variableValueEquals("loopCounter", 2)
	              .list();*/
		
		
		for (HistoricProcessInstance historicProcessInstance : list) {
			//HistoricProcessInstanceEntity
			System.out.println(historicProcessInstance.getId());
		   for (ActivityEntity activityEntity : historicProcessInstance.getCurrentActivities()) {
			 System.out.println(activityEntity.getActivityId()+"   name: "+activityEntity.getActivityName());
		}
		}
		//System.out.println(histroyService.createHistoricProcessInstanceQuery().count());
      
		
		List<HistoricTaskInstance> hislist=histroyService.createHistoricTaskInstanceQuery()
		             .processInstanceId("processInstanceId")
		             .finished()
		             .taskValid()
		             .list();
		
		for (HistoricTaskInstance historicTaskInstance : hislist) {
			System.out.println("key: "+historicTaskInstance.getTaskDefinitionKey()
			+" name: "+historicTaskInstance.getName());
		}
	}

	@Test
    public void processStateTest(){    	
    	//runtimeService.suspendProcessInstanceById("385023");
		List<ProcessDefinition> result=new ArrayList<ProcessDefinition>();
		
		
		List<ProcessDefinition> list=repositoryService.createProcessDefinitionQuery().latestVersion().list();
		for (ProcessDefinition processDefinition : list) {
		Model model=	repositoryService.createModelQuery()
			.processDefinitionId(processDefinition.getId())
			.singleResult();
		
		
		if(model.isDefaultProcess()){
			result.add(processDefinition);
		}else{
			//查询对应的默认流程
			Model defaultModel=	repositoryService
			.createModelQuery()
			.modelKey(processDefinition.getKey())
			.deployed()
			.defaultProcess().singleResult();
			if(defaultModel==null){
				result.add(processDefinition);
			}else{
				result.add(repositoryService.getProcessDefinition(defaultModel.getProcessDefinitionId()));
			}
		}
			
     }
     for (ProcessDefinition processDefinition : result) {
		System.out.println(processDefinition.getName());
	}
		
    }
	
	@Test
	public void processInstSearch(){
		List<ProcessInstance> list=new ArrayList<ProcessInstance>();
		/*list= runtimeService.createProcessInstanceQuery().active().list();
		System.out.println("--------活动的流程--");
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId());
		}
		
		
		list= runtimeService.createProcessInstanceQuery().suspended().list();
		System.out.println("--------挂起的流程--");
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId());
		}
		
		list= runtimeService.createProcessInstanceQuery().termination().list();
		System.out.println("--------终止的--");
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId());
			
		}*/
		
		 //runtimeService.activateProcessInstanceById("117501");
	//	runtimeService.terminateProcess("117501", "reason", "u01");
		
		//runtimeService.suspendProcessInstanceById("120001");
		
		/*List<Execution> exlist=runtimeService.createExecutionQuery().list();
		
		
		List<HistoricProcessInstance> hisList=histroyService.createHistoricProcessInstanceQuery().startedBy("u06").list();
	    for (HistoricProcessInstance historicProcessInstance : hisList) {
			System.out.println(historicProcessInstance.getSuspensionState()+"  "+historicProcessInstance.getId());
		}*/
		
		histroyService.createHistoricProcessInstanceQuery().processDefinitionName(processDefinitionName)
	}

	@Test
	public void deploytest() throws InterruptedException{
		/*Map<String,Object> param=new HashedMap();
		param.put("process_starter", "u01");
		runtimeService.startProcessInstanceByIdWithBizKey("Process_2:1:30005", param,"business1", null);*/
		//runtimeService.startProcessInstanceByIdWithBizKey("Process_single:1:752511", param,null, "870001");
		
		
		runtimeService
		.createProcessInstanceBuilder()
		.processDefinitionKey("Process_3")
		.processStarter("u01")
		//.previousProcessInstId("35001")
		.processInstanceName("simple")
		.businessKey("sinple")
		.start();
		
		//Thread.sleep(10000000);
		
	}
	
	@Test
	public void completeBusinessTask() throws InterruptedException{
		taskService.complete("47503");
		Thread.sleep(10000000);
	}
	
	@Test
	public void getBusinessComment(){
		List<BizComment> list=	histroyService.getBizCommentsAfterTask("45007", "Process_3:1:30011", ActivityImplConstants.ACTIVITYIMPL_TYPE_USERTASK);
		for (BizComment businessComment : list) {
			StringBuffer buffer=new StringBuffer();
			//setStr(buffer, "BusinessFlag", businessComment.getBusinessFlag());
			//setStr(buffer, "BusinessInstId", businessComment.getBusinessInstId());
			setStr(buffer, "ProcDefId", businessComment.getProcDefId());
			setStr(buffer, "ActId", businessComment.getActId());
			setStr(buffer, "ActName", businessComment.getActName());
			setStr(buffer, "CompleteTime", businessComment.getCompleteTime());
			System.out.println(buffer.toString());
		}
	}
	
	private void setStr(StringBuffer stringBuffer,String propertyName,Object value){
		stringBuffer.append("  "+propertyName+"="+value);
	}
	
}
