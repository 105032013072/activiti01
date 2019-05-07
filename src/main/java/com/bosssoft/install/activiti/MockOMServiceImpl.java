package com.bosssoft.install.activiti;


import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.OMService;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.identity.ParticipatorDescriptor;

import com.bosssoft.platform.common.lang.data.Page;
import com.bosssoft.platform.common.lang.data.Searcher;

public class MockOMServiceImpl implements OMService {


	//@Override
	public Participator getParticipatorInfo(String participatorId, IdentityEnum identityEnum) {
		Participator participator=new Participator();
		String name=participatorId.substring(participatorId.length()-1, participatorId.length());
		participator.setParticipatorId(participatorId);
		participator.setParticipatorName("用户"+name);
		participator.setParticipatorType(identityEnum.USER);
		return participator;
	}

	//@Override
	public Page<Participator> getUsers(Page page, Searcher searcher) {
		Page<Participator> participatorPage = new Page<Participator>();
		int total = 1000;
		for(int i=0;i<total;i++){
			Participator participator = new Participator();
			participator.setParticipatorName("参与者"+i);
			participator.setParticipatorId("user"+i);
			participator.setParticipatorType(IdentityEnum.USER);
			participatorPage.add(participator);
		}
		participatorPage.setPageSize(10);
		participatorPage.setPageNum(0);
		participatorPage.setTotal(total);
		return participatorPage;
	}

	//@Override
	public Page<Participator> getUsersByParticipator(Page page, String s, IdentityEnum identityEnum) {
		Page<Participator> resultPage=new Page<Participator>(page.getPageNum(), page.getPageSize());
		for(int i=0;i<1000;i++){
			Participator p=new Participator("user"+i, "用户"+i, IdentityEnum.USER, null);
			resultPage.add(p);
		}
		resultPage.setTotal(10);
		return resultPage;
	}

	//@Override
	public List<ParticipatorDescriptor> getParticipatorTypes() {
		List<ParticipatorDescriptor> list = new ArrayList<ParticipatorDescriptor>();
		list.add(new ParticipatorDescriptor("候选人", IdentityEnum.USER.toString()));
		list.add(new ParticipatorDescriptor("候选机构", IdentityEnum.ORGANIZATION.toString()));
		list.add(new ParticipatorDescriptor("候选岗位", IdentityEnum.POSITION.toString()));
		list.add(new ParticipatorDescriptor("候选角色", IdentityEnum.ROLE.toString()));
		return list;
	}

	//@Override
	public Page<Participator> getParticipatorList(Page<Participator> page , Searcher searcher) {
		Page<Participator> participatorPage = new Page<Participator>();
		int total = 1000;
		for(int i=0;i<total;i++){
			Participator participator = new Participator();
			participator.setParticipatorName("搜索用户"+i);
			participator.setParticipatorId("user"+i);
			participator.setParticipatorType(IdentityEnum.USER);
			participatorPage.add(participator);
		}
		participatorPage.setPageSize(10);
		participatorPage.setPageNum(0);
		participatorPage.setTotal(total);
		return participatorPage;
	}

	//@Override
	public List<Participator> getCandidateTree(String participatorId, Page page) {
		List<Participator> list = new ArrayList<Participator>();
		if(participatorId==null){
			for(int i=0;i<1000;i++){
				Participator p = new Participator();
				list.add(p);
				p.setParticipatorType(IdentityEnum.ORGANIZATION);
				p.setParticipatorId("o"+i);
				p.setParticipatorName("机构"+i);
				p.setParentParticipatorId("0");
				p.setIsParent(true);
			}
			return list;
		}
		int index = 0;
		if(participatorId.equals("o0")){
			index = 0;

			for(int i=index;i<index+3;i++){
				Participator p = new Participator();
				list.add(p);
				p.setParticipatorType(IdentityEnum.ROLE);
				p.setParticipatorId("p"+i);
				p.setParticipatorName("岗位"+i);
				p.setParentParticipatorId(participatorId);
			}
		}else if(participatorId.equals("o1")){
			index = 10;

			for(int i=index;i<index+3;i++){
				Participator p = new Participator();
				list.add(p);
				p.setParticipatorType(IdentityEnum.POSITION);
				p.setParticipatorId("p"+i);
				p.setParticipatorName("岗位"+i);
				p.setParentParticipatorId(participatorId);
			}
		}
		return list;
	}


	//@Override
	public List<String> getIdentityGroupsOfUser(String userId, IdentityEnum type) {
		List<String> list = new ArrayList<String>();
		list.add("test");
		return list;
	}

}
