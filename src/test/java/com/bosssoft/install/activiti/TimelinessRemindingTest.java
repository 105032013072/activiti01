package com.bosssoft.install.activiti;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.bpmn.helper.AgeingRemindHelper;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.runtime.ExecuteCalendar;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class TimelinessRemindingTest extends BaseTest{
    
	@Test
   	public void deploy() {
   		
   		repositoryService//与流程定义和部署对象相关的Service
   		.createDeployment()//创建一个部署对象
   		.name("财政流程")//添加部署的名称
   		.addClasspathResource("com/finance/finance3.bpmn")//从classpath的资源中加载，一次只能加载一个文件
   		//.addClasspathResource("com/finance/finance3.png")//从classpath的资源中加载，一次只能加载一个文件
   		.deploy();//完成部署
   		
   	}
    
  
 	@Test
 	public void startProcess() throws InterruptedException {
 	    
 		identityService.setAuthenticatedUserId("AA");
 		
 		Map<String, Object> variable=new HashMap<String, Object>();
 		variable.put("counterSignService", new CounterSignService());
 		variable.put("timeOut", getOutTime());
 		
 		ProcessInstance in=runtimeService.startProcessInstanceByKey("finance", "business_01", variable);
 		
 		runtimeService.setProcessInstanceName(in.getProcessInstanceId(), "报销审批_mike");
 		
 		Thread.sleep(600000000);
 		
 	}
	
 	
 	@Test
	public void deleteDeployment() {
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery().deploymentName("财政流程")
				.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId());

			processEngine.getRepositoryService().deleteDeploymentCascade(deployment.getId());
		    //repositoryService.deleteDeployment(deployment.getId(), false);
		}
	}
 	
 	private  Date getOutTime(){
 		Calendar ca=Calendar.getInstance();
 		ca.setTime(new Date());
 		ca.add(Calendar.MINUTE, 1);
 		return ca.getTime();
 	}
 	
 	
 	@Test
 	public void complet() throws InterruptedException{
 		taskService.complete("277502");
 		Thread.sleep(600000000);
 		
 	}
 	
 	@Test
 	public void activitOrSuspend() throws InterruptedException{
 		runtimeService.activateProcessInstanceById("760001");
 		//runtimeService.suspendProcessInstanceById("760001");
 		Thread.sleep(600000000);
 	}
 	
 	
 	@Test
 	public void claim(){
 		//taskService.claim("4042527", "u1");
 		System.out.println(StringUtils.isEmpty(null));
 		
 	}
 	
 	@Test
 	public void test(){
 		taskService.insertAfter("4480018", "TTTT");
 		
 	}
 	
 	@Test
 	public void wordkDayTest() throws Exception{
 		Calendar startCalendar=Calendar.getInstance();
 	
 		
 		startCalendar.set(Calendar.YEAR, 2018);
 		startCalendar.set(Calendar.MONTH, 6);
 		startCalendar.set(Calendar.DAY_OF_MONTH,13);
 		startCalendar.set(Calendar.HOUR, 8);
 		
 		
 	
 		Calendar endCalendar=Calendar.getInstance();
 		endCalendar.set(Calendar.YEAR, 2018);
        endCalendar.set(Calendar.MONTH, 6);
        endCalendar.set(Calendar.DAY_OF_MONTH,16);
        endCalendar.set(Calendar.HOUR, 10);
        differ(endCalendar.getTime().getTime()-startCalendar.getTime().getTime());
        
 		
 		long date=AgeingRemindHelper.getDifferTimeByCalendar(startCalendar.getTime(), endCalendar.getTime(), ExecuteCalendar.WORKINGDAY);
 		differ(date);
 	}
 	
 	private static void differ(long diff) throws Exception{
	
		//按照传入的格式生成一个simpledateformate对象
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		//获得两个时间的毫秒时间差异
		
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算相差剩余多少小时
        long min=diff%nd%nh/nm;
        long s=diff%nd%nh%nm/ns;
        System.out.println(day+"   "+hour+"  "+min+"  "+s);
        
        
	}

}
