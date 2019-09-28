package com.bosssoft.install.activiti;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.nustaq.serialization.FSTConfiguration;

public class FSTSerial {
	static FSTConfiguration configuration = FSTConfiguration
		    // .createDefaultConfiguration();
		            .createStructConfiguration();
	
	public static byte[] serialize(Object obj) {
		configuration.registerClass(ProcessDefinitionEntity.class);
        return configuration.asByteArray(obj);
    }
 
    public static Object unserialize(byte[] sec) {
    	//configuration.setClassLoader(ProcessDefinitionEntity.class.getClassLoader());
    	configuration.registerClass(ProcessDefinitionEntity.class);
        return configuration.asObject(sec);
    }
}
