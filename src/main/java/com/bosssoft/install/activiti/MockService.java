package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;

public class MockService {
    
	private   Map<String, Participator> participatorMap=new LinkedHashMap<String, Participator>();
	
	private  Map<String,List<String>>  roleRelationMap=new HashMap<String, List<String>>();//角色-用户关系
	
	private  Map<String,List<String>> positionRelationMap=new HashMap<String, List<String>>();//岗位-用户关系
	
	private  Map<String,List<String>> orgRelationMap=new HashMap<String, List<String>>();//机构-用户关系
	
	
	public MockService(){
		initParticipatorMap();
		initOrgRelationMap();
		initPositionRelationMap();
		initRoleRelationMap();
	}

	//岗位-用户关系
	private void initPositionRelationMap() {
		List<String> positionListOne=new ArrayList<String>();
		positionListOne.add("u01");
		positionListOne.add("u02");
		positionListOne.add("u03");
		positionRelationMap.put("p01", positionListOne);
		
		List<String> positionListTwo=new ArrayList<String>();
		positionListTwo.add("u04");
		positionListTwo.add("u05");
		positionRelationMap.put("p02", positionListTwo);
		
		
		List<String> positionListThree=new ArrayList<String>();
		positionListThree.add("u06");
		positionListThree.add("u07");
		positionListThree.add("u08");
		positionRelationMap.put("p03", positionListThree);
		
		List<String> positionListFour=new ArrayList<String>();
		positionListFour.add("u09");
		positionListFour.add("u10");
		positionRelationMap.put("p04", positionListFour);
		
	}


	//机构-用户关系
	private void initOrgRelationMap() {
		List<String> orgRelationListOne=new ArrayList<String>();
		orgRelationListOne.add("u01");
		orgRelationListOne.add("u02");
		orgRelationListOne.add("u03");
		orgRelationListOne.add("u04");
		orgRelationListOne.add("u05");
		orgRelationMap.put("o01", orgRelationListOne);
		
		List<String> orgRelationListTwo=new ArrayList<String>();
		orgRelationListTwo.add("u06");
		orgRelationListTwo.add("u07");
		orgRelationListTwo.add("u08");
		orgRelationListTwo.add("u09");
		orgRelationListTwo.add("u10");
		orgRelationMap.put("o02", orgRelationListTwo);
		
		
	}


	private void initRoleRelationMap() {
		List<String> roleRelationListOne=new ArrayList<String>();
		roleRelationListOne.add("u01");
		roleRelationListOne.add("u06");
		roleRelationMap.put("r01", roleRelationListOne);
		
		List<String> roleRelationListTwo=new ArrayList<String>();
		roleRelationListTwo.add("u04");
		roleRelationListTwo.add("u02");
		roleRelationListTwo.add("u05");
		roleRelationMap.put("r02", roleRelationListTwo);
		
		List<String> roleRelationListThree=new ArrayList<String>();
		roleRelationListThree.add("u01");
		roleRelationListThree.add("u03");
		roleRelationMap.put("r03", roleRelationListThree);
		
		List<String> roleRelationListFour=new ArrayList<String>();
		roleRelationListFour.add("u06");
		roleRelationListFour.add("u07");
		roleRelationListFour.add("u08");
		roleRelationMap.put("r04", roleRelationListFour);
		
		List<String> roleRelationListFive=new ArrayList<String>();
		roleRelationListFive.add("u09");
		roleRelationListFive.add("u10");
		roleRelationMap.put("r05", roleRelationListFour);
	}


	private void initParticipatorMap() {
		Participator u1=new Participator("u01", "小明", IdentityEnum.USER, null);
		Participator u2=new Participator("u02", "小雨", IdentityEnum.USER, null);
		Participator u3=new Participator("u03", "小王", IdentityEnum.USER, null);
		Participator u4=new Participator("u04", "张三", IdentityEnum.USER, null);
		Participator u5=new Participator("u05", "李四", IdentityEnum.USER, null);
		
		Participator u6=new Participator("u06", "王五", IdentityEnum.USER, null);
		Participator u7=new Participator("u07", "小丁", IdentityEnum.USER, null);
		Participator u8=new Participator("u08", "小红", IdentityEnum.USER, null);
		Participator u9=new Participator("u09", "丁丁", IdentityEnum.USER, null);
		Participator u10=new Participator("u10", "小雪", IdentityEnum.USER, null);
		participatorMap.put(u1.getParticipatorId(), u1);
		participatorMap.put(u2.getParticipatorId(), u2);
		participatorMap.put(u3.getParticipatorId(), u3);
		participatorMap.put(u4.getParticipatorId(), u4);
		participatorMap.put(u5.getParticipatorId(), u5);
		participatorMap.put(u6.getParticipatorId(), u6);
		participatorMap.put(u7.getParticipatorId(), u7);
		participatorMap.put(u8.getParticipatorId(), u8);
		participatorMap.put(u9.getParticipatorId(), u9);
		participatorMap.put(u10.getParticipatorId(), u10);
		
		
		
		//角色
		Participator r1=new Participator("r01", "部门经理", IdentityEnum.ROLE, null); 
		Participator r2=new Participator("r02", "助理", IdentityEnum.ROLE, null);
		Participator r3=new Participator("r03", "高级会计", IdentityEnum.ROLE, null);
		Participator r4=new Participator("r04", "高级开发工程师", IdentityEnum.ROLE, null);
		Participator r5=new Participator("r05", "高级测试工程师", IdentityEnum.ROLE, null);
		participatorMap.put(r1.getParticipatorId(), r1);
		participatorMap.put(r2.getParticipatorId(), r2);
		participatorMap.put(r3.getParticipatorId(), r3);
		participatorMap.put(r4.getParticipatorId(), r4);
		participatorMap.put(r5.getParticipatorId(), r5);
		
		//岗位
		Participator p1=new Participator("p01", "会计", IdentityEnum.POSITION, null);
		Participator p2=new Participator("p02", "商务", IdentityEnum.POSITION, null);
		Participator p3=new Participator("p03", "研发", IdentityEnum.POSITION, null);
		Participator p4=new Participator("p04", "测试", IdentityEnum.POSITION, null);
		participatorMap.put(p1.getParticipatorId(), p1);
		participatorMap.put(p2.getParticipatorId(), p2);
		participatorMap.put(p3.getParticipatorId(), p3);
		participatorMap.put(p4.getParticipatorId(), p4);
		
		//机构
		Participator o1=new Participator("o01", "财务部", IdentityEnum.ORGANIZATION, null);
		Participator o2=new Participator("o02", "研发中心", IdentityEnum.ORGANIZATION, null);
		participatorMap.put(o1.getParticipatorId(), o1);
		participatorMap.put(o2.getParticipatorId(), o2);
	}


	public Map<String, Participator> getParticipatorMap() {
		return participatorMap;
	}


	public void setParticipatorMap(Map<String, Participator> participatorMap) {
		this.participatorMap = participatorMap;
	}


	public Map<String, List<String>> getRoleRelationMap() {
		return roleRelationMap;
	}


	public void setRoleRelationMap(Map<String, List<String>> roleRelationMap) {
		this.roleRelationMap = roleRelationMap;
	}


	public Map<String, List<String>> getPositionRelationMap() {
		return positionRelationMap;
	}


	public void setPositionRelationMap(Map<String, List<String>> positionRelationMap) {
		this.positionRelationMap = positionRelationMap;
	}


	public Map<String, List<String>> getOrgRelationMap() {
		return orgRelationMap;
	}


	public void setOrgRelationMap(Map<String, List<String>> orgRelationMap) {
		this.orgRelationMap = orgRelationMap;
	}


	
	
}
