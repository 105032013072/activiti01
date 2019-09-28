package com.bosssoft.install.activiti;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;


public class ProtostuffUtil {
	 /*private static Logger logger = LoggerFactory.getLogger(ProtostuffUtil.class);
	
	 *//**
     * 需要使用包装类进行序列化/反序列化的class集合
     *//*
    private static final Set<Class<?>> WRAPPER_SET = new HashSet<Class<?>>();
 
    *//**
     * 序列化/反序列化包装类 Class 对象
     *//*
    private static final Class<SerializeDeserializeWrapper> WRAPPER_CLASS = SerializeDeserializeWrapper.class;
 
    *//**
     * 序列化/反序列化包装类 Schema 对象
     *//*
    private static final Schema<SerializeDeserializeWrapper> WRAPPER_SCHEMA = RuntimeSchema.createFrom(WRAPPER_CLASS);*/
 
   
 
    /**
     * 预定义一些Protostuff无法直接序列化/反序列化的对象
     */
    /*static {
        WRAPPER_SET.add(List.class);
        WRAPPER_SET.add(ArrayList.class);
        WRAPPER_SET.add(CopyOnWriteArrayList.class);
        WRAPPER_SET.add(LinkedList.class);
        WRAPPER_SET.add(Stack.class);
        WRAPPER_SET.add(Vector.class);
 
        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(HashMap.class);
        WRAPPER_SET.add(TreeMap.class);
        WRAPPER_SET.add(Hashtable.class);
        WRAPPER_SET.add(SortedMap.class);
        WRAPPER_SET.add(Map.class);
 
        WRAPPER_SET.add(Persion.class);
    }*/
 
    /**
     * 注册需要使用包装类进行序列化/反序列化的 Class 对象
     *
     * @param clazz 需要包装的类型 Class 对象
     */
   /* public static void registerWrapperClass(Class clazz) {
    	 WRAPPER_SET.add(clazz);
    }*/
       

	
	
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();
    
    private static Objenesis objenesis = new ObjenesisStd(true);

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    /**
     * 将对象序列化
     * @param obj 对象
     * @return
     */
   /* public static <T> byte[] serializer(T obj) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
        	System.out.println(e.toString());
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }*/
    
    @SuppressWarnings("unchecked")
    public static <T> String serializeToString(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return new String(ProtobufIOUtil.toByteArray(obj, schema, buffer), "ISO8859-1");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }
 
    public static <T> T deserializeFromString(String data, Class<T> cls) {
        try {
            T message = (T) objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtobufIOUtil.mergeFrom(data.getBytes("ISO8859-1"), message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
     
 
    @SuppressWarnings("unchecked")
    public static <T> byte[] serializeToByte(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
      //  LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        LinkedBuffer buffer = LinkedBuffer.allocate(2048);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }
 
    public static <T> T deserializeFromByte(byte[] data, Class<T> cls) {
        try {
            T message = (T) objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtobufIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
   
}
