package com.bosssoft.install.activiti;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class BeforeTest {
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	private TaskService taskService = processEngine.getTaskService();
	private RuntimeService runtimeService = processEngine.getRuntimeService();
	private HistoryService histroyService = processEngine.getHistoryService();
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private IdentityService identityService = processEngine.getIdentityService();
    private ManagementService managementService=processEngine.getManagementService();
    
    
    @Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("请假流程")//添加部署的名称
   		.addClasspathResource("com/hr/leaveprocess.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		.addClasspathResource("com/hr/leaveprocess.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
   		
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("离职流程")//添加部署的名称
   		.addClasspathResource("com/hr/quitprocess.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		.addClasspathResource("com/hr/quitprocess.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
   		
   		
   	/*	repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("合同审批流程")//添加部署的名称
   		.addClasspathResource("com/contract/contract.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		.addClasspathResource("com/contract/contract.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
   		
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("宿舍申请流程")//添加部署的名称
   		.addClasspathResource("com/manage/dormitory.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		.addClasspathResource("com/manage/dormitory.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
*/   	}
    
 // 启动请假流程,测试代理
 	@Test
 	public void startProcess() {
 		identityService.setAuthenticatedUserId("u01");
 		ProcessInstance in = runtimeService.startProcessInstanceByKey("leave");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "请假申请_tom");// AA

 		identityService.setAuthenticatedUserId("u02");
 		in = runtimeService.startProcessInstanceByKey("quit");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "离职申请_123");// 部门经理（AA，BB，CC）

 		/*identityService.setAuthenticatedUserId("tom");
 		in = runtimeService.startProcessInstanceByKey("dormitory");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "宿舍申请_tom");// AA,CC

 		identityService.setAuthenticatedUserId("mike");
 		 in = runtimeService.startProcessInstanceByKey("contract");
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "合同审批_mike");// AA,BB,大大
*/ 	}
    
    
    @Test
   	public void deleteDeployment() {
   		List<Deployment> list = repositoryService.createDeploymentQuery().list();
   		for (Deployment deployment : list) {
   			System.out.println(deployment.getId());

   			repositoryService.deleteDeploymentCascade(deployment.getId());
   		}
   	}
}
