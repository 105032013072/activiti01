package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.DefaultNotificationDetail;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.CarbonCopyEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.NotificationEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.notification.NotificationType;
import org.activiti.engine.spi.task.BackCompensateHandler;
import org.activiti.engine.spi.task.TaskContext;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class AuthorityOpTest extends BaseTest{
   
	
	
	@Test
	public void returnToLast(){
		//taskService.backToLast("3562502",true);
		/*taskService.backToLast("3855007", false, new BackCompensateHandler() {
			
			public void before(String businessKey, List<TaskContext> currentTasks) {
				System.out.println("--------before----");
				System.out.println("businesskey: "+businessKey);
				for (TaskContext task : currentTasks) {
				
					System.out.println("节点："+task.getTaskDefinitionKey()+"   办理人："+task.getAssignee());
				}
			}
			
			public void after(String businessKey, List<TaskContext> newTasks) {
				System.out.println("--------after----");
				System.out.println("businesskey: "+businessKey);
				for (TaskContext task : newTasks) {
					System.out.println("节点："+task.getTaskDefinitionKey()+"   办理人："+task.getAssignee());
				}
				
			}
		});*/

		taskService.backToLast("955004", false);
		
		
	}
	
	@Test
	public void returnToActivity(){
		taskService.backToActivity("1210155", "A", false);
		taskService.cancelBack("482567");
	}
	
	@Test
	public void returnToApply(){
		/*Map<String, Object> params = new HashMap<String, Object>();
		params.put("num", 60);*/
		
		taskService.backToReapply("1040036");
		
		//taskService.backToReapply("1002562");
	//	taskService.createTaskSubstitutionInst(consignor, consignee, consigneeType, taskId, consignSourceId);
		
       
	}
	
	//加签
	@Test
	public void insert(){
	// taskService.insertBefore("410014", "u05");
		//taskService.insertParallel("430034", "u02","u03");
		//taskService.setVariableLocal("350002","测试变量" , "test");
		
		taskService.insertParallel("452507", "u05");
	 
	 
	  
	}
	
	
	//回收
	@Test
	public void callBack(){
		
	taskService.withdrawByAssignee("977579", "u01", true);
		
		//taskService.unclaimCascade("435002", null);
		//taskService.withdrawByStarter("147501");
		//runtimeService.revokeProcess(processInstanceId, revokeReason, userId, userName);
		
	//	taskService.createTaskQuery().processInstanceBusinessKey(processInstanceBusinessKey).
	}

	/*public void */
	
	//通知查询
	@Test
	public void notificationSearch(){
		List<NotificationEntity> list=	runtimeService.createNotificationListQuery()
				                         .receiverId("u01")
				                         .notificationType(NotificationType.CARBONCOPY)
				                         //.alreadyRead()
				                         .orderByCreateTime()
				                        // .taskNameLike("%%审核%%")
				                         .desc()
                                         .list();
				                         
		for (NotificationEntity notificationEntity : list) {
			CarbonCopyEntity carbonCopyEntity=(CarbonCopyEntity) notificationEntity;
			System.out.println(notificationEntity.getTitle()+"  "+notificationEntity.getId()+" "+notificationEntity.getProcInstName()+"  "+notificationEntity.getTaskName());
		}
		
	}
	
	
	//通知详情查询，办理
	@Test
	public void notificationDetailSearch(){
		DefaultNotificationDetail detail=  runtimeService.getNotificationDetail("765005");
		System.out.println(detail.getAssigneeId());
		//runtimeService.handleNotification(detail.getId(), "u04", true);
	}
	
	
	//撤销
	@Test
	public void revoke(){
		runtimeService.revokeProcess("385023", "不需要请假了", "u01");
		runtimeService.terminateProcess(processInstanceId, reason, userId);
	}
	
	//查询撤销流程
	@Test
	public void getRevokeProcess(){
		List<ProcessInstance> list=  runtimeService.createProcessInstanceQuery().revoke().list();
		for (ProcessInstance processInstance : list) {
			StringBuffer strbuffer=new StringBuffer("");
			strbuffer.append("流程定义Id："+processInstance.getId()+" 流程名："+processInstance.getName()+" ");
			List<Comment> commentlist=taskService.getProcessInstanceComments(processInstance.getId(), HistoricProcessInstanceEntity.DELETE_REASON_REVOKE);
			if(commentlist.size()>0){
				Comment c=commentlist.get(0);
				strbuffer.append("撤销人："+c.getUserName()+" 撤销原因："+c.getFullMessage());
			}
			System.out.println(strbuffer.toString());
		}
	}
	
	@Test
	public void recoveryRevokeProcess(){
		runtimeService.recoveryProcess("385023");
		
		runtimeService.activateProcessInstanceById(processInstanceId);
		
	}
	
	
	@Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		.addClasspathResource("com/finance/finance3.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		.addClasspathResource("com/finance/finance3.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
   	}
    
 // 启动请假流程,测试代理
 	@Test
 	public void startProcess() {
 		/*identityService.setAuthenticatedUserId("tom");
 		ProcessInstance in = runtimeService.startProcessInstanceByKey("leave");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "请假申请_tom");// AA

 		identityService.setAuthenticatedUserId("123");
 		in = runtimeService.startProcessInstanceByKey("quit");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "离职申请_123");// 部门经理（AA，BB，CC）

 		identityService.setAuthenticatedUserId("tom");
 		in = runtimeService.startProcessInstanceByKey("dormitory");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "宿舍申请_tom");// AA,CC
*/
 		identityService.setAuthenticatedUserId("u01");
 		ProcessInstance in = runtimeService.startProcessInstanceByKey("finance", "business_01");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "报销审批_mike");// AA,BB,大大
 	}
 	
 	@Test
	public void deleteDeployment() {
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery().deploymentName("财政流程")
				.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId());

			processEngine.getRepositoryService().deleteDeploymentCascade(deployment.getId());
		}
	}
 	
 	//知会
 	@Test
 	public void inform(){
 		
		// taskService.add
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> person = new ArrayList<String>();
		person.add("1");
		person.add("2");
		person.add("3");
		map.put("users", person);

		Participator p = new Participator();
		p.setParticipatorId("r01");
		p.setParticipatorType(IdentityEnum.ROLE);
		p.setParticipatorName("角色");
		taskService.completeInformCascade("42564", null, map, "请确认办理情况", p);
 	
 	}
 	
 	@Test
 	public void complet(){
 		taskService.complete("532507");
 		
 		
 	}
 	
 	@Test
 	public void claim(){
 		//taskService.setVariable("1065013", "ALL", "全局");
 		System.out.println(taskService.getVariableLocal("1065013", "ALL"));
 		//runtimeService.getVariable("1065008", "num");
 	}
 	
 	@Test
 	public void jobTest() throws InterruptedException{
 	    String[] hisTaskArray={"295010","295014"};
 	    for (String string : hisTaskArray) {
			taskService.withdrawByAssignee(string, "u01", true);
		}
 	    
 	    
 	    taskService.createTaskQuery()
 	}
 	
 	
 
 	
 
}
