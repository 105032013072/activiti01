package com.bosssoft.install.activiti.event;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

public class TaskCompleteListener implements ActivitiEventListener{

	public void onEvent(ActivitiEvent event) {
		System.out.println("event");
	}

	public boolean isFailOnException() {
		return false;
	}

}
