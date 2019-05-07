package com.bosssoft.install.activiti;

import java.util.ArrayList;
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
import org.activiti.engine.history.CustomHistoricTaskExtQuery;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.TaskDetail;
import org.activiti.engine.impl.TaskExt;
import org.activiti.engine.impl.bpmn.helper.ConsignInstLinkHelper;
import org.activiti.engine.impl.bpmn.helper.ConsignRankMate;
import org.activiti.engine.impl.bpmn.parser.factory.ParticipatorRuleParserFactory;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.ConsignInstEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ReverseRunTrailEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.runtime.Action;
import org.activiti.engine.impl.runtime.OperationType;
import org.activiti.engine.impl.task.TaskQueryAttribute;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.parse.ParticipatorRuleParser;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.task.custom.HavingCondition;
import org.activiti.engine.spi.task.custom.SqlCondition;
import org.activiti.engine.spi.task.custom.WhereCondition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.ConsignInst;
import org.activiti.engine.task.CustomTaskExtQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import com.bosssoft.platform.common.classloader.RuntimeClassLoaderHelper;
import com.bosssoft.platform.common.extension.ExtensionLoader;

import groovy.util.OrderBy;

public class TaskQueryTest extends BaseTest {
	@Test
	public void edite(){
		/*taskService.setAssignee("812518", null);
		taskService.removeVariableLocal("812518", ActVariable.ACT_CONSIGN_CONSIGNLINK);
		taskService.removeVariableLocal("812518", ActVariable.ACT_CONSIGN_ISTRIGGER);*/
		//System.out.println(taskService.getVariableLocal("1617505", ActVariable.ACT_CONSIGN_CONSIGNLINK));
		//HistoricVariableInstanceEntity inst=(HistoricVariableInstanceEntity) histroyService.createHistoricVariableInstanceQuery().taskId("1617505").variableName(ActVariable.ACT_CONSIGN_CONSIGNLINK).singleResult();
		//String  byteId= inst.getByteArrayValueId();
		
		//System.out.println(inst);
		// System.out.println(histroyService.createHistoricVariableInstanceQuery().taskId("1595009").variableName(ActVariable.ACT_CONSIGN_CONSIGNLINK).singleResult().getValue());
		/*Object value = taskService.getVariableLocal("1840002", ActVariable.ACT_CONSIGN_CONSIGNLINK);
		System.out.println(value);
		LinkedList<ConsignRankMate>  link = (LinkedList<ConsignRankMate>) value;
		System.out.println(link);*/
		
		
		//taskService.claimCascade(taskId, consignInstId, userId);
		
		runtimeService.createExecutionQuery()
		.processInstanceId("123")
		.activityId("A")
		.list();
		
		List<HistoricProcessInstance> list= histroyService.createHistoricProcessInstanceQuery().list();
 
		histroyService.createHistoricTaskInstanceQuery().
		
	    for (Task task : taskList) {
            
		}
		
	   
	}

	@Test
	public void one() {
		List<ConsignInst> list = taskService.createConsignInstQuery().taskId("12").consignee("12").consignInstTypeIn(ConsignInstType.SUBSTITUTION, ConsignInstType.COOPERATION).list();

		/*
		 * TaskDetail taskDetail =taskService.getHisTaskDetail("37531", null);
		 * System.out.println(taskDetail.getStartTime());
		 */
		// taskService.createTaskExtQuery().ownerTaskAssigneeOrCandidate("u02").list();
		/*
		 * taskService.createTaskExtQuery().delegateTaskAssigneeOrCandidate(
		 * "u02").active() .list();
		 * 
		 * //taskService.completCascade(taskId, consignInstId);
		 * 
		 * //taskService.addCandidateGroup("2097520", "", groupType);
		 * //taskService.addUserIdentityLink("2097520", "r01",
		 * IdentityEnum.USER.toString());
		 * taskService.addCandidateGroup("2097520", "r01", IdentityEnum.ROLE);
		 */
	}

	// 委托链查询
	@Test
	public void consignLinkSearch() {
		/*List<String> list = ConsignInstLinkHelper.getAllConsignInstIdsInLink(taskService, "2827505");
		System.out.println(list);
		*/
		System.out.println(taskService.createTaskExtQuery().ownerTaskAssigneeOrCandidate("u01").count());
		
		taskService.completCascade(taskId, consignInstId, variables);
		
		List<ProcessInstance> list=	runtimeService.createProcessInstanceQuery().list();
		for (ProcessInstance processInstance : list) {
			processInstance.getActivityId()
		}
		
		
		
		
		
	}

	// 待办任务查询
	@Test
	public void unfinishTaskSearch() {
		String userId = "u01";
		String userRole = "r01";
		List<TaskExt> list = new ArrayList<TaskExt>();
		System.out.println("--------------------自己的-------");
		list = taskService.createTaskExtQuery().taskAssigneeOrCandidate(userId)

				// .delegateTaskAssigneeOrCandidate(userId)
				// .ownerTaskAssigneeOrCandidate(userId)

				// .businessKeyLike("%EEC2A8940E883BD841F6D%")

				// .processDefIds(processDefIds)
				.list();
		for (TaskExt taskExt : list) {
			System.out.println(taskExt.getBusinessKey() + "  操作状态：" + taskExt.getOperationFlag() + "  " + taskExt.getTaskDefKey() + "  " + taskExt.getStarterName() + " 任务名称： " + taskExt.getTaskDefKey()
					+ "任务Id：" + taskExt.getTaskId() + " 任务办理人： " + taskExt.getAssigneeId() + " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： " + taskExt.getConsigInstId() + " 业务状态 :"
					+ taskExt.getBusinessState() + " 任务状态：" + taskExt.getConsigState() + " 时间：" + taskExt.getTaskCreateTime());
		}

		long count = taskService.createTaskExtQuery()

				.ownerTaskAssigneeOrCandidate(userId)

				.count();
		System.out.println(count);

		/*
		 * System.out.println("--------------------代理的-------"); list =
		 * taskService.createTaskExtQuery().delegateTaskAssigneeOrCandidate(
		 * userId) // .processDefIds(processDefIds) .list(); for (TaskExt
		 * taskExt : list) {
		 * System.out.println("任务名称： "+taskExt.getTaskDefKey()+"任务Id：" +
		 * taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" +
		 * taskExt.getProcessName() + "  委托实例Id： " + taskExt.getConsigInstId() +
		 * " 业务状态 :" + taskExt.getBusinessState() + " 任务状态：" +
		 * taskExt.getConsigState()); }
		 * 
		 * System.out.println("--------------------代办的-------"); list =
		 * taskService.createTaskExtQuery().substitutionTaskAssignee(userId) //
		 * .processDefIds(processDefIds) .list(); for (TaskExt taskExt : list) {
		 * System.out.println("任务名称： "+taskExt.getTaskDefKey()+"任务Id：" +
		 * taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" +
		 * taskExt.getProcessName() + "  委托实例Id： " + taskExt.getConsigInstId() +
		 * " 业务状态 :" + taskExt.getBusinessState() + " 任务状态：" +
		 * taskExt.getConsigState()); }
		 * 
		 * System.out.println("--------------------协办的-------"); list =
		 * taskService.createTaskExtQuery().cooperationTaskAssignee(userId) //
		 * .processDefIds(processDefIds) .list(); for (TaskExt taskExt : list) {
		 * System.out.println("任务名称： "+taskExt.getTaskDefKey()+"任务Id：" +
		 * taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" +
		 * taskExt.getProcessName() + "  委托实例Id： " + taskExt.getConsigInstId() +
		 * " 业务状态 :" + taskExt.getBusinessState() + " 任务状态：" +
		 * taskExt.getConsigState()); }
		 * 
		 */
		/*
		 * System.out.println("--------------------全部-------"); list =
		 * taskService.createTaskExtQuery().taskAssigneeOrCandidate(userId).
		 * termination() // .processDefIds(processDefIds) .list(); for (TaskExt
		 * taskExt : list) {
		 * System.out.println("任务名称： "+taskExt.getTaskDefKey()+"任务Id：" +
		 * taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" +
		 * taskExt.getProcessName() + "  委托实例Id： " + taskExt.getConsigInstId() +
		 * " 业务状态 :" + taskExt.getBusinessState() + " 任务状态：" +
		 * taskExt.getConsigState()+" 时间："+taskExt.getStartTime()+" isover:"
		 * +taskExt.isArriveConsignLimit()); }
		 */

	}

	// 委托任务查询
	@Test
	public void consignTaskSearch(){
		List<ConsignTask> list=new ArrayList<ConsignTask>();
		String userId="u01";
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
		 list=histroyService.createHistoricConsignTaskQuery()
				 .consignor(userId)
				 .list();
    	for (ConsignTask consignTask : list) {
			System.out.println("任务名称： "+consignTask.getTaskDefKey()+"任务Id："+consignTask.getTaskId()+" 任务办理人： "+consignTask.getAssigneeId()+" 流程名称："+consignTask.getProcessName()+" 委托实例Id: "+consignTask.getConsigInstId()
			+" 业务状态： "+consignTask.getBusinessState()+" 任务状态： "+consignTask.getConsigState());
		}
    	
    	
    	histroyService
    	.createHistoricConsignTaskQuery()
    	.consignor(userId)
        .complete()
        
    	.list();
    	
    	List<HistoricTaskInstance> list1=histroyService.createHistoricTaskInstanceQuery().list();
    	for (HistoricTaskInstance historicTaskInstance : list1) {
			historicTaskInstance.getAssigneeName()
		}
    	
	}

	// 已完成任务查询
	@Test
	public void finishTaskSearch(){
		List<String> processDefIds=new ArrayList<String>();
    	processDefIds.add("leave:1:442504");
    	String userId="u01";
    	
    	
    	System.out.println("-----------自己-----------");
    	List<TaskExt> list=histroyService
    			          .createHistoricTaskExtQuery()
    			          .completeOwnerTaskAssignee(userId)
    			          .ext2NotEqual("待办")
    			         // .termination()
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("操作状态："+taskExt.getOperationFlag()+"  "+"任务名称： "+taskExt.getTaskName()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState());
		}
    	
    	/*System.out.println("-----------代理-----------");
    	 list=histroyService
    			          .createHistoricTaskExtQuery()
    			          .completeDelegateTaskAssignee(userId)
    			          //.processDefIds(processDefIds)
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("任务名称： "+taskExt.getTaskName()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState());
		}
    	
    	
    	System.out.println("-----------代办-----------");
    	list=histroyService
    			          .createHistoricTaskExtQuery()
    			          .completeSubstitutionTaskAssignee(userId)
    			          //.processDefIds(processDefIds)
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("任务名称： "+taskExt.getTaskName()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState());
		}
    	
    	
    	System.out.println("-----------协办-----------");
    	list=histroyService
    			          .createHistoricTaskExtQuery()
    			          .completeCooperationTaskAssignee(userId)
    			          //.processDefIds(processDefIds)
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("任务名称： "+taskExt.getTaskName()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState());
		}
    	
    	System.out.println("-----------全部-----------");
    	list=histroyService
    			          .createHistoricTaskExtQuery()
    			          .completetaskAssignee(userId)
    			        //  .businessKey("business")
    						.processInstanceId("710008")
    			         // .taskNameLike("%审核%")
    			          //.processDefIds(processDefIds)
    			         // .taskDefinitionKey("C")
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("任务名称： "+taskExt.getTaskName()+"任务Id：" + taskExt.getTaskId() +" 任务办理人： "+taskExt.getAssigneeId()+ " 流程名称：" + taskExt.getProcessName() + "  委托实例Id： "
					+ taskExt.getConsigInstId() + " 业务状态 :" + taskExt.getBusinessState() + " 任务状态："
					+ taskExt.getConsigState());
		}
    	*/
    
    	runtimeService.createProcessInstanceQuery()
    	
	}

	// 领取任务
	@Test
	public void claim(){
		//taskService.claimCascade("812505", "1085001", "BB", "BB_NAME");
		//taskService.claimCascade("157508", null, "u06");
		//taskService.claimCascade("812505", "1115006", "BB", "BB_NAME");
		//taskService.unclaimCascade("157508", "u06");
		
		taskService.completCascade("445009", null);
		
		taskService.createTaskQuery().operationType(operationType)
		taskService.addCommentWithUserId("445009", "445001", Action.COMPLETE_TASK, "完成", "u01");
		//taskService.claim("2412519", "TT");
		repositoryService.deleteProcessDefinition(processDefinitionId, cascade);
		
	}

	// 创建代办/协办
	@Test
	public void createConsignInst(){
		//代办
		taskService.createTaskSubstitutionInst("u02", "u04",IdentityEnum.USER, "382508", null);
	
		taskService.createTaskQuery().orderByTaskCreateTime().listPage(0, 1);
		
		taskService.createTaskQuery().querForVaricabla()
		
		
		
		//协办
	//  taskService.createTaskCooperationInst("AA", "AA_Name", "BB", "BB_NAME", IdentityEnum.USER, "3710005", null);
	 //taskService.createTaskCooperationInst("u06", "u01",IdentityEnum.USER, "2182507", null);
	}

	@Test
	public void acceptOrRejectTask() {
		// 接受（领取）
		// taskService.claimCascade("2142507", "2405001", "u06");

		// 拒绝
		taskService.rejectTask("2420001");
	}

	@Test
	public void withdraw() {
		taskService.withdrawConsign("1245013");
	}

	@Test
	public void complet() throws InterruptedException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ActVariable.ACT_COUNTERSIGNATURE_RESULT, true);
		taskService.completCascade("1257561", null);
		/*
		 * taskService.complete("1172594",map);
		 * taskService.complete("1172601",map);
		 * taskService.complete("1172608",map);
		 */
	}

	@Test
	public void redo(){
		taskService.createTaskQuery().active().list();
		taskService.createTaskQuery().
	}

	@Test
	public void unclaim() {
		taskService.unclaimCascade("2255007", "2255014");
	}

	@Test
	public void taskDetail() {
		TaskDetail taskdetail = taskService.getTaskDetail("317510", "317512");
		System.out.println(taskdetail);
	}

	@Test
	public void taskComplet() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ActVariable.ACT_COUNTERSIGNATURE_RESULT, false);

		Participator p = new Participator("u04", "张三", IdentityEnum.USER, null);
		Participator p2 = new Participator("u01", "小明", IdentityEnum.USER, null);
		taskService.addComment("750045", "750040", "同意");
		taskService.completeInformCascade("750045", null, map, "我完成了", p, p2);
		/* taskService.complete("2087527", map); */

	}

	// 待办任务的自定义查询
	@Test
	public void runTaskCustom() {
		List<Object> list = new ArrayList<Object>();
		list.add("18");
		//list.add(456);
		//list.add("AA");

		WhereCondition whereCondition = new WhereCondition("TASKEXT.ID_  in ? ");
		//whereCondition.setString(1, "2508");
		//whereCondition.setInteger(1, 123);
		 whereCondition.setList(1, list);

		HavingCondition havingCondition=new HavingCondition("count(TASKEXT.ID_)>= ?");
		havingCondition.setInteger(1, 1);
		
		CustomTaskExtQuery customTaskExtQuery = taskService
				.createCustomTaskExtQuery()
				.taskAssigneeOrCandidate("u01")
				.selectColumn("count(RES.TASK_DEF_KEY_) as num")
				.leftJoin("USER_INFO_", "USER_ID_", "TASKEXT", "ASSIGNEE_")
				.groupBy("TASKEXT.ID_,USER_ID_")
				.taskCreatedBefore(getDate("2018-12-28 11:50:37.971"))
				.taskCreatedAfter(getDate("2018-12-26 11:20:37.971"))
				.having(havingCondition)
				.orderBy("TASKEXT.ID_ asc")
				.where(whereCondition);

		System.out.println("数量： " + customTaskExtQuery.count());

		List<Map<String, Object>> result = customTaskExtQuery.listPage(0, 10);

		System.out.println("----------------------------");
		for (Map<String, Object> map : result) {
			StringBuffer buffer = new StringBuffer();
			for (Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey() + ": " + entry.getValue() + "; ");
			}
			System.out.println(buffer.toString());
		}
	}

	@Test
	public void then() {
		taskService.complete("82552");
	}

	// 已办任务的自定义查询
	@Test
	public void hisCustom(){

		List<Object> list=new ArrayList<Object>();
		list.add("BB");
		list.add("CC");
		list.add("AA");
		
		WhereCondition whereCondition=new WhereCondition(" TASKEXT.ID_=?");
		whereCondition.setString(1, "14");
		//whereCondition.setList(2, list);
		
		HavingCondition havingCondition=new HavingCondition("count(TASKEXT.ID_)>= ?");
		havingCondition.setInteger(1, 1);
		
		CustomHistoricTaskExtQuery customHistoricTaskExtQuery = histroyService
				.createCustomHistoricTaskExtQuery()
				.completetaskAssignee("u01")
				.having(havingCondition)
				.selectColumn("MAX(TASKEXT.ID_) as taskId")
				.taskCreatedBefore(getDate("2018-12-28 09:26:10.137"))
				 .taskCreatedAfter(getDate("2018-12-26  08:26:10.137"))
	            .taskCompletedBefore(getDate("2018-12-28 12:00:23.990"))
	             .taskCompletedAfter(getDate("2018-12-26 08:36:23.990"))
				
				
				// .leftJoin("BUSINESS_INFO", "BS_ID_", "TASKEXT",
				// "HI_BUSINESS_KEY_")
				.leftJoin("USER_INFO_", "USER_ID_", "TASKEXT", "ASSIGNEE_")
				.groupBy("TASKEXT.ID_", "USER_ID_")
				.orderBy("TASKEXT.ID_ asc")
				.where(whereCondition);
		
		System.out.println("数量： "+customHistoricTaskExtQuery.count());
		
	List<Map<String,Object>> result=customHistoricTaskExtQuery.listPage(0, 5);
	
	System.out.println("----------------------------");
		for (Map<String, Object> map : result) {
			StringBuffer buffer=new StringBuffer();
			for (Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey()+": "+entry.getValue()+"; ");
			}
			System.out.println(buffer.toString());
		}
	}

	// 委托任务的自定义查询
	@Test
	public void consignCustom() {
		List<Object> list = new ArrayList<Object>();
		list.add("BB");
		list.add("CC");
		list.add("AA");

		WhereCondition whereCondition = new WhereCondition(" TASKEXT.ID_=? and BUSINESS_INFO.BS_NAME_ in ?");
		whereCondition.setString(1, "537514");
		whereCondition.setList(2, list);
		
		HavingCondition havingCondition=new HavingCondition("count(TASKEXT.ID_)> ?");
		havingCondition.setInteger(1, 100);

		List<Map<String, Object>> result = histroyService.createCustomHistoricConsignTaskQuery().consignor("u02")
				.selectColumn("BUSINESS_INFO.BS_NAME_ as BUSINESS_INFO_NAME_ ", "TASKEXT.HI_BUSINESS_KEY_", "USER_INFO_.USER_ID_ as UID1")
                .groupBy("TASKEXT.ID_","BS_ID_")
				.leftJoin("BUSINESS_INFO", "BS_ID_", "TASKEXT", "HI_BUSINESS_KEY_").leftJoin("USER_INFO_", "USER_ID_", "BUSINESS_INFO", "BS_USER_ID_").orderBy("BS_NAME_ desc", "TASKEXT.ID_ asc")
				.having(havingCondition)
				.orderBy(column)
				.taskCreatedBefore(getDate("2018-12-28 09:26:10.137"))
				 .taskCreatedAfter(getDate("2018-12-26  08:26:10.137"))
	            .taskCompletedBefore(getDate("2018-12-28 12:00:23.990"))
	             .taskCompletedAfter(getDate("2018-12-26 08:36:23.990"))
				.where(whereCondition)
				.listPage(0, 5);

		System.out.println("----------------------------");
		for (Map<String, Object> map : result) {
			StringBuffer buffer = new StringBuffer();
			for (Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey() + ": " + entry.getValue() + "; ");
			}
			System.out.println(buffer.toString());
		}
	}

	@Test
	public void runTrailSearch() {

		taskService.createTaskQuery().list();// 返回激活和挂起的任务
		taskService.createTaskQuery().active().list();// 返回激活的任务
		taskService.createTaskQuery().suspended().list();// 返回挂起的任务
		taskService.createTaskQuery().termination().list();// 返回终止的任务

		histroyService.createHistoricTaskInstanceQuery().termination().list();
	}

	@Test
	public void comment() {
		/*
		 * List<Task> list= taskService.createTaskQuery() // .termination() //
		 * .active() .list(); for (Task task : list) {
		 * System.out.println(task.getTaskDefinitionKey()); }
		 */

		List<HistoricTaskInstance> list = histroyService.createHistoricTaskInstanceQuery().termination()
				// .active()
				.list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getTaskDefinitionKey());
		}

	}

	@Test
	public void taskWithBusinessKey() {
		TaskQuery query = taskService.createTaskQuery();
		System.out.println("数量: " + query.count());

		Map<String, Object> map = query.attributeForMaxCreateTime().attributeForMinCreateTime().queryAttribute();
		System.out.println(map.get(TaskQueryAttribute.MAX_CREATE_TIME));
		System.out.println(map.get(TaskQueryAttribute.MIN_CREATE_TIME));

		/*
		 * Map<String, Object> map=
		 * histroyService.createHistoricTaskInstanceQuery()
		 * .processInstanceId("960008") .attributeForMaxCreateTime()
		 * .attributeForMinCreateTime() .queryAttribute();
		 */

		/*
		 * System.out.println(map.get(TaskQueryAttribute.MAX_CREATE_TIME));
		 * System.out.println(map.get(TaskQueryAttribute.MIN_CREATE_TIME));
		 */
	}

	@Test
	public void hisTaskQuery() {

		/*
		 * List<HistoricTaskInstance> list=histroyService
		 * .createHistoricTaskInstanceQuery() .ext1NotEqual("已办") .taskValid()
		 * .list(); for (HistoricTaskInstance historicTaskInstance : list) {
		 * System.out.println(historicTaskInstance.getId()); }
		 */

		/*
		 * List<HistoricProcessInstance> list=
		 * histroyService.createHistoricProcessInstanceQuery().listPage(0, 100);
		 * for (HistoricProcessInstance historicProcessInstance : list) {
		 * runtimeService.deleteProcessInstance(historicProcessInstance.getId(),
		 * "noreasa");
		 * histroyService.deleteHistoricProcessInstance(historicProcessInstance.
		 * getId()); }
		 */

		// taskService.createTaskQuery().termination();
		// taskService.setVariableLocal("1110002", "num", "local_B");
		// System.out.println(taskService.getVariable("1065013",
		// "test").toString());

		/*
		 * Map<String, Object> map= taskService.getVariables("1065013"); for
		 * (Entry<String, Object> entry : map.entrySet()) {
		 * System.out.println(entry.getKey()+"  "+entry.getValue()); }
		 */

		List<HistoricTaskInstance> list = histroyService.createHistoricTaskInstanceQuery().list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getBusinessKey());
		}

	}

	@Test
	public void taskSearchTest(){
		//taskService.cancelBack("1425070");
	//	taskService.backToActivity("1427502", "A", true);
	//	taskService.setVariableLocal("1457505", "B_local", "B_newLocal");
		
		taskService.complete("750014");
		
		
		
		
		
	}

}
