package com.bosssoft.install.activiti;

import com.bosssoft.platform.microservice.common.fse.ByteArray;
import com.bosssoft.platform.microservice.common.fse.Fse;

public class Util {

	public  static byte[] serializeObject(Object object) {
		Fse fse = new Fse();
		ByteArray byteArray = ByteArray.allocate();
		fse.serialize(object, byteArray);
		return byteArray.toArray();
	}

	public static Object deserializeObject(byte[] array) {
		Fse fse = new Fse();
		ByteArray byteArray = ByteArray.allocate();
		byteArray.put(array);
		return fse.deSerialize(byteArray);
	}
}
