package com.bosssoft.install.activiti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.activiti.engine.BpmnxTool;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.helper.TaskFlowUtils;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.parse.ParticipatorRuleParser;
import org.activiti.engine.repository.Category;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosssoft.platform.common.extension.ExtensionLoader;

public class BaseTest {
	public static Logger logger = LoggerFactory.getLogger(ConsigTest.class);
	public ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	public TaskService taskService = processEngine.getTaskService();
	public RuntimeService runtimeService = processEngine.getRuntimeService();
	public HistoryService histroyService = processEngine.getHistoryService();
	public RepositoryService repositoryService = processEngine.getRepositoryService();
	public IdentityService identityService = processEngine.getIdentityService();
	public ManagementService managementService = processEngine.getManagementService();
	public FormService formService=processEngine.getFormService();
	public BpmnxTool bpmnxTool=processEngine.getBpmnxTool();
    public DynamicBpmnService dynamicBpmnService=processEngine.getDynamicBpmnService();
	
	
	@Test
	public void getActivity() {
		ActivityImpl activityImpl = TaskFlowUtils.getActivity(repositoryService, "finance:1:627504", "A");
	   
	}
	
	@Test
	public void one(){
		runtimeService.suspendProcessInstanceById("385023");
	}

	@Test
	public void  getParticipatorSource(){
		List<Participator> source=new ArrayList<Participator>();
		source.add(new Participator("u01", "01", IdentityEnum.USER, null));
		source.add(new Participator("u02", "01", IdentityEnum.USER, null));
		source.add(new Participator("u03", "01", IdentityEnum.USER, null));
		
		
		List<Participator> mtu=new ArrayList<Participator>();
		mtu.add(new Participator("u01", "01", IdentityEnum.USER, null));
		mtu.add(new Participator("u02", "01", IdentityEnum.USER, null));
		
		source.remove(mtu);
		for (Participator participator : source) {
			System.out.println(participator.getParticipatorId());
		}
	}
	
	
	public Date getDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
        	return sdf.parse(strDate);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
       
	}
}
