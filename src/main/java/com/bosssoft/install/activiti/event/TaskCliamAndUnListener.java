package com.bosssoft.install.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.task.Task;

public class TaskCliamAndUnListener implements ActivitiEventListener{

	public void onEvent(ActivitiEvent event) {
    ActivitiEntityEvent entityEvent=(ActivitiEntityEvent)event;
		
		switch (event.getType()) {

	      case TASK_CLAIMED:
	       Task	task=  (Task) entityEvent.getEntity();
	    
	        System.out.println("任务被领取: "+task.getId()+"  "+task.getTaskDefinitionKey()+"  "+task.getAssigneeName());
	        break;

	      case TASK_UNCLAIMED:
	    	task=  (Task) entityEvent.getEntity();
	  	    
	        System.out.println("任务取消领取: "+task.getId()+"  "+task.getTaskDefinitionKey());
	        break;

	      default:
	        System.out.println("Event received: " + event.getType());
	    }
		
	}

	public boolean isFailOnException() {
		
		return false;
	}

}
