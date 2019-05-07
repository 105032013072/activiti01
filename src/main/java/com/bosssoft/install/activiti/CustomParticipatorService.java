package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.Participator;

public class CustomParticipatorService {

	public Participator getAssignee(){

		   Participator applicant=new Participator("u01", "小王", IdentityEnum.USER, null);
		   return applicant;
	 }
	
	
	public List<Participator> getAssignee(String arg1,Map<String,String> map){
		  System.out.println(arg1);
		  System.out.println(map);
		  Participator applicant=new Participator("u01", "小王", IdentityEnum.USER, null);
		  List<Participator> result=new ArrayList<Participator>();
		  result.add(applicant);
		  
		  return result;
	 }
}
