package com.bosssoft.install.activiti.back;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.junit.Test;

import com.bosssoft.install.activiti.BaseTest;

public class MyTest extends  BaseTest{
   
	@Test
	public void  complete(){
		/*Map<String,Object> var=new HashMap<String, Object>();
	   List<String> list=new ArrayList<String>();
	   list.add("aa");
	   list.add("bb");
		var.put("collection", list);
	
		taskService.complete("1107505",var);*/

	   taskService.complete("60087");
		
		/*taskService.complete("487506");*/
		/*taskService.complete("470012");*/
	}
	
	@Test
	public void back(){
		//taskService.backToActivity(currentTaskId, targetActivityId, iskeepTaskState);
		taskService.backToActivity("550004", "D", true);
	}
	
	@Test
	public void cancelBack(){
		taskService.cancelBack("602509");
	}
	
	
}
