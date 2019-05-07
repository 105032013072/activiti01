package com.bosssoft.install.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;

public class DeleteEntityListener implements ActivitiEventListener{

	public void onEvent(ActivitiEvent event) {
		//System.out.println("捕获到entity删除事件："+event.getClass().getSimpleName());
		
	}

	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
