package com.bosssoft.install.activiti;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class ObjectUtil {
	/** 
     * 字节数组转换成对象 
     * @param bytes  
     * @return Object 取得结果后强制转换成你存入的对象类型 
     */  
    public static Object ByteToObject(byte[] bytes){  
        java.lang.Object obj = null;  
        try {  
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);  
        ObjectInputStream oi = new ObjectInputStream(bi);  
  
        obj = oi.readObject();  
  
        bi.close();  
        oi.close();  
        }  
        catch(Exception e) {  
            System.out.println("translation"+e.getMessage());  
            e.printStackTrace();  
        }  
        return obj;  
    }  
}
