package com.bosssoft.install.activiti;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.junit.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import redis.clients.jedis.Jedis;

public class KyroSerializable extends BaseTest{
	
	@Test
	public  void kyroTest() throws Exception {
		Jedis jedis=new Jedis("127.0.0.1",6379 );
		jedis.connect();
		
		
		String key="direct_pay_voucher:2:155006";
		Long t1=System.currentTimeMillis();
		//ProcessDefinitionEntity	result=(ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(key).singleResult();
		ProcessDefinitionEntity	result=(ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition("direct_pay_voucher:2:155006");
		System.out.println("==时间："+(System.currentTimeMillis()-t1));
		//Persion result=new Persion("name");
		/*Map<String,String> result=new HashMap<String, String>();
		result.put("key1", key);*/
	//jedis.set(key, serialize(result));
		//jedis.set(key.getBytes(), ProtostuffUtil.serializeToByte(result));
		jedis.set(key.getBytes(), FSTSerial.serialize(result));
	}
	
	
	@Test
	public void StringGetTest(){
		Jedis jedis=new Jedis("127.0.0.1",6379 );
		jedis.connect();
		
		String key="direct_pay_voucher:2:155006";

		Long t1=System.currentTimeMillis();
		//ProcessDefinitionEntity	result=deserialize(jedis.get(key), ProcessDefinitionEntity.class);
		ProcessDefinitionEntity	result=(ProcessDefinitionEntity) FSTSerial.unserialize(jedis.get(key.getBytes()));
		System.out.println("==时间："+(System.currentTimeMillis()-t1));
		System.out.println(result.getProcessDefinition());
		//print(result);
	}
	
	
	 public  byte[] serialize(Object obj) throws Exception {
		 
		    Kryo kryo = new Kryo();  
	        kryo.setReferences(false);  
	        kryo.setRegistrationRequired(false);  
	        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());  
	        kryo.register(obj.getClass()); 
		 
	        ByteArrayOutputStream out = null;
	        Output output = null;
	        try {
	            out = new ByteArrayOutputStream();
	            output = new Output(out, 1024);
	            kryo.writeClassAndObject(output, obj);
	            return output.toBytes();
	        } catch (Exception e) {
	            throw new Exception(e);
	        } finally {
	            if (null != out) {
	                try {
	                    out.close();
	                    out = null;
	                } catch (IOException e) {
	                }
	            }
	            if (null != output) {
	                output.close();
	                output = null;
	            }
	        }
	    }
	 
	    public  Object deserialize(byte[] bytes) throws Exception {
	    	Kryo kryo = new Kryo();  
	        kryo.setReferences(false);  
	        kryo.setRegistrationRequired(false);  
	        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());  


	    	
	    	Input input = null;
	        try {
	            input = new Input(bytes, 0, 1024);
	            return kryo.readClassAndObject(input);
	        } catch (Exception e) {
	            throw new Exception(e);
	        } finally {
	            if (null != input) {
	                input.close();
	                input = null;
	            }
	        }
	    }
	

	/*public static void setSerializableObject(Object object) throws Exception{  
		  
        Kryo kryo = new Kryo();  
        kryo.setReferences(false);  
        kryo.setRegistrationRequired(false);  
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());  
        kryo.register(object.getClass());  
        Output output = new Output(new FileOutputStream("D:/file1.bin"));  
        kryo.write
        for (int i = 0; i < 100000; i++) {  
            Map<String,Integer> map = new HashMap<String, Integer>(2);  
            map.put("zhang0", i);  
            map.put("zhang1", i);  
            kryo.writeObject(output, new Simple("zhang"+i,(i+1),map));  
        }  
        output.flush();  
        output.close();  
    }  
  
  
    public static void getSerializableObject(){  
        Kryo kryo = new Kryo();  
        kryo.setReferences(false);  
        kryo.setRegistrationRequired(false);  
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());  
        Input input;  
        try {  
            input = new Input(new FileInputStream("D:/file1.bin"));  
            Simple simple =null;  
            while((simple=kryo.readObject(input, Simple.class)) != null){  
                //System.out.println(simple.getAge() + "  " + simple.getName() + "  " + simple.getMap().toString());  
            }  
  
            input.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch(KryoException e){  
  
        }  
    } */
}
