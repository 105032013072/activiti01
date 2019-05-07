package com.bosssoft.install.activiti;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskBackEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.WithdrawEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OnceDoneTaskEventListener implements ActivitiEventListener {

	private static Logger logger = LoggerFactory.getLogger(OnceDoneTaskEventListener.class);

    private RepositoryService repositoryService;
    private HistoryService historyService;
    private ManagementService managementService;
	
	public void onEvent(ActivitiEvent event) {
		repositoryService = event.getEngineServices().getRepositoryService();
        historyService = event.getEngineServices().getHistoryService();
        managementService = event.getEngineServices().getManagementService();

        ActivitiEventType eventType = event.getType();
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        Object entity = entityEvent.getEntity();

        switch (eventType) {
            case TASK_COMPLETED:
                if (entity instanceof TaskEntity) {
                    TaskEntity task = (TaskEntity) entity;
                    ProcessDefinitionEntity processDef = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                            repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
                    ActivityImpl startActivityImpl = processDef.findActivity(task.getTaskDefinitionKey());
                    Set<ActivityImpl> userTaskSet = new HashSet<ActivityImpl>();
                    findIncomingUserTask(userTaskSet, startActivityImpl);
                    for (ActivityImpl prevActivityImpl : userTaskSet) {
                        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                                .orderByHistoricTaskInstanceEndTime().desc().finished();
                        historicTaskInstanceQuery.processInstanceId(task.getProcessInstanceId());
                        historicTaskInstanceQuery.taskDefinitionKey(prevActivityImpl.getId());
                        List<HistoricTaskInstance> historicTaskInstanceList = historicTaskInstanceQuery.listPage(0, 1);
                        if (CollectionUtils.isNotEmpty(historicTaskInstanceList)) {
                            updateHisTaskOfExt1(historicTaskInstanceList.get(0).getId(), "ONCE_DONE_TASK");
                        }
                    }
                    boolean endEvent = isEndEvent(startActivityImpl);
                    if (endEvent) {//流程结束所有任务都是曾经办
                        updateHisTaskOfExt1(task.getId(), "ONCE_DONE_TASK");
                    }
                }
                break;
            case TASK_BACKED:
                if (entity instanceof TaskBackEntity) {
                    TaskBackEntity task = (TaskBackEntity) entity;
                    List<Task> backTargets = task.getBackTargets();
                    for (Task targetTask : backTargets) {
                        ProcessDefinitionEntity processDef = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                                repositoryService).getDeployedProcessDefinition(targetTask.getProcessDefinitionId());
                        ActivityImpl startActivityImpl = processDef.findActivity(targetTask.getTaskDefinitionKey());
                        Set<ActivityImpl> userTaskSet = new HashSet<ActivityImpl>();
                        findIncomingUserTask(userTaskSet, startActivityImpl);
                        for (ActivityImpl prevActivityImpl : userTaskSet) {
                            HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().finished();
                            historicTaskInstanceQuery.processInstanceId(targetTask.getProcessInstanceId());
                            historicTaskInstanceQuery.taskDefinitionKey(prevActivityImpl.getId());
                            HistoricTaskInstance historicTaskInstance = historicTaskInstanceQuery.singleResult();
                            if (historicTaskInstance != null) {
                                updateHisTaskOfExt1(historicTaskInstance.getId(), null);
                            }
                        }
                    }
                }
                break;
            case TASK_WITHDRAWED:
                if (entity instanceof WithdrawEntity) {
                    WithdrawEntity task = (WithdrawEntity) entity;
                    HistoricTaskInstance taskInstance = task.getWithdrawTaskInstance();
                    ProcessDefinitionEntity processDef = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                            repositoryService).getDeployedProcessDefinition(taskInstance.getProcessDefinitionId());
                    ActivityImpl startActivityImpl = processDef.findActivity(taskInstance.getTaskDefinitionKey());
                    Set<ActivityImpl> userTaskSet = new HashSet<ActivityImpl>();
                    findIncomingUserTask(userTaskSet, startActivityImpl);
                    for (ActivityImpl prevActivityImpl : userTaskSet) {
                        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().finished();
                        historicTaskInstanceQuery.taskDefinitionKey(prevActivityImpl.getId());
                        historicTaskInstanceQuery.processInstanceId(taskInstance.getProcessInstanceId());
                     //   HistoricTaskInstance historicTaskInstance = historicTaskInstanceQuery.singleResult();
                        HistoricTaskInstance historicTaskInstance = null;
                        List<HistoricTaskInstance> list=  historicTaskInstanceQuery.list();
                        if(CollectionUtils.isNotEmpty(list)){
                        	historicTaskInstance=list.get(0);
                        }
                        if (historicTaskInstance != null) {
                            updateHisTaskOfExt1(historicTaskInstance.getId(), null);
                        }
                    }
                }
                break;
        }
    }

    private void updateHisTaskOfExt1(final String taskId, final String ext1) {
       /* managementService.executeCustomSql(
                new AbstractCustomSqlExecution<TaskCustomMapper, Integer>(TaskCustomMapper.class) {
                    @Override
                    public Integer execute(TaskCustomMapper myCustomMapper) {
                        return myCustomMapper.updateHisTaskOfExt1(ext1, taskId);
                    }
                });*/
    }

    //获取当前节点前驱的用户任务节点
    private void findIncomingUserTask(Set<ActivityImpl> actIdSet, ActivityImpl startActivityImpl) {
        List<PvmTransition> transitionList = startActivityImpl.getIncomingTransitions();
        for (PvmTransition pvmTransition : transitionList) {
            ActivityImpl nearActivityImpl = (ActivityImpl) pvmTransition.getSource();
            String type = (String) nearActivityImpl.getProperty("type");
            if ("userTask".equals(type)) {
                actIdSet.add(nearActivityImpl);
                continue;
            } else if ("startEvent".equals(type) || "endEvent".equals(type)) {
                continue;
            } else {
                findIncomingUserTask(actIdSet, nearActivityImpl);
            }
        }
    }

    //判断后继节点是否是结束节点
    private boolean isEndEvent(ActivityImpl startActivityImpl) {
        List<PvmTransition> transitionList = startActivityImpl.getOutgoingTransitions();
        if (transitionList.size() == 1) {
            PvmTransition pvmTransition = transitionList.get(0);
            ActivityImpl nearActivityImpl = (ActivityImpl) pvmTransition.getDestination();
            String type = (String) nearActivityImpl.getProperty("type");
            if ("endEvent".equals(type)) {
                return true;
            }
        }
        return false;
    }

	public boolean isFailOnException() {
		
		return false;
	}

    
}

