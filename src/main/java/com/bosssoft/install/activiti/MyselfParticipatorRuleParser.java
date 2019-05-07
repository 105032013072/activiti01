package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.ParticipatorProperty;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.parse.ParticipatorRuleParser;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.task.IdentityLinkType;

public class MyselfParticipatorRuleParser implements ParticipatorRuleParser{

	public String getParticipatorSource() {
		
		return "myself";
	}

	public String getParticipatorType() {
		// TODO Auto-generated method stub
		return IdentityLinkType.CANDIDATE;
	}

	public String getParticipatorRuleName() {
		
		return "我自己定义的规则";
	}

	public List<Participator> parse(Expression participatorExpression, ActivityExecution execution,
			ParticipatorProperty participatorProperty) {
		List<Participator> list=new ArrayList<Participator>();
		list.add(new Participator("u04", "就是我", IdentityEnum.USER, null));
		list.add(new Participator("r05", "高级测试工程师", IdentityEnum.ROLE, null));
		return list;
	}

}
