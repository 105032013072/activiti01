package com.bosssoft.install.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ProcessEndListener implements ExecutionListener{

	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("流程结束");
		
	}

}
