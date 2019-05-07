package com.bosssoft.install.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 流程激活或者挂起事件
 * @author huangxw
 *
 */
public class ProcessActiveAndSupendListener implements ActivitiEventListener{

	public void onEvent(ActivitiEvent event) {
		ActivitiEntityEvent entityEvent=(ActivitiEntityEvent)event;
		
		switch (event.getType()) {

	      case PROCESS_ACTIVED:
	       ExecutionEntity	executionEntity=  (ExecutionEntity) entityEvent.getEntity();
	    
	        System.out.println("流程激活: "+executionEntity.getId()+"  "+executionEntity.getName());
	        break;

	      case PROCESS_SUPENDED:
	    	executionEntity=  (ExecutionEntity) entityEvent.getEntity();
	  	    
	        System.out.println("流程挂起: "+executionEntity.getId()+"  "+executionEntity.getName());
	        break;

	      default:
	        System.out.println("Event received: " + event.getType());
	    }
	}

	public boolean isFailOnException() {
		
		return false;
	}

}
