package com.bosssoft.install.activiti.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.consign.ConsignDetail;
import org.activiti.engine.consign.ConsignRelationInfo;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.TaskExt;
import org.activiti.engine.impl.bpmn.helper.ConsignInstLinkHelper;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ReminderEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.impl.variable.ActVariable;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangxw
 *
 */
public class TaskCreateGrobleListener implements ActivitiEventListener{
	

	private static Logger logger = LoggerFactory.getLogger(TaskCreateGrobleListener.class);
	
	private TaskService taskService;

	public void onEvent(ActivitiEvent event) {
		
		taskService=event.getEngineServices().getTaskService();
     
        if (!(event instanceof ActivitiEntityEventImpl)) {
            return;
        }
 
        ActivitiEntityEventImpl activitiEntityEventImpl = (ActivitiEntityEventImpl) event;
        Object entity = activitiEntityEventImpl.getEntity();
 
        if (!(entity instanceof TaskEntity)) {
            return;
        }
 
        TaskEntity taskEntity = (TaskEntity) entity;
 
       //模拟任务创建完成后，设置时效提醒的定时器
        ReminderEntity job=new ReminderEntity();
       job.setDuedate(getOutTime());
       job.setExclusive(true);
       job.setExecutionId(taskEntity.getExecutionId());
       job.setProcessInstanceId(taskEntity.getProcessInstanceId());
       job.setProcessDefinitionId(taskEntity.getProcessDefinitionId());
       job.setTaskId(taskEntity.getId());
       
		//Context.getCommandContext().getJobEntityManager().schedule(job);
		
       
    }

	public boolean isFailOnException() {
		
		return false;
	}
 
    
	private  Date getOutTime(){
 		Calendar ca=Calendar.getInstance();
 		ca.setTime(new Date());
 		ca.add(Calendar.MINUTE, 1);
 		return ca.getTime();
 	}
	
}
