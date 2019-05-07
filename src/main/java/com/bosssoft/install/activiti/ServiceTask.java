package com.bosssoft.install.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceTask implements JavaDelegate{
	
	public void execute(DelegateExecution execution) throws Exception {
		
		
		
		System.out.println("serviceTask已经执行已经执行！text: ");
		
	}

}
