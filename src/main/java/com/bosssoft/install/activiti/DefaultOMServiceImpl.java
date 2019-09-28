package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.OMService;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.identity.ParticipatorDescriptor;

import com.bosssoft.platform.common.lang.data.Page;
import com.bosssoft.platform.common.lang.data.Searcher;

public class DefaultOMServiceImpl implements OMService {

	private MockService mockService=new MockService();


	public String getParticipatorName(String participatorId, IdentityEnum participatorType) {
		Participator p=mockService.getParticipatorMap().get(participatorId);
		if(p!=null) return p.getParticipatorName();
		else return null;
	}

	


	public Page<Participator> getUsers(Page page, Searcher searcher) {
		List<Participator> list=new ArrayList<Participator>();
		for (Participator participator : mockService.getParticipatorMap().values()) {
			if(IdentityEnum.USER.equals(participator.getParticipatorType())) list.add(participator);
		} 
		Page<Participator> pageResult=new Page<Participator>(page.getPageNum(), page.getPageSize());
		pageResult.setTotal(list.size());
		pageResult.addAll(list);
		
		return pageResult;
	}


	public Page<Participator> getUsersByParticipator(Page page, String participatorId, IdentityEnum participatorType) {
		List<String> userIdList=new ArrayList<String>();
		switch (participatorType) {
		case ROLE:
			userIdList=mockService.getRoleRelationMap().get(participatorId);
			break;
		case POSITION:
			userIdList=mockService.getPositionRelationMap().get(participatorId);
			break;
		case ORGANIZATION:
			userIdList=mockService.getOrgRelationMap().get(participatorId);
			break;
		}
		
		Page<Participator> resultPage=new Page<Participator>(page.getPageNum(), page.getPageSize());
		for (String userId : userIdList) {
			resultPage.add(mockService.getParticipatorMap().get(userId));
		}
		resultPage.setTotal(resultPage.getResult().size());
		return resultPage;
	}


	public List<Participator> getParticipatorList(IdentityEnum participatorType) {
		List<Participator> result=new ArrayList<Participator>();
		for (Participator participator : mockService.getParticipatorMap().values()) {
			if(participatorType.equals(participator.getParticipatorType())){
				result.add(participator);
			}
		}
		return result;
	}

	public List<Participator> getParticipatorByParentId(String participatorId, IdentityEnum participatorType) {
		List<Participator> result=new ArrayList<Participator>();
		for (Participator participator : mockService.getParticipatorMap().values()) {
			if(participatorType.equals(participator.getParticipatorType())){
				if(participatorId.equals(participator.getParentParticipatorId())){
					result.add(participator);
				}
			}
		}
		return result;
	}



	public List<Participator> getParticipatorList(String participatorType, Searcher searcher) {
	
		List<Participator> list=new ArrayList<Participator>();
		for (Participator participator : mockService.getParticipatorMap().values()) {
			list.add(participator);
		} 
		Page<Participator> participatorPage = new Page<Participator>();
		participatorPage.setTotal(list.size());
		participatorPage.addAll(list);
		
		return participatorPage;
	}




	public List<Participator> getRootParticipatorListByType() {
		List<Participator> list=new ArrayList<Participator>();
		for (Participator participator : mockService.getParticipatorMap().values()) {
			if(!IdentityEnum.USER.equals(participator.getParticipatorType())) list.add(participator);
		} 
	
		return list;
	}




	public List<Participator> getParticipatorByParentId(String participatorId, String participatorType) {
		List<Participator> list = new ArrayList<Participator>();
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

	public List<ParticipatorDescriptor> getParticipatorTypes() {
		
		List<ParticipatorDescriptor> list = new ArrayList<ParticipatorDescriptor>();
		list.add(new ParticipatorDescriptor("用户", IdentityEnum.USER.toString()));
		list.add(new ParticipatorDescriptor("机构", IdentityEnum.ORGANIZATION.toString()));
		list.add(new ParticipatorDescriptor("岗位", IdentityEnum.POSITION.toString()));
		list.add(new ParticipatorDescriptor("角色", IdentityEnum.ROLE.toString()));
		return list;
	}


	public Page<Participator> getParticipatorList(Page<Participator> page, Searcher searcher) {
		
		return null;
	}




	public List<String> getUserRoles(String userId) {
        List<String> result=new ArrayList<String>();
        
        Map<String, List<String>> map=mockService.getRoleRelationMap();
        for (Entry<String, List<String>> entry : map.entrySet()) {
			if(entry.getValue().contains(userId)) result.add(entry.getKey());
		}
		return result;
	}


	public List<String> getUserOrganizations(String userId) {
		
        List<String> result=new ArrayList<String>();
        
        Map<String, List<String>> map=mockService.getOrgRelationMap();
        for (Entry<String, List<String>> entry : map.entrySet()) {
			if(entry.getValue().contains(userId)) result.add(entry.getKey());
		}
		return result;
	}

	
	public List<String> getUserPositions(String userId) {
       List<String> result=new ArrayList<String>();
        
        Map<String, List<String>> map=mockService.getPositionRelationMap();
        for (Entry<String, List<String>> entry : map.entrySet()) {
			if(entry.getValue().contains(userId)) result.add(entry.getKey());
		}
		return result;
	}




	public List<String> getIdentityGroupsOfUser(String userId, IdentityEnum type) {
		List<String> result=new ArrayList<String>();
		
		if(IdentityEnum.ROLE.equals(type)){
			result=getUserRoles(userId);
		}else if(IdentityEnum.ORGANIZATION.equals(type)){
			result=getUserOrganizations(userId);
		}else if(IdentityEnum.POSITION.equals(type)){
			result=getUserPositions(userId);
		}
		return result;
	}




	public Participator getParticipatorInfo(String participatorId, IdentityEnum participatorType) {
		
		Participator p=mockService.getParticipatorMap().get(participatorId);
		return p;
	}




	public List<Participator> getCandidateTree(String parentId, Page page) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public Participator getOrganizationByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<Participator> getAuthorityOrganizationTree(String orgCode, boolean root) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
