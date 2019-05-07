package com.bosssoft.install.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;

public class GlobalEvenListener implements ActivitiEventListener{

	public void onEvent(ActivitiEvent event) {
		System.out.println("捕获到事件："+event.getClass().getSimpleName()+"   "+event.getType());
	}

	public boolean isFailOnException() {
		
		return false;
	}

}
