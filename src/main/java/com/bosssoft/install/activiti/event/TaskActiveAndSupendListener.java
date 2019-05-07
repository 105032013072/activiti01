package com.bosssoft.install.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.task.Task;

/**
 * 任务激活或者挂起
 * @author huangxw
 *
 */
public class TaskActiveAndSupendListener implements ActivitiEventListener {

	public void onEvent(ActivitiEvent event) {
     ActivitiEntityEvent entityEvent=(ActivitiEntityEvent)event;
		
		switch (event.getType()) {

	      case TASK_ACTIVED:
	       Task	task=  (Task) entityEvent.getEntity();
	    
	        System.out.println("任务激活: "+task.getId()+"  "+task.getTaskDefinitionKey());
	        break;

	      case TASK_SUPENDED:
	    	task=  (Task) entityEvent.getEntity();
	  	    
	        System.out.println("任务挂起: "+task.getId()+"  "+task.getTaskDefinitionKey());
	        break;

	      default:
	        System.out.println("Event received: " + event.getType());
	    }
		
	}

	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
