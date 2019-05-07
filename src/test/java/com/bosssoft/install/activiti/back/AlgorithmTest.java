package com.bosssoft.install.activiti.back;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.helper.TaskFlowUtils;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import com.bosssoft.install.activiti.BaseTest;

public class AlgorithmTest extends  BaseTest{

	@Test
	public void bfsTest(){
		Stack<ActivityImpl> stack=	minimumbfs("K", "A", "Process_1:1:810006");
		for (ActivityImpl activityImpl : stack) {
			System.out.print(activityImpl.getId()+"-->");
		}
	
	}
	
	//最短路径
	public void getMinimum(){
		String source="K";
		String target="A";
		String processDefId="Process_1:1:810006";
		
		Map<String, List<ActivityImpl>> predecessorMap=	bfs(source, target, processDefId);
		Stack<ActivityImpl> stack=new Stack<ActivityImpl>();//保存路径
		ActivityImpl end = TaskFlowUtils.getActivity(repositoryService, processDefId, );
		ActivityImpl predecessor=end;
		 do {
			    stack.push(predecessor);
               predecessor = predecessorMap.get(predecessor.getId());
         } while (predecessor != null
		
		
	}
	
	
	
	/**
	 * 广度搜索算法
	 * @param sourceActId 当前节点Id
	 * @param targetActId 回退节点ID
	 * @param processDefId
	 */
	//@Test
	public Stack<ActivityImpl> minimumbfs(String sourceActId, String targetActId, String processDefId) {
		LinkedList<ActivityImpl> queue = new LinkedList<ActivityImpl>();// 待搜索队列
		List<String> searchList = new ArrayList<String>();// 保存已经走过的节点
		Map<String, ActivityImpl> predecessorMap = new HashedMap();// 记录每个节点的前继节点
		ActivityImpl end = null;

		// 开始节点入队列
		ActivityImpl start = TaskFlowUtils.getActivity(repositoryService, processDefId, sourceActId);
		queue.offer(start);
		while (!queue.isEmpty()) {
			ActivityImpl activityImpl = queue.poll();
			if (searchList.contains(activityImpl.getId())) {// 已经走过，跳过
				continue;
			}

			if(activityImpl.getId().equals(targetActId)){
				end=activityImpl;
				break;
			}else{
				// 如果还不是终点，规把该顶点可以到达的邻居全部添加到队列末端
				for (PvmTransition neighborPvm : activityImpl.getIncomingTransitions()) {
					ActivityImpl neighborAct = (ActivityImpl) neighborPvm.getSource();
					queue.offer(neighborAct);

					if (predecessorMap.get(neighborAct.getId()) == null && neighborAct != start) {
						// 记录前继节点
						predecessorMap.put(neighborAct.getId(), activityImpl);
						
					}
				}
				searchList.add(activityImpl.getId());
			}
		
		}

		//获取结果
		 List<ActivityImpl> list=new ArrayList<ActivityImpl>();
		 if(end!=null){
			 ActivityImpl predecessor=end;
			 do {
				   list.add(predecessor);
	                predecessor = predecessorMap.get(predecessor.getId());
	           } while (predecessor != null);
			 
		 }
		 
		 //转存为栈
		 Stack<ActivityImpl> stack=new Stack<ActivityImpl>();
		 for(int i=list.size()-1;i>=0;i--){
			 stack.push(list.get(i));
		 }
		 return stack;
	}
	
	
	@Test
	public void testGetAll(){
		Set<String> set=	allBfs("g03", "g05", "Process_1:1:810006");
		for (String string : set) {
			System.out.print(string+"  ");
		}
	}
	
	
	
	
	public Set<String> allBfs(String sourceActId, String targetActId, String processDefId) {
		LinkedList<ActivityImpl> queue = new LinkedList<ActivityImpl>();// 待搜索队列
		Set<String> searchSet = new LinkedHashSet<String>();// 保存已经走过的节点
		Map<String, List<ActivityImpl>> predecessorMap = new HashedMap();// 记录每个节点的前继节点
		ActivityImpl end = null;

		// 开始节点入队列
		ActivityImpl start = TaskFlowUtils.getActivity(repositoryService,processDefId, sourceActId);
		queue.offer(start);
		while (!queue.isEmpty()) {
			ActivityImpl activityImpl = queue.poll();
			if (searchSet.contains(activityImpl.getId())) {// 已经走过，跳过
				continue;
			}

			if (!activityImpl.getId().equals(targetActId)) {
				// 如果还不是终点，规把该顶点可以到达的邻居全部添加到队列末端
				for (PvmTransition neighborPvm : activityImpl.getOutgoingTransitions()) {
					ActivityImpl neighborAct = (ActivityImpl) neighborPvm.getDestination();
					queue.offer(neighborAct);
				}
				searchSet.add(activityImpl.getId());

			}
		}

		return searchSet;
	}
	
	
	
	//记录前继节点
	public void recordPreAct(Map<String,List<ActivityImpl>> predecessorMap,String key,ActivityImpl pre ){
		if(predecessorMap.containsKey(key)){
			predecessorMap.get(key).add(pre);
		}else{
			List<ActivityImpl> preList=new ArrayList<ActivityImpl>();
			preList.add(pre);
			predecessorMap.put(key, preList);
		}
	}
	
	
	
	
	@Test
	public void dfsTest(){
		
		ActivityImpl start= TaskFlowUtils.getActivity(repositoryService, "Process_1:1:830006", "g04");
		
		ActivityImpl matchGw=dfs(start);
		if(matchGw!=null){
			System.out.println("-----");
			System.out.println(matchGw.getId());
		}else{
			System.out.println("-----");
			System.out.println("没有找到");
		}
	}
	
	@Test
	public void testDivision(){
		ActivityImpl start = TaskFlowUtils.getActivity(repositoryService, "Process_1:1:952506", "g08");
		System.out.println(getDivision(start).getId());
	}
	
	
	public ActivityImpl getDivision(ActivityImpl start){
		
		
		
		
		ActivityImpl result=null;
		List<String> searchList=new ArrayList<String>();//保存已经走过的节点
		Stack<ActivityImpl> stack=new Stack<ActivityImpl>();//保存搜索过程中网关
		searchList.add(start.getId());
		stack.push(start);
		//开始节点所有出线深度搜索
		for (PvmTransition outPvm : start.getIncomingTransitions()) {
			ActivityImpl source=(ActivityImpl)outPvm.getSource();
			result=dfsForDivision(source,searchList,stack);
			if(result!=null) return result; 
		}
		return result;
	}
	
	
	private ActivityImpl  dfsForDivision(ActivityImpl activityImpl,List<String> searchList,Stack<ActivityImpl> stack){
		if(!searchList.contains(activityImpl.getId())){
			searchList.add(activityImpl.getId());
			
			if("parallelGateway".equals(activityImpl.getProperty("type"))){
				int in=activityImpl.getIncomingTransitions().size();
				int out=activityImpl.getOutgoingTransitions().size();
				if(in>1 && out>1){
					throw new  ActivitiException("不支持该类型的网关回退");
				}
				
				//网关栈中弹出栈顶元素
				ActivityImpl topAct=stack.pop();
				if(topAct.getIncomingTransitions().size()!=out){//不是一对配置的网关
					stack.push(topAct);//刚刚弹出的栈顶网关重新入栈
					stack.push(activityImpl);
				}else{//出入线个数相同，是一对匹配的网关
					if(stack.isEmpty()){//找到
						return activityImpl;
					}
				}
			}
			
			for (PvmTransition outPvm : activityImpl.getIncomingTransitions()) {
				ActivityImpl taget=(ActivityImpl)outPvm.getSource();
				ActivityImpl result=dfsForDivision(taget,searchList,stack);
				if(result!=null) return result; 
			}

		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//深度优先搜索
	public ActivityImpl dfs(ActivityImpl start){
		ActivityImpl result=null;
		List<String> searchList=new ArrayList<String>();//保存已经走过的节点
		Stack<ActivityImpl> stack=new Stack<ActivityImpl>();//保存搜索过程中网关
		searchList.add(start.getId());
		stack.push(start);
		//开始节点所有出线深度搜索
		for (PvmTransition outPvm : start.getOutgoingTransitions()) {
			ActivityImpl taget=(ActivityImpl)outPvm.getDestination();
			result=dfs(taget,searchList,stack);
			if(result!=null) return result; 
		}
		return result;
		
	}
	
	private ActivityImpl  dfs(ActivityImpl activityImpl,List<String> searchList,Stack<ActivityImpl> stack){
		if(!searchList.contains(activityImpl.getId())){
			System.out.print(activityImpl.getId()+"-->");
			searchList.add(activityImpl.getId());
			
			if("parallelGateway".equals(activityImpl.getProperty("type"))){
				int in=activityImpl.getIncomingTransitions().size();
				int out=activityImpl.getOutgoingTransitions().size();
				if(in>1 && out>1){
					throw new  ActivitiException("不支持该类型的网关回退");
				}
				
				//网关栈中弹出栈顶元素
				ActivityImpl topAct=stack.pop();
				if(topAct.getOutgoingTransitions().size()!=in){//不是一对配置的网关
					stack.push(topAct);//刚刚弹出的栈顶网关重新入栈
					stack.push(activityImpl);
				}else{//出入线个数相同，是一对匹配的网关
					if(stack.isEmpty()){//找到
						return activityImpl;
					}
				}
			}
			
			for (PvmTransition outPvm : activityImpl.getOutgoingTransitions()) {
				ActivityImpl taget=(ActivityImpl)outPvm.getDestination();
				ActivityImpl result=dfs(taget,searchList,stack);
				if(result!=null) return result; 
			}

		}
		return null;
	}
	
	
}
