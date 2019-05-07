package com.bosssoft.install.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceListener implements JavaDelegate{

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("任务未完成提醒");
	}

}
