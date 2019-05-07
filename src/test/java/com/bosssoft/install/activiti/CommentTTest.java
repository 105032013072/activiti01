package com.bosssoft.install.activiti;

import java.util.List;

import org.activiti.engine.impl.Direction;
import org.activiti.engine.impl.history.CommentDisplayType;
import org.activiti.engine.impl.runtime.Action;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class CommentTTest extends BaseTest{

	@Test
	public void completeWithComment(){
    Task task=taskService.createTaskQuery().taskId("55061").singleResult();
		
    taskService.complete(task.getId());
    // taskService.addCommentWithUserId(task.getId(), null, task.getProcessInstanceId(), Action.COMPLETE_TASK, "完成", task.getAssignee());
	
	}
	
	@Test
	public void getComment(){
		String processInstanceId="55010";
		
		List<Comment> result=taskService.getProcessInstanceComments(processInstanceId,CommentDisplayType.WITHCURRENT,Direction.DESCENDING);
		for (Comment comment : result) {
			System.out.println("taskName: "+comment.getTaskName()+" userId: "+comment.getUserId()
			+" userName: "+comment.getUserName()+" msg: "+comment.getFullMessage());
		}
	}
	
	@Test
	public void getCommentFull(){
		//String processInstanceId="77520";
		String processInstanceId="85012";
		List<Comment> result=taskService.getProcessInstanceComments(processInstanceId,CommentDisplayType.FULL,Direction.ASCENDING);
		for (Comment comment : result) {
			System.out.println("taskName: "+comment.getTaskName()+" userId: "+comment.getUserId()
			+" userName: "+comment.getUserName()+" msg: "+comment.getFullMessage());
		}
	}
}
