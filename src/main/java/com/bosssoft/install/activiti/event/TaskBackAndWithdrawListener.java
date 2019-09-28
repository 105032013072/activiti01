package com.bosssoft.install.activiti.event;

import java.util.List;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import org.activiti.engine.task.Task;

public class TaskBackAndWithdrawListener implements ActivitiEventListener{

	public void onEvent(ActivitiEvent event) {
		/* ActivitiEntityEvent entityEvent=(ActivitiEntityEvent)event;
			
			switch (event.getType()) {

		      case TASK_BACKED:
		       TaskBackAndWithdrawEntity	entity=  (TaskBackAndWithdrawEntity) entityEvent.getEntity();
		       System.out.println("任务回退");
		       printInfo(entity);
		        break;

		      case TASK_WITHDRAWED:
		    	  entity=  (TaskBackAndWithdrawEntity) entityEvent.getEntity();
		    	System.out.println("任务回收");
		    	printInfo(entity);
		        break;

		      default:
		        System.out.println("Event received: " + event.getType());
		    }
		*/
	}
	
	/*public void printInfo(TaskBackAndWithdrawEntity entity){
		List<Task> newList=	entity.getBackTargets();
		StringBuffer buffer=new StringBuffer("新的任务节点：");
		for (Task task : newList) {
			buffer.append("["+task.getId()+", "+task.getTaskDefinitionKey()+"]");
		}
		System.out.println(buffer.toString());
	}*/

	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
