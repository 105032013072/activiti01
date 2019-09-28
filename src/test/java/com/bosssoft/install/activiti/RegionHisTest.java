package com.bosssoft.install.activiti;

import java.util.List;

import org.activiti.engine.BpmnxTool;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.Test;

public class RegionHisTest extends BaseTest{
    
	public  static final String  ORGCODE_1="code1" ;
	public  static final String  ORGCODE_2="code2" ;
	
	//=====query
	
	@Test
	public void hisActQuery(){
		List<HistoricActivityInstance> list=	histroyService.createHistoricActivityInstanceQuery().list();
		System.out.println("=====all");
		for (HistoricActivityInstance historicActivityInstance : list) {
			System.out.println(historicActivityInstance.getId()+"  "+historicActivityInstance.getActivityId()+"  "+historicActivityInstance.getActivityName()+" "+historicActivityInstance.getOrgCode());
		}
		
		list=	histroyService.createHistoricActivityInstanceQuery().activityOrgCode(ORGCODE_1).list();
		System.out.println("=====code1");
		for (HistoricActivityInstance historicActivityInstance : list) {
			System.out.println(historicActivityInstance.getId()+"  "+historicActivityInstance.getActivityId()+"  "+historicActivityInstance.getActivityName()+" "+historicActivityInstance.getOrgCode());
		}
		
		
		list=	histroyService.createHistoricActivityInstanceQuery().activityOrgCode(ORGCODE_2).list();
		System.out.println("=====code2");
		for (HistoricActivityInstance historicActivityInstance : list) {
			System.out.println(historicActivityInstance.getId()+"  "+historicActivityInstance.getActivityId()+"  "+historicActivityInstance.getActivityName()+" "+historicActivityInstance.getOrgCode());
		}
		
	}
	
	@Test
	public void hisTaskQuery(){
		List<HistoricTaskInstance> list=histroyService.createHistoricTaskInstanceQuery().list();
		System.out.println("===all");
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getId()+"  "+historicTaskInstance.getTaskDefinitionKey()+" "+historicTaskInstance.getOrgCode());
		}
		
		
		list=histroyService.createHistoricTaskInstanceQuery().taskOrgCode(ORGCODE_1).list();
		System.out.println("===code1");
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getId()+"  "+historicTaskInstance.getTaskDefinitionKey()+" "+historicTaskInstance.getOrgCode());
		}
		
		list=histroyService.createHistoricTaskInstanceQuery().taskOrgCode(ORGCODE_2).list();
		System.out.println("===code2");
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println(historicTaskInstance.getId()+"  "+historicTaskInstance.getTaskDefinitionKey()+" "+historicTaskInstance.getOrgCode());
		}
	}
	
	
	@Test
	public void hisProcessInstQuery(){
		List<HistoricProcessInstance> list=histroyService.createHistoricProcessInstanceQuery().list();
		System.out.println("====all");
		for (HistoricProcessInstance historicProcessInstance : list) {
			System.out.println(historicProcessInstance.getProcessDefinitionId()+"  "+historicProcessInstance.getOrgCode());
		}
		
		
		list=histroyService.createHistoricProcessInstanceQuery().processInstanceOrgCode(ORGCODE_1).list();
		System.out.println("====code1");
		for (HistoricProcessInstance historicProcessInstance : list) {
			System.out.println(historicProcessInstance.getProcessDefinitionId()+"  "+historicProcessInstance.getOrgCode());
		}
		
		list=histroyService.createHistoricProcessInstanceQuery().processInstanceOrgCode(ORGCODE_2).list();
		System.out.println("====code2");
		for (HistoricProcessInstance historicProcessInstance : list) {
			System.out.println(historicProcessInstance.getProcessDefinitionId()+"  "+historicProcessInstance.getOrgCode());
		}
	}
	
	@Test
	public void query(){
		List<HistoricProcessInstance>  list=	bpmnxTool.getLatestVersionProcessInstance("Process_A", ORGCODE_2);
	    for (HistoricProcessInstance historicProcessInstance : list) {
			System.out.println(historicProcessInstance.getId()+"  "+historicProcessInstance.getOrgCode());
		}
	}
}
