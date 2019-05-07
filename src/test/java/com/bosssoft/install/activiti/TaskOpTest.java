package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;

import org.activiti.bpmn.model.Activity;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.consign.ConsignInstState;
import org.activiti.engine.consign.ConsignInstType;
import org.activiti.engine.consign.ConsignTask;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricConsignTaskQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskExtQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.TaskDetail;
import org.activiti.engine.impl.TaskExt;
import org.activiti.engine.impl.authority.AuthorityManager;
import org.activiti.engine.impl.bpmn.helper.ConsignInstLinkHelper;
import org.activiti.engine.impl.bpmn.helper.ConsignRankMate;
import org.activiti.engine.impl.bpmn.parser.factory.ParticipatorRuleParserFactory;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.ConsignInstEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ReverseRunTrailEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.rollback.BatchCachedUtil;
import org.activiti.engine.impl.rollback.BatchContext;
import org.activiti.engine.impl.runtime.Action;
import org.activiti.engine.impl.runtime.OperationType;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.task.TaskQueryAttribute;
import org.activiti.engine.impl.util.json.JSONTokener;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.parse.ParticipatorRuleParser;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.task.custom.WhereCondition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.ConsignInst;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskExtQuery;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bosssoft.platform.common.classloader.RuntimeClassLoaderHelper;
import com.bosssoft.platform.common.extension.ExtensionLoader;

import groovy.util.OrderBy;


public class TaskOpTest extends BaseTest{
	
	// 待办任务查询
	@Test
	public void unfinishTaskSearch() {
		String userId="u01";
		String userRole="r01";
		List<TaskExt> list=new ArrayList<TaskExt>();
		TaskExtQuery taskExtQuery =taskService.createTaskExtQuery();
		
		
		
		System.out.println("--------------------自己的-------");
		long num = taskExtQuery
				//.businessKey(userId)
			//	.ownerTaskAssigneeOrCandidate(userId)
				.taskAssigneeOrCandidate(userId)
				//.taskCreatedBefore(getDate("2018-12-28 15:30:37.971"))
				//.taskCreatedAfter(getDate("2018-12-27 01:20:37.971"))
				.count();
		System.out.println("数量："+num);
		list=taskExtQuery.orderByTaskCreateTime().desc().list();
		for (TaskExt taskExt : list) {
			System.out.println(taskExt.getBusinessKey()+"  操作状态："+taskExt.getOperationFlag()+"  "+taskExt.getTaskDefKey()+"  "+taskExt.getStarterName()+" 任务名称： "+taskExt.getTaskDefKey()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState()+" 时间："+taskExt.getTaskCreateTime()+"  结束时间："+taskExt.getTaskEndTime()+"  中断状态："+taskExt.getSuspensionState());
		}
		
		
		
	}
	
	//委托任务查询
	@Test
	public void consignTaskSearch(){
		List<ConsignTask> list=new ArrayList<ConsignTask>();
		String userId="u02";
		/*System.out.println("--------------------代理的-------");
		List<ConsignTask> list=histroyService.createHistoricConsignTaskQuery().consignor(userId).delegate().unfinish().list();
    	for (ConsignTask consignTask : list) {
			System.out.println("任务名称： "+consignTask.getTaskDefKey()+"任务Id："+consignTask.getTaskId()+" 任务办理人： "+consignTask.getAssigneeId()+" 流程名称："+consignTask.getProcessName()+" 委托实例Id: "+consignTask.getConsigInstId()
			+" 业务状态： "+consignTask.getBusinessState()+" 任务状态： "+consignTask.getConsigState());
		}
    	
    	
    	System.out.println("--------------------代办的-------");
		 list=histroyService.createHistoricConsignTaskQuery().consignor(userId).substitution().list();
    	for (ConsignTask consignTask : list) {
			System.out.println("任务名称： "+consignTask.getTaskDefKey()+"任务Id："+consignTask.getTaskId()+" 任务办理人： "+consignTask.getAssigneeId()+" 流程名称："+consignTask.getProcessName()+" 委托实例Id: "+consignTask.getConsigInstId()
			+" 业务状态： "+consignTask.getBusinessState()+" 任务状态： "+consignTask.getConsigState());
		}
    	
    	System.out.println("--------------------协办的-------");
		 list=histroyService.createHistoricConsignTaskQuery().consignor(userId).cooperation().list();
     	for (ConsignTask consignTask : list) {
			System.out.println("任务名称： "+consignTask.getTaskDefKey()+"任务Id："+consignTask.getTaskId()+" 任务办理人： "+consignTask.getAssigneeId()+" 流程名称："+consignTask.getProcessName()+" 委托实例Id: "+consignTask.getConsigInstId()
			+" 业务状态： "+consignTask.getBusinessState()+" 任务状态： "+consignTask.getConsigState());
		}*/
     	
     	System.out.println("--------------------全部的-------");
     	HistoricConsignTaskQuery  historicConsignTaskQuery=histroyService.createHistoricConsignTaskQuery();
     	
     	
		 long num=historicConsignTaskQuery
				 .consignor(userId)
				 .businessKey(userId)
				 .taskCreatedBefore(getDate("2018-12-27 10:26:10.137"))
				 .taskCreatedAfter(getDate("2018-12-27  08:26:10.137"))
	             .taskCompletedBefore(getDate("2018-12-27 12:00:23.990"))
	             .taskCompletedAfter(getDate("2018-12-27 08:36:23.990"))
				 .count();
		 System.out.println("数量："+num);
		 
		list= historicConsignTaskQuery.orderByTaskCreateTime().desc().list();

    	for (ConsignTask consignTask : list) {
			System.out.println("任务名称： "+consignTask.getTaskDefKey()+"任务Id："+consignTask.getTaskId()+" 任务办理人： "+consignTask.getAssigneeId()+" 流程名称："+consignTask.getProcessName()+" 委托实例Id: "+consignTask.getConsigInstId()
			+" 业务状态： "+consignTask.getBusinessState()+" 任务状态： "+consignTask.getConsigState()+" "+consignTask.getTaskCreateTime()+"  结束时间："+consignTask.getTaskEndTime()+"  中断状态："+consignTask.getSuspensionState());
		}
    	
	}
	
	//已完成任务查询
	@Test
	public void finishTaskSearch(){
		List<String> processDefIds=new ArrayList<String>();
    	
    	String userId="u01";
    	HistoricTaskExtQuery historicTaskExtQuery=histroyService.createHistoricTaskExtQuery();
    	
    	System.out.println("-----------自己-----------");
    long num=historicTaskExtQuery
             .completeOwnerTaskAssignee(userId)
             .taskCreatedBefore(getDate("2018-12-28 15:26:10.137"))
			 .taskCreatedAfter(getDate("2018-12-27  08:26:10.137"))
             .taskCompletedBefore(getDate("2018-12-27 12:00:23.990"))
             .taskCompletedAfter(getDate("2018-12-27 08:36:23.990"))
             .count();
    
    System.out.println("数量："+num);
  
    	List<TaskExt> list=historicTaskExtQuery
    	.orderByTaskCreateTime()
    	.desc()
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("操作状态："+taskExt.getOperationFlag()+"  "+"任务名称： "+taskExt.getTaskName()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState()+"  "+taskExt.getTaskCreateTime()+"  结束时间："+taskExt.getTaskEndTime()+" "+taskExt.getOperationFlag()+"  中断状态："+taskExt.getSuspensionState());
		}
    
	}
	
	
	
	@Test
	public void complet() throws InterruptedException{
		Map<String,Object> map=new HashMap<String, Object>();
	    map.put(ActVariable.ACT_COUNTERSIGNATURE_RESULT, true);
		//map.put("muti_var", null);
		//taskService.complete("1065035");
		long t1 = System.currentTimeMillis();
		taskService.complete("1027504",map);
		//taskService.complete("1022522",map);
		
		long t2 = System.currentTimeMillis();
	//	System.out.println("执行时间："+(t2-t1));
		//taskService.complete("532507",map);
	    //taskService.complete("1105040",map);
		//Context.getProcessEngineConfiguration().getActivityBehaviorFactory()
	}
	
	@Test
	public void redo(){
		//runtimeService.suspendProcessInstanceById("1405013");
		//runtimeService.activateProcessInstanceById("1405013");
	   runtimeService.terminateProcess("1072504");
	}
	
	@Test
	public void unclaim(){
		taskService.unclaimCascade("2255007", "2255014");
	}
	
	@Test
	public void taskDetail(){
		TaskDetail taskdetail=	taskService.getTaskDetail("317510", "317512");
		System.out.println(taskdetail);
	}
	
	@Test
	public void taskComplet(){
		Map<String,Object> map=new HashMap<String, Object>();
	     map.put(ActVariable.ACT_COUNTERSIGNATURE_RESULT, false);
	     
	     Participator p=new Participator("u04", "张三", IdentityEnum.USER, null);
	     Participator p2=new Participator("u01", "小明", IdentityEnum.USER, null);
	     taskService.addComment("750045", "750040", "同意");
	     taskService.completeInformCascade("750045", null, map, "我完成了", p,p2);
	   /*  taskService.complete("2087527", map);*/
	     
	    
	}
	
	//待办任务的自定义查询
	@Test
	public void runTaskCustom(){
		List<Object> list=new ArrayList<Object>();
		list.add("BB");
		list.add("CC");
		list.add("AA");
		
		WhereCondition whereCondition=new WhereCondition(" TASKEXT.ID_=? and BUSINESS_INFO.BS_NAME_ in ?");
		whereCondition.setString(1, "547510");
		whereCondition.setList(2, list);
		
	List<Map<String,Object>> result=taskService.createCustomTaskExtQuery()
		                                       .taskAssigneeOrCandidate("u02")
		                                      .selectColumn("BUSINESS_INFO.BS_NAME_ as BUSINESS_INFO_NAME_ ","TASKEXT.HI_BUSINESS_KEY_ as buKey","USER_INFO_.USER_ID_ as UID1")
				                                
			                                   .leftJoin("BUSINESS_INFO", "BS_ID_", "TASKEXT", "HI_BUSINESS_KEY_")
		                                       .leftJoin("USER_INFO_", "USER_ID_", "BUSINESS_INFO", "BS_USER_ID_")
		                                       .orderBy("BS_NAME_ asc","TASKEXT.ID_ asc")
		                                       .where(whereCondition)
		                                      
		                                       .listPage(0, 10);
	
	System.out.println("----------------------------");
		for (Map<String, Object> map : result) {
			StringBuffer buffer=new StringBuffer();
			for (Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey()+": "+entry.getValue()+"; ");
			}
			System.out.println(buffer.toString());
		}
	}
	
	@Test
	public void then(){
		taskService.createTaskQuery().processInstanceBusinessKey(processInstanceBusinessKey)
		

	}
	
	//已办任务的自定义查询
	@Test
	public void hisCustom(){

		List<Object> list=new ArrayList<Object>();
		list.add("BB");
		list.add("CC");
		list.add("AA");
		
		WhereCondition whereCondition=new WhereCondition(" TASKEXT.ID_=? and BUSINESS_INFO.BS_NAME_ in ?");
		whereCondition.setString(1, "537514");
		whereCondition.setList(2, list);
		
	List<Map<String,Object>> result=histroyService.createCustomHistoricTaskExtQuery()
		                                      .completetaskAssignee("u01")
		                                      
		                                       .selectColumn("BUSINESS_INFO.BS_NAME_ as BUSINESS_INFO_NAME_ ","TASKEXT.HI_BUSINESS_KEY_","USER_INFO_.USER_ID_ as UID1")
				                                
			                                   .leftJoin("BUSINESS_INFO", "BS_ID_", "TASKEXT", "HI_BUSINESS_KEY_")
		                                       .leftJoin("USER_INFO_", "USER_ID_", "BUSINESS_INFO", "BS_USER_ID_")
		                                       .orderBy("BS_NAME_ asc","TASKEXT.ID_ asc")
		                                       .where(whereCondition)
		                                       .listPage(0, 5);
	
	System.out.println("----------------------------");
		for (Map<String, Object> map : result) {
			StringBuffer buffer=new StringBuffer();
			for (Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey()+": "+entry.getValue()+"; ");
			}
			System.out.println(buffer.toString());
		}
	}
	
	//委托任务的自定义查询
	@Test
	public void consignCustom(){
		List<Object> list=new ArrayList<Object>();
		list.add("BB");
		list.add("CC");
		list.add("AA");
		
		WhereCondition whereCondition=new WhereCondition(" TASKEXT.ID_=? and BUSINESS_INFO.BS_NAME_ in ?");
		whereCondition.setString(1, "537514");
		whereCondition.setList(2, list);
		
	List<Map<String,Object>> result=histroyService.createCustomHistoricConsignTaskQuery()
			                                  .consignor("u02")
		                                       .selectColumn("BUSINESS_INFO.BS_NAME_ as BUSINESS_INFO_NAME_ ","TASKEXT.HI_BUSINESS_KEY_","USER_INFO_.USER_ID_ as UID1")
			                                   
			                                   .leftJoin("BUSINESS_INFO", "BS_ID_", "TASKEXT", "HI_BUSINESS_KEY_")
		                                       .leftJoin("USER_INFO_", "USER_ID_", "BUSINESS_INFO", "BS_USER_ID_")
		                                       .orderBy("BS_NAME_ desc","TASKEXT.ID_ asc")
		                                       .where(whereCondition)
		                                       .listPage(0, 5);
	
	System.out.println("----------------------------");
		for (Map<String, Object> map : result) {
			StringBuffer buffer=new StringBuffer();
			for (Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey()+": "+entry.getValue()+"; ");
			}
			System.out.println(buffer.toString());
		}
	}
	
	
	@Test
	public void runTrailSearch(){
		
	  taskService.createTaskQuery().list();//返回激活和挂起的任务
	  taskService.createTaskQuery().active().list();//返回激活的任务
	  taskService.createTaskQuery().suspended().list();//返回挂起的任务
	  taskService.createTaskQuery().termination().list();//返回终止的任务
	  
	  histroyService.createHistoricTaskInstanceQuery().termination().list();
	}
	
	@Test
	public void comment(){
		/*List<Task> list=	taskService.createTaskQuery()
				           // .termination()
				            // .active()
				             .list();
		for (Task task : list) {
			System.out.println(task.getTaskDefinitionKey());
		}*/
		
		List<HistoricTaskInstance> list=	histroyService.createHistoricTaskInstanceQuery()
		.termination()
		//.active()
		.list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getTaskDefinitionKey());
		}
		
		
	}

	@Test
	public void taskWithBusinessKey(){
	   /*taskService.setVariableLocal("902513", "var1-1", "testA");
	   taskService.setVariableLocal("902513", "var1-2", "varTest");
	   
	   
	   taskService.setVariableLocal("902522", "var2-1", "testA");
	   taskService.setVariableLocal("902522", "var2-2", "varTest");
	   
	   
	   taskService.setVariableLocal("902531", "var3-1", "testA");
	   taskService.setVariableLocal("902531", "var3-2", "varTest");*/
	   //taskService.unclaim("732502");
		/*runtimeService.setVariable("902508", "var1", "var1");
		runtimeService.setVariable("902517", "var2", "var2");
		runtimeService.setVariable("902526", "var3", "var3");*/
		long t1 = System.currentTimeMillis();
		taskService.complete("1020010");
		long t2 = System.currentTimeMillis();
		System.out.println("insertCommit执行时间："+(t2-t1));
	}
	
	//完成任务回滚
	@Test
	public void hisTaskQuery(){
		
		//将批次号保存为线程变量
	     BatchContext.setBatchNo("1001");
	    try{
	    	//完成任务
	    	taskService.complete("985085");
	    }catch(Exception e){
	    	//发生异常，根据批次号回滚
	    	taskService.rollbackBatchTask("1001");
	    }
	    
	}
	

	@Test
	public void subProcessTest(){
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition("Process_sub:1:1137599");
		
		List<ActivityImpl> list=processDefinitionEntity.getActivities();
		for (ActivityImpl activityImpl : list) {
			if(activityImpl.getId().equals("1")){
				System.out.println(activityImpl);
			}
		}
		
	}
	
	@Test
	public void json(){
		Participator p=new Participator("u01", "name", IdentityEnum.USER, null);
		String jsonStr=JSON.toJSON(p).toString();
		System.out.println(jsonStr);
		
		Participator p1=new Participator("u01", "name", IdentityEnum.USER, null);
		Participator p2=new Participator("u02", "name_two", IdentityEnum.USER, null);
		List<Participator> list=new ArrayList<Participator>();
		list.add(p1);
		list.add(p2);
		String jsonListStr=JSON.toJSON(list).toString();
		System.out.println(jsonListStr);
		//test
		
		String participatorJsonStr = JSON.toJSONString(list);
		System.out.println("131: "+participatorJsonStr);

		Map<String,Object> map=new HashMap<String, Object>();
		
		
	}
	
	
	@Test
	public void zookeeperTest1(){
		repositoryService.deleteDeployment("4", true);
	}
	
	@Test
	public void zookeeperTest2() throws InterruptedException{
		ProcessDefinitionEntity	result=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("Process_1:1:1250305");
		
		Thread.sleep(30000);
		
		 ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("Process_1:1:1250305");
		 
		 /*Context.getProcessEngineConfiguration().getZkPublishSubscribeManager()*/
	}
	
	
	@Test
	public void endTest(){
		///runtimeService.endProcess("30008");
		
		//runtimeService.setVariable("30008", "var_name", "test");
		//Context.getCommandContext().getTaskEntityManager()
	//	repositoryService.saveCategory(null, "test", null, false);
		//taskService.complete("622509");
	    /*List<Task> list=	taskService.createTaskQuery().list();
	    for (Task task : list) {
			System.out.println(task.getId());
		}*/
		//taskService.complete("560003");
		List<String> list=new ArrayList<String>();
		list.add("01");
		list.add("02");
		String objstr=JSON.toJSONString(list);
		System.out.println(objstr.getBytes());
		taskService.withdrawByStarter(processInstId);
		
		List<String> result=JSONObject.parseArray(objstr,  String.class);
		System.out.println(result);
	}
	
	
	@Test
	public void bacthComplete(){
		
		//ProcessDefinitionEntity result=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("Process_1:1:897510");
		//result.getActivities()
		
		//taskService.complete("900007");
		List<String> processDefinitionKeyList=new ArrayList<String>();
		processDefinitionKeyList.add("Process_2");
		
		List<Task> list=	taskService.createTaskQuery()
		.processInstanceBusinessKey("u01")
		.taskDefinitionKey("A")
		.processDefinitionKeyIn(processDefinitionKeyList)
		.orderByTaskCreateTime()
		.desc()
		.list();
		
		/*for (Task task : list) {
			System.out.println(task.getId()+"  "+task.getProcessDefinitionKey());
		}*/
		
		
		/*List<HistoricTaskInstance> list=	histroyService.createHistoricTaskInstanceQuery()
		.processInstanceBusinessKey("u01")
		.processDefinitionKey("Process_1")
		.list();
	   
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getId()+"  "+historicTaskInstance.getProcessDefinitionKey());
		}*/
		
		
		//histroyService.createHistoricTaskInstanceQuery()
      
	}
	
	@Test
	public void testProcessdef(){
		//repositoryService.createModelQuery().modelKey("Process_1").list();
		
		histroyService
		.createHistoricTaskInstanceQuery()
		.processInstanceId("")
		.taskDefinitionKey("")
		.unfinished()
		.orderByHistoricTaskInstanceEndTime()
		.processDefinitionName("")
		.desc()
		.count();
		
		
		histroyService

	}
	
	
	

	
}
