package com.bosssoft.install.activiti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.cache.CacheHelper;
import org.activiti.engine.impl.db.PersistentObject;

import redis.clients.jedis.Jedis;

public class JedisTest {
	
	public  static Jedis	jedis;
 
	public static void main(String[] args){
		jedis = new Jedis ("127.0.0.1",Integer.valueOf("6379")); 
		 jedis.connect();
		
		
		Map<Class<? extends PersistentObject>,String> entityCacheNameMap=CacheHelper.getEntityCacheNameMap();
		for (Entry<Class<? extends PersistentObject>, String> entry : entityCacheNameMap.entrySet()) {
			System.out.println("#####实体缓存: "+entry.getValue());
			Map<String, PersistentObject> entityMap=	getEntityCacheItemByCacheName(entry.getValue());
			for (Entry<String, PersistentObject> entity: entityMap.entrySet()) {
				System.out.println(entity.getKey()+"  "+entity.getValue());
			}
			
			List<String> fieldList=CacheHelper.getIndexFieldNameList(entry.getKey());
			for (String field : fieldList) {
				System.out.println("  索引缓存   "+field);
				Map<String, List<String>> result=getIndexCacheItemByCacheName(CacheHelper.getIndexCacheName(entry.getKey(), field));
				System.out.println(result);
			}
			
			System.out.println("//////////////////////////////////////");
		}

	}
	
   public static  Map<String, PersistentObject> getEntityCacheItemByCacheName(String cacheName) {
		
        Map<byte[], byte[]> byteResult=jedis.hgetAll(serialize(cacheName));
		
		Map<String,PersistentObject> result=new HashMap<String, PersistentObject>();
		for (Entry<byte[], byte[]> entry : byteResult.entrySet()) {
			result.put((String)unserialize(entry.getKey()), (PersistentObject)unserialize(entry.getValue()));
		}
		return result;
	}
   
   
   public static Map<String, List<String>> getIndexCacheItemByCacheName(String cacheName) {
		Map<byte[], byte[]> byteResult=jedis.hgetAll(serialize(cacheName));
		
		Map<String,List<String>> result=new HashMap<String, List<String>>();
		for (Entry<byte[], byte[]> entry : byteResult.entrySet()) {
			result.put((String)unserialize(entry.getKey()), (List<String>)unserialize(entry.getValue()));
		}
		return result;
	}
	
	public static byte[] serialize(Object object) {

		ObjectOutputStream oos = null;

		ByteArrayOutputStream baos = null;

		try {

			// 序列化

			baos = new ByteArrayOutputStream();

			oos = new ObjectOutputStream(baos);

			oos.writeObject(object);

			byte[] bytes = baos.toByteArray();

			return bytes;

		} catch (Exception e) {

		}

		return null;

	}

	public static Object unserialize(byte[] bytes) {

		ByteArrayInputStream bais = null;

		try {

			// 反序列化

			bais = new ByteArrayInputStream(bytes);

			ObjectInputStream ois = new ObjectInputStream(bais);

			return ois.readObject();

		} catch (Exception e) {

		}

		return null;

	}
}
