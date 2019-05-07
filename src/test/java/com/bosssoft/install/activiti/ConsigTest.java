package com.bosssoft.install.activiti;

import java.util.ArrayList;
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
import org.activiti.engine.consign.ConsignDefInfo;
import org.activiti.engine.consign.ConsignDetail;
import org.activiti.engine.consign.ConsignRelationInfo;
import org.activiti.engine.consign.ConsignTask;
import org.activiti.engine.consign.ConsignDefType;
import org.activiti.engine.impl.TaskExt;
import org.activiti.engine.impl.persistence.entity.ConsignRelationEntity;
import org.activiti.engine.impl.persistence.entity.ConsignDefineExcludeEntity;
import org.activiti.engine.repository.ConsignDefineExclude;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsigTest extends BaseTest{
	
	
    
    @Test
    public void cretaeNewConsig(){
    	repositoryService
    	
    	.createProcessConsignDefinition()
    	//.consignDefinitionId("20001")
    	.consignor("u02")
    	.addConsignee(new Participator("u05", "李四", IdentityEnum.USER, null))
    	.consignDefType(ConsignDefType.PART)
    	.addExcludeProcess("test")
    	.startTime("2018-06-11")
    	.endTime("2018-8-21")
    	.reason("出差")
    	.save();                
    }
    
    @Test
    public void consigSearch(){
    	List<ConsignDetail> list=repositoryService.createConsignDefineQuery().consignor("u02").list();
    	for (ConsignDetail consignDetail : list) {
			System.out.println(consignDetail.getConsignDefId()+"  "+consignDetail.getStartTime());
		}
    }
    
    @Test
    public void updateConsign(){
    	List<String> list=new ArrayList<String>();
    	list.add("newm1");
    	list.add("new2");
    	
    	repositoryService
    	.createProcessConsignDefinition()
    	//.ConsignDefinitionId("2342502")
    	.consignor("u02")
    	.addConsignee(new Participator("u01", "小明", IdentityEnum.USER, null))
    	.addConsignee(new Participator("r01", "部门经理", IdentityEnum.ROLE, null))
    	.consignDefType(ConsignDefType.ALL)
    	.setExcludeProcessList(list)
    	.startTime("2018-05-11")
    	.endTime("2018-09.22")
    	.reason("出差")
    	.save();                
    }
   
    @Test
    public void testDeleteConsign(){
    	repositoryService.deleteConsignDefinition("2282501");
    }
    
    
    
    /**
     * 修改委托定义
     */
    @Test
    public void editeConsigDef(){
    	/*List<ConsignDetail> list=repositoryService.createConsignDefineQuery().consignor("AA").list();
    	if(list.size()>0){
    		ConsignDetail consignDetail=list.get(0);
    		
    		//修改时间和理由
    		ConsignDefInfo info=consignDetail;
    		info.setConsignReason("没有理由");
    		info.setEndTime("2018-05-27");
    		
    		repositoryService.savePartConsignDefine(info, consignDetail.getConsignExcludeList());
    	}*/
    	
    	/*List<ConsignDetail> list=repositoryService.createConsignDefineQuery().consignor("AA").list();
    	if(list.size()>0){
    		ConsignDetail consignDetail=list.get(0);
    		
    		//删除一个委托对象,增加一个例外流程，删除一个例外流程
    		ConsignDefInfo info=consignDetail;
    		info.getRelationList().remove(0);
    		consignDetail.getConsignExcludeList().remove(0);
    		ConsignDefineExcludeEntity exclude=new ConsignDefineExcludeEntity();
    		exclude.setConsignDefId(info.getConsignDefId());
    		exclude.setProcessKey("testBill");
    		consignDetail.getConsignExcludeList().add(exclude);
    		
    		repositoryService.savePartConsignDefine(info, consignDetail.getConsignExcludeList());
    	}*/
    	
    	
    	List<ConsignDetail> list=repositoryService.createConsignDefineQuery().consignor("AA").effective().list();
    	if(list.size()>0){
    		ConsignDetail consignDetail=list.get(0);
    		
    		//部分代理改为全部代理
    		ConsignDefInfo info=consignDetail;
    		info.setConsignType(ConsignDefType.ALL);
    		ConsignRelationInfo relationInfo=new ConsignRelationInfo();
    		relationInfo.setConsignee("AA");
    		relationInfo.setConsigneeType(IdentityEnum.POSITION);
    		relationInfo.setConsignee("研发");
    		info.getRelationList().add(relationInfo);
    		
    		repositoryService.saveAllConsignDefine(info);
    	}
    	
    }
    
    @Test
    public void searchConsignRelation(){
    	List<ConsignDetail> list=	repositoryService.createConsignDefineQuery()
    			                                     .consignor("u02")
    			                                     .notEffective()
    			                                     .list();
    	for (ConsignDetail consignDetail : list) {
			System.out.println(consignDetail.getConsignDefId());
		}
    	
    }
    
    /**
     * 查询代理关系 并且创建代理
     */
    @Test
    public void searchConsign(){
       /* ConsignDetail consignDetail=repositoryService.createConsignDefineQuery().consignor("AA").processKey("Bill").isEffective().singleResult();
    	System.out.println(consignDetail);*/
    	/*if(consignDetail!=null){
    	String id=	taskService.createTaskDelegateInst(consignDetail.getConsignor(),
    				                 consignDetail.getConsignee(), IdentityEnum.getEnum(consignDetail.getConsigneeType()), "2210", null);
    	System.out.println(id);
    	
    	
    	}else{
    		System.out.println("不存在代理关系");
    	}*/
    	
    	//String id=	taskService.createTaskDelegateInst("AA", "BB", IdentityEnum.USER, true, "445005", null);
    	
    	/*taskService.createTaskDelegateInst("大大", "小小", IdentityEnum.USER, false, "445035", null);
    	taskService.createTaskDelegateInst("大大", "总经理", IdentityEnum.ROLE, false, "445035", null);*/
    	String id=taskService.createTaskCooperationInst("u01","u03",IdentityEnum.USER, "540006", null);
    	//taskService.createTaskSubstitutionInst("AA","AA_NAME","BB_NAME","DD", IdentityEnum.USER, "111", id);
    }
    
    @Test
    public void edite(){
    	/*taskService.claim("537509", null);*/
    	//taskService.setAssignee("445035", "BB");
    	/*taskService.complete("445005");
    	taskService.complete("445035");*/
    	taskService.complete("1912532");
    	
    }
    
    
    @Test
    public void constructConsignData(){
    	
    	/*String id=	taskService.createTaskDelegateInst("大大","小小", IdentityEnum.USER, "367535", null);
        id=taskService.createTaskDelegateInst("大大","总经理", IdentityEnum.ROLE, "367535", null);
    	*/
    	taskService.createTaskSubstitutionInst("BB","CC",IdentityEnum.USER, "445005", "447501");
   }
    
   //待办任务的查询
    @Test
    public void searchUnfinish(){
    	
    	
    	
    	List<TaskExt> list=taskService.createTaskExtQuery().delegateTaskAssigneeOrCandidate("BB").list();
    	System.out.println(list.size());
    	/*List<String> processDefIds=new ArrayList<String>();
    	processDefIds.add("leave:1:442504");
    List<TaskExt> list=	taskService.createTaskExtQuery()
    	          .taskAssigneeOrCandidate("BB")
    	           .role("总经理")
    	         // .processDefIds(processDefIds)
    	           .list();
    for (TaskExt taskExt : list) {
		System.out.println("任务Id："+taskExt.getTaskId()+"流程名称："+taskExt.getProcessName()+"  创建人ID： "+taskExt.getStarterId()+
				" 业务状态 :"+taskExt.getConsignInstType()+" 任务状态："+taskExt.getConsigState());
	}*/
    }
    
  //已办任务的查询
    @Test
    public void searchfinished(){
    	List<String> processDefIds=new ArrayList<String>();
    	processDefIds.add("leave:1:442504");
    	
    	
    	List<TaskExt> list=histroyService
    			          .createHistoricTaskExtQuery()
    			          .completeDelegateTaskAssignee("BB")
    			          //.processDefIds(processDefIds)
    			          .list();
    	for (TaskExt taskExt : list) {
    		System.out.println("任务Id："+taskExt.getTaskId()+"流程名称："+taskExt.getProcessName()+"  创建人ID： "+taskExt.getStarterId()+
    				" 业务状态 :"+taskExt.getConsignInstType()+" 任务状态："+taskExt.getConsigState());
		}
    }
    
    @Test
    public void searchConsignTask(){
    	List<ConsignTask> list=histroyService.createHistoricConsignTaskQuery().consignor("BB").substitution().unfinish().list();
    	System.out.println(list.size());
    }
    
    
    

    
}
