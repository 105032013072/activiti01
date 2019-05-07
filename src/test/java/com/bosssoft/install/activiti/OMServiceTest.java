package com.bosssoft.install.activiti;

import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;
import org.junit.Test;

import com.bosssoft.platform.common.lang.data.Page;
import com.bosssoft.platform.common.lang.data.Searcher;

public class OMServiceTest {
	private DefaultOMServiceImpl omservice=new DefaultOMServiceImpl();
    
	private Page<Participator> page=new Page<Participator>(1, 15);
	
	@Test
	public void getName(){
		System.out.println(omservice.getParticipatorName("u10", IdentityEnum.USER));
	}
	
	@Test
	public void getUsers(){
		List<Participator> list=omservice.getUsers(page, new Searcher()).getResult();
		for (Participator participator : list) {
			System.out.println(participator.getParticipatorId()+"   "+participator.getParticipatorName());
		}
	}
	
	@Test
	public void getUsersByParticipator(){
		List<Participator> list=omservice.getUsersByParticipator(page, "r03", IdentityEnum.ROLE).getResult();
		for (Participator participator : list) {
			System.out.println(participator.getParticipatorId()+"   "+participator.getParticipatorName());
		}
	}
	
	@Test
	public void th(){
		Page<Participator> page=new Page<Participator>();
		page.setPageSizeZero(true);
		List<Participator> users=omservice
				                 .getUsersByParticipator(page, "r01", IdentityEnum.ROLE)
				                 .getResult();
		for (Participator participator : users) {
			System.out.println(participator.getParticipatorId());
		}
		
	}
}
