package com.bosssoft.install.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.TransitionImpl;


public class SequenceFlowTaskListener implements JavaDelegate{

	public void execute(DelegateExecution execution) throws Exception {
		ExecutionEntity executionEntity =(ExecutionEntity) execution;
		TransitionImpl	transitionImpl=executionEntity.getTransition();
		
		System.out.println("SequenceFlowTaskListener: "+transitionImpl.getSource().getId()+"---->"+transitionImpl.getDestination().getId());
		
	}

	
}
