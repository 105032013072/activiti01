package com.bosssoft.install.activiti.multiTenantTest;

import java.util.List;

import javax.sql.DataSource;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.multitenant.MultiSchemaMultiTenantProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.entity.NotificationEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import com.bosssoft.install.activiti.multiSchemaVerification.TenantMate;
import static org.junit.Assert.*;
/**
 * 多种类型数据测试
 * @author huangxw
 *
 */
public class MultiDataBaseTypeTest extends MultiTenantBaseTest{

	protected void initTenantInfos() {
		TenantMate cacheTestTenantMate=new TenantMate();
		cacheTestTenantMate.setTenantId("cacheTest");
		cacheTestTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_cache_test?useUnicode=true&amp;characterEncoding=utf8");
		cacheTestTenantMate.setJdbcPassword("root");
		cacheTestTenantMate.setJdbcUsername("root");
		cacheTestTenantMate.setProcessClassRelativePath("multitenant/Process_cacheTest.bpmnx");
		cacheTestTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
		tenantInfos.add(cacheTestTenantMate);
		
		
		TenantMate orgTenantMate=new TenantMate();
		orgTenantMate.setTenantId("org");
		orgTenantMate.setJdbcUrl("jdbc:oracle:thin:@172.18.174.80:1521/orcl");
		orgTenantMate.setJdbcPassword("bs");
		orgTenantMate.setJdbcUsername("bpmnx");
		orgTenantMate.setProcessClassRelativePath("multitenant/Process_org.bpmnx");
		orgTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_ORACLE);
		tenantInfos.add(orgTenantMate);
		
		/*TenantMate nullTenantMate=new TenantMate();
		nullTenantMate.setTenantId(null);
		nullTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_org?useUnicode=true&amp;characterEncoding=utf8");
		nullTenantMate.setJdbcPassword("root");
		nullTenantMate.setJdbcUsername("root");
		nullTenantMate.setProcessClassRelativePath("multitenant/Process_org.bpmnx");
		nullTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
		tenantInfos.add(nullTenantMate);*/
	}
	
	/**
	 * 通过运行排序的sql 测试不同类型数据库对SqlSessionFactory的路由   启动时加载
	 */
	@Test
	public void testMutiSqlSessionFactoryOnStart(){
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_one");
		tenantStartProcessByProcessKey("org","Process_org","org_one");
		
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_two");
		tenantStartProcessByProcessKey("org","Process_org","org_two");
		
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_three");
		tenantStartProcessByProcessKey("org","Process_org","org_three");
		
		assertOrderBy("cacheTest", "cacheTest");
		assertOrderBy("org", "org");
	}
	
	
	
	@Test
   public void testMutiDbSqlSessionFactoryOnStart(){
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_one");
		tenantStartProcessByProcessKey("org","Process_org","org_one");
		
		//流程定义中配置了知会，完成任务时创建知会任务，该过程涉及到批量插入，批量插入的sql是通过DbSqlSessionFactory定义的
		completeTask("cacheTest");
		completeTask("org");
		
		
   }
	
   
   
	
	
	/**
	 * 测试动态添加用户SqlSessionFactory的初始化
	 * @throws Exception 
	 */
	@Test
	public void  testMutiSqlSessionFactoryOnDynamic() throws Exception{
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_one");
		tenantStartProcessByProcessKey("org","Process_org","org_one");
		
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_two");
		tenantStartProcessByProcessKey("org","Process_org","org_two");
		
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest","cacheTest_three");
		tenantStartProcessByProcessKey("org","Process_org","org_three");
		
		//动态添加
		TenantMate otherTenantMate=new TenantMate();
		otherTenantMate.setTenantId("other");
		otherTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_org?useUnicode=true&amp;characterEncoding=utf8");
		otherTenantMate.setJdbcPassword("root");
		otherTenantMate.setJdbcUsername("root");
		otherTenantMate.setProcessClassRelativePath("multitenant/Process_org.bpmnx");
		otherTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
		addTenantDynamic(otherTenantMate);
		
		tenantStartProcessByProcessKey("other","Process_org","other_one");
		tenantStartProcessByProcessKey("other","Process_org","other_two");
		tenantStartProcessByProcessKey("other","Process_org","other_three");
		
		assertOrderBy("cacheTest", "cacheTest");
		assertOrderBy("org", "org");
		assertOrderBy("other", "other");
		
	}
	
	@Test
	public void testMutiDbSqlSessionFactoryOnDynamic() throws Exception {
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest", "cacheTest_one");
		tenantStartProcessByProcessKey("org", "Process_org", "org_one");
		
		
		// 动态添加
		TenantMate otherTenantMate = new TenantMate();
		otherTenantMate.setTenantId("other");
		otherTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_org?useUnicode=true&amp;characterEncoding=utf8");
		otherTenantMate.setJdbcPassword("root");
		otherTenantMate.setJdbcUsername("root");
		otherTenantMate.setProcessClassRelativePath("multitenant/Process_org.bpmnx");
		otherTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
		addTenantDynamic(otherTenantMate);

		tenantStartProcessByProcessKey("other", "Process_org", "other_one");

		// 流程定义中配置了知会，完成任务时创建知会任务，该过程涉及到批量插入，批量插入的sql是通过DbSqlSessionFactory定义的
		completeTask("cacheTest");
		completeTask("org");
		completeTask("other");

		assertNotificationReceiver("cacheTest", "user8", "user7", "user9");
		assertNotificationReceiver("org", "user0", "user2", "user1");
		assertNotificationReceiver("other", "user0", "user2", "user1");
	}
	
	
	private void addTenantDynamic(TenantMate tenantMate) throws Exception{
		tenantInfos.add(tenantMate);
		MultiSchemaMultiTenantProcessEngineConfiguration	config =(MultiSchemaMultiTenantProcessEngineConfiguration) processEngine.getProcessEngineConfiguration();
		DataSource dataSource=createDataSource(tenantMate.getJdbcUrl(), tenantMate.getJdbcUsername(), tenantMate.getJdbcPassword());
		config.registerTenant(tenantMate.getTenantId(), dataSource,tenantMate.getDatabaseType());
		
		tenantInfoHolder.setCurrentTenantId(tenantMate.getTenantId());
		repositoryService.saveCategory(null, "商务", null, false);
		Model model=createAndDeployModel(tenantMate.getProcessClassRelativePath());
		tenantInfoHolder.clearCurrentTenantId();
	}
	
	
	
	
	
	
	
	
}
