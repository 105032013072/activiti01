package com.bosssoft.install.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.Direction;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Comment;
import org.junit.Test;

public class CommentTTest extends BaseTest{

	@Test
	public void completeWithComment(){
    /*Task task=taskService.createTaskQuery().taskId("55061").singleResult();
		
    taskService.complete(task.getId());*/
    // taskService.addCommentWithUserId(task.getId(), null, task.getProcessInstanceId(), Action.COMPLETE_TASK, "完成", task.getAssignee());
	
		
	}
	
	@Test
	public void getComment(){
		String processInstanceId="80061";
		//runtimeService.terminateProcess(processInstanceId);
		runtimeService.activateProcessInstanceById(processInstanceId);
		
		List<Comment> result=taskService.getProcessInstanceComments(processInstanceId,CommentDisplayType.WITHCURRENT,Direction.DESCENDING);
		for (Comment comment : result) {
			System.out.println("taskName: "+comment.getTaskName()+" userId: "+comment.getUserId()
			+" userName: "+comment.getUserName()+" msg: "+comment.getFullMessage());
		}
	}
	
	@Test
	public void getCommentFull(){
		//String processInstanceId="77520";
		
		Map<String,Object> map=new HashMap<String, Object>();
		
		ExecutionEntity executionEntity=(ExecutionEntity) runtimeService.createExecutionQuery().executionId("342529").singleResult();
		//map.put("var1", "a");
		//map.put("var2","b");
		map.put("code", "999");
		map.put("agencylevel", "1");
		map.put("agencytype", "0");
		map.put("paytypecode", "001001009");

		
		
		
		String processInstanceId="155077";
		List<Comment> result=taskService.getProcessInstanceComments(processInstanceId,org.activiti.engine.impl.history.comment.CommentDisplayType.FULL_CURRENT,Direction.ASCENDING,map);
		for (Comment comment : result) {
			System.out.println("taskName: "+comment.getTaskName()+" userId: "+comment.getUserId()
			+" userName: "+comment.getUserName()+" msg: "+comment.getFullMessage());
		}

	}
	
	@Test
	public void getCommentByProcessDefKey(){
		
		Map<String,Object> map=new HashMap<String, Object>();
		
		ExecutionEntity executionEntity=(ExecutionEntity) runtimeService.createExecutionQuery().executionId("342529").singleResult();

		map.put("code", "999");

		String processDefinitionKey="Process_A:1:127505";
		List<Comment> result=taskService.getCommentsByProcessDefinitionId(processDefinitionKey,map);
		for (Comment comment : result) {
			System.out.println("taskName: "+comment.getTaskName()+" userId: "+comment.getUserId()
			+" userName: "+comment.getUserName()+" msg: "+comment.getFullMessage());
		}

	}
}
