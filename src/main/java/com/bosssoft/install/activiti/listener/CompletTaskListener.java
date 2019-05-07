package com.bosssoft.install.activiti.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.jobexecutor.TimerStartEventJobHandler;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.task.DelegationState;

public class CompletTaskListener implements TaskListener{

	public void notify(DelegateTask delegateTask) {
        
        DelegateExecution  execution =delegateTask.getExecution();
        
        
        
	}

}
