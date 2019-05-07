package com.bosssoft.install.activiti;

import org.activiti.bpmn.model.FormDefinition;
import org.junit.Test;

public class FormTest extends BaseTest{

	@Test
	public void getProcessForm(){
		FormDefinition formDefinition=formService.getProcessFormDefinition("Process_1:1:1092506");
		System.out.println(formDefinition.getFormPage());
	}
}
