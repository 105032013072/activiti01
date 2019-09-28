package com.bosssoft.install.activiti;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.spi.notification.ChannelType;
import org.activiti.engine.spi.notification.NotificationContext;
import org.activiti.engine.spi.notification.NotificationListener;
import org.activiti.engine.spi.notification.NotificationType;
import org.activiti.engine.spi.notification.RemindType;
import org.activiti.engine.spi.notification.TimeEffectiveContext;
import org.activiti.engine.spi.notification.event.NotificationCategory;
import org.activiti.engine.spi.notification.event.NotificationEvent;
import org.activiti.engine.spi.notification.event.NotificationEventContext;

public class OvertimeForTask implements NotificationListener{

	public ChannelType channelType() {
		
		return ChannelType.CUSTOM;
	}

	public void execute(NotificationEvent noticeEvent) {
		NotificationEventContext context=noticeEvent.getNotificationEventContext();
		StringBuilder title=new StringBuilder("流程邮件通知:");
		StringBuilder body=new StringBuilder();
		
		String notifyType="";
		
		if(NotificationCategory.TASKNOTIFICATION.equals(noticeEvent.getNotificationCategory())){
			NotificationContext notificationContext=(NotificationContext)context;
			notifyType=NotificationType.getNotificationName(notificationContext.getNotificationType());
			
		}else if(NotificationCategory.TIMEEFFECTIVEREMID.equals(noticeEvent.getNotificationCategory())){
			TimeEffectiveContext timeEffectiveContext=(TimeEffectiveContext)context;
		
			if(RemindType.TASK.equals(timeEffectiveContext.getType())){
				notifyType="任务超时提醒";
			}else if(RemindType.PROCESS.equals(timeEffectiveContext.getType())){
				notifyType="流程超时提醒";
			}
			
			body.append("过期时间:").append(dateToStr(timeEffectiveContext.getExpiryTime()));
			
		}
		
		title.append("[").append(notifyType).append("]").append(context.getProcessName()).append("-").append(context.getTaskName());
		
		body.append("任务ID    :").append(context.getTaskId()).append("\n");
		body.append("任务名称    :").append(context.getTaskName()).append("\n");
		body.append("流程实例ID:").append(context.getProcessInstanceId()).append("\n");
		body.append("流程名称    :").append(context.getProcessName()).append("\n");
		body.append("业务Key   :").append(context.getBusinessKey()).append("\n");
		body.append("\n");
		body.append("------------------------本邮件无需回复---------------------");
		
	}
	
	private String dateToStr(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

}
