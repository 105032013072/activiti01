package com.bosssoft.install.activiti.listener;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosssoft.install.activiti.Mybean;

public class TaskCompleteListener implements ActivitiEventListener{

	
	public void onEvent(ActivitiEvent event) {
		ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
		Object entity = entityEvent.getEntity();
		if (entity instanceof TaskEntity){
			TaskEntity task = (TaskEntity) entity;
			HistoricTaskInstanceEntity hisTaskEntity=Context.getCommandContext()
					                                       .getHistoricTaskInstanceEntityManager()
					                                       .findHistoricTaskInstanceById(task.getId());
			
			hisTaskEntity.setExt1("已办");
		}
	}

	public boolean isFailOnException() {
		return false;
	}

}
