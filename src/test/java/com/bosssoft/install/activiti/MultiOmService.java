package com.bosssoft.install.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.cfg.multitenant.TenantInfoHolder;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.OMService;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.identity.ParticipatorDescriptor;

import com.bosssoft.platform.common.lang.data.Page;
import com.bosssoft.platform.common.lang.data.Searcher;

public class MultiOmService  implements OMService{

	protected TenantInfoHolder tenantInfoHolder;
	public Map<String, OMService> omMap=new HashMap<String, OMService>();
	
	@Override
	public Participator getParticipatorInfo(String participatorId, IdentityEnum participatorType) {
		
		return null;
	}

	@Override
	public Page<Participator> getUsers(Page page, Searcher searcher) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Participator> getUsersByParticipator(Page page, String participatorId, IdentityEnum participatorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParticipatorDescriptor> getParticipatorTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Participator> getParticipatorList(Page<Participator> page, Searcher searcher) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Participator> getCandidateTree(String parentId, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIdentityGroupsOfUser(String userId, IdentityEnum type) {
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
