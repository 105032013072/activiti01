package com.bosssoft.install.activiti.multiTenantTest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.multitenant.MultiTenantOMServiceImpl;
import org.activiti.engine.impl.cfg.multitenant.MultiTenantUIServiceImpl;
import org.activiti.engine.impl.cfg.multitenant.TenantAwareDataSource;
import org.activiti.engine.impl.cfg.multitenant.TenantAwareDbSqlSessionFactory;
import org.activiti.engine.impl.cfg.multitenant.TenantAwareSqlSessionFactory;
import org.activiti.engine.impl.multitenant.TenantServiceImpl;
import org.activiti.engine.impl.persistence.entity.EnableState;
import org.activiti.engine.impl.persistence.entity.NotificationEntity;
import org.activiti.engine.impl.persistence.entity.TenantResourceEntity;
import org.activiti.engine.multitenant.TenantBuilder;
import org.activiti.engine.multitenant.TenantInfo;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.OMService;
import org.activiti.engine.spi.identity.Participator;
import org.activiti.engine.spi.ui.UIService;
import org.activiti.engine.spi.ui.bizextend.BizExtendDescriptor;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Test;


public class TenantOperationTest extends MultiTenantBaseTest{

	/**
	 * 关闭懒加载
	 */
	protected boolean isTenantLazyLoad(){
		return false;
	}
	
	
	/**
	 * 租户注册
	 * @throws Exception
	 */
	@Test
	public void testTenantRegister() throws Exception{
		tenantRegister();
		//验证
		TenantInfo tenantInfo=tenantService.getTenantInfoByTenantId("AppFrame");
		assertTenantInfo(tenantInfo);
		tenantInfo=tenantService.getTenantInfoByTenantId("Component");
		assertTenantInfo(tenantInfo);
		
		List<TenantResourceEntity> list=	tenantService.getTenantResourcesByTenantId("AppFrame");
		assertEquals(1, list.size());
		assertEquals("AppFrame.jar",list.get(0).getName());
		
		list=tenantService.getTenantResourcesByTenantId("Component");
		assertEquals(1, list.size());
		assertEquals("Component.jar",list.get(0).getName());
		
		assertResourceState("AppFrame", isTenantLazyLoad(), false);
		assertResourceState("Component", isTenantLazyLoad(), false);
	}
	
	/**
	 * 租户数据源路由
	 * @throws Exception 
	 */
	@Test 
	public void testRoutingDatasource() throws Exception{
		tenantRegister();
		routingDatasource();
	} 
	
	
	protected void routingDatasource() throws Exception{
		Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
		Model componentModel=deployProcess("Component", "multitenant/Process_Component.bpmnx");
		startProcess("AppFrame", "AppFrame", appFramModel.getKey());
		startProcess("Component", "Component", componentModel.getKey());
		
		assertProcessData("AppFrame", "Process_AppFrame", "AppFrame");
		assertProcessData("Component", "Process_Component", "Component");
	}
	
	/**
	 * 通过运行排序的sql 测试不同类型数据库对SqlSessionFactory的路由
	 * @throws Exception
	 */
	@Test
	public void testRoutingSqlSessionFactory() throws Exception{
		tenantRegister();
		routingSqlSessionFactory();
	}
	
	protected void routingSqlSessionFactory() throws Exception{
		Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
		Model componentModel=deployProcess("Component", "multitenant/Process_Component.bpmnx");
		
		startProcess("AppFrame", "AppFrame_business_1", appFramModel.getKey());
		startProcess("AppFrame", "AppFrame_business_2", appFramModel.getKey());
		startProcess("AppFrame", "AppFrame_business_3", appFramModel.getKey());
		
		startProcess("Component", "Component_business_1", componentModel.getKey());
		startProcess("Component", "Component_business_2", componentModel.getKey());
		startProcess("Component", "Component_business_3", componentModel.getKey());
		
		assertOrderBy("AppFrame", "AppFrame_business");
		assertOrderBy("Component", "Component_business");
	}
	
	/**
	 * DbSqlSessionFactory 路由
	 * @throws Exception
	 */
	@Test
	public void testRoutingDbSqlSessionFactory() throws Exception{
		tenantRegister();
		routingDbSqlSessionFactory();
	}
	
	protected void routingDbSqlSessionFactory() throws Exception{
		Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
		Model componentModel=deployProcess("Component", "multitenant/Process_Component.bpmnx");
		startProcess("AppFrame", "AppFrame", appFramModel.getKey());
		startProcess("Component", "Component", componentModel.getKey());
		
		//流程定义中配置了知会，完成任务时创建知会任务，该过程涉及到批量插入，批量插入的sql是通过DbSqlSessionFactory定义的
		completeTask("AppFrame");
		completeTask("Component");
		
		assertNotificationReceiver("AppFrame","user8","user7","user9");
		assertNotificationReceiver("Component","user0","user2","user1");
	}
	
	/**
	 * spi资源路由
	 * @throws Exception
	 */
	@Test
    public void testRoutingSpi()throws Exception{
		tenantRegister();
		
		assertSpi("AppFrame","AppFrame");
		assertSpi("Component","Component");
    }

	/**
	 * 禁用租户
	 * @throws Exception
	 */
	@Test
	public void testForbidTenant() throws Exception{
		tenantRegister();
		String forbidTenantId = "AppFrame";
		tenantService.forbidTenant(forbidTenantId);

		// 验证
		TenantInfo tenantInfo = tenantService.getTenantInfoByTenantId(forbidTenantId);
		assertEquals(EnableState.FORBID.getStateCode(), tenantInfo.getEnableState());
		assertResourceState("Component",false,false);
		assertResourceState("AppFrame",true,false);
		
		//Component 租户可以正常发起流程
		Model componentModel = deployProcess("Component", "multitenant/Process_Component.bpmnx");
		startProcess("Component", "Component", componentModel.getKey());
		assertProcessData("Component", "Process_Component", "Component");
		
		//AppFrame 租户报错
		try{
			Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
			fail("No exception thrown.");
		}catch(Exception e){
			String errorinfo=String.format("org.activiti.engine.ActivitiException: tenant with tenantId=%s have been forbidden", forbidTenantId);
			assertEquals(errorinfo, e.toString());
		}
	}
	
	//=====启用租户
	@Test
	public void TestEnableTenant() throws Exception{
		testForbidTenant();
		
		//执行禁用租户的测试时发起了流程，需要将已经存在的流程信息清空
		for (String tenantId : tenantInfoHolder.getAllTenants()) {
			deleteProcessData(tenantId);
		}
		
		String tenantId = "AppFrame";
		tenantService.enableTenant(tenantId);
		
		//验证资源
		TenantInfo tenantInfo = tenantService.getTenantInfoByTenantId(tenantId);
		assertEquals(EnableState.ENABLE.getStateCode(), tenantInfo.getEnableState());
		assertResourceState("Component",false,false);
		assertResourceState("AppFrame",false,false);
		
		//可以正常发起流程
		Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
		Model componentModel=deployProcess("Component", "multitenant/Process_Component.bpmnx");
		startProcess("AppFrame", "AppFrame", appFramModel.getKey());
		startProcess("Component", "Component", componentModel.getKey());
		
		
		assertProcessData("AppFrame", "Process_AppFrame", "AppFrame");
		assertProcessData("Component", "Process_Component", "Component");
	}

	
	/**
	 * 删除租户
	 * @throws Exception 
	 */
	@Test
	public void deleteTenant() throws Exception{
		testTenantRegister();
		
		tenantService.deleteTenant("AppFrame");
		
		//验证
		List<TenantInfo> list=tenantService.createTenantQuery().list();
		assertEquals(1, list.size());
		assertEquals("Component", list.get(0).getTenantId());
		List<TenantResourceEntity> resources=tenantService.getTenantResourcesByTenantId("AppFrame");
		assertEquals(0, resources.size());
		
		//被删除的租户资源不存在
		assertResourceState("AppFrame", true,true);
		
		// 被删除租户无法部署流程
		try {
			Model appFramModel = deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
			fail("No exception thrown.");
		} catch (Exception e) {
			assertEquals("org.activiti.engine.ActivitiException: get tenant resource error for cant't find tenantInfo by tenantId=AppFrame", e.toString());
		}
	}

	/**
	 * 测试引擎启动时租户资源的加载
	 * @throws Exception 
	 */
	@Test
	public void testTenantLoadForProcessEngineStart() throws Exception{
		testForbidTenant();

		processEngineRestart();
		
		// 执行禁用租户的测试时发起了流程，需要将已经存在的流程信息清空
		for (String tenantId : tenantInfoHolder.getAllTenants()) {
			deleteProcessData(tenantId);
		}

	   //验证所有激活租户的信息均被加载
		assertResourceState("AppFrame", true,false);
		assertResourceState("Component", false,false);
		
		//激活状态的可以正常发起流程
		Model componentModel=deployProcess("Component", "multitenant/Process_Component.bpmnx");
		startProcess("Component", "Component_business_1", componentModel.getKey());
		startProcess("Component", "Component_business_2", componentModel.getKey());
		startProcess("Component", "Component_business_3", componentModel.getKey());
		assertOrderBy("Component", "Component_business");	
		
		//被禁止的没有加载到引擎中，无法操作
		try{
			Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
			fail("No exception thrown.");
		}catch(Exception e){
			String errorinfo=String.format("org.activiti.engine.ActivitiException: tenant with tenantId=%s have been forbidden", "AppFrame");
			assertEquals(errorinfo, e.toString());
		}
	}
	
	
	
	protected void processEngineRestart() throws Exception{
		tenantInfoHolder=null;
		multiTenantConfig=null;
		processEngine =null;
		super.init();
	}
	
	
	protected void assertResourceState(String tenantId, boolean isExpectNulll,boolean isDeleted) {
        assertEquals(!isExpectNulll,tenantInfoHolder.getAllTenants().contains(tenantId));
		
		
		TenantAwareDataSource tenantAwareDataSource = (TenantAwareDataSource) multiTenantConfig.getDataSource();
		assertNullOrNot(tenantAwareDataSource.getResourcePool().get(tenantId), isExpectNulll);
		assertNullOrNot(tenantAwareDataSource.getDatabaseTypeByTenantId(tenantId), isExpectNulll);
		
		TenantAwareSqlSessionFactory tenantAwareSqlSessionFactory=(TenantAwareSqlSessionFactory) multiTenantConfig.getSqlSessionFactory();
		assertNullOrNot(tenantAwareSqlSessionFactory.getResourcePool().get(tenantId), isExpectNulll);
	
		TenantAwareDbSqlSessionFactory tenantAwareDbSqlSessionFactory=(TenantAwareDbSqlSessionFactory) multiTenantConfig.getDbSqlSessionFactory();
		assertNullOrNot(tenantAwareDbSqlSessionFactory.getMultiTenantResource().getResourcePool().get(tenantId), isExpectNulll);
		
		MultiTenantOMServiceImpl oMService=(MultiTenantOMServiceImpl) multiTenantConfig.getSpiConfiguration().getOmService();
		assertNullOrNot(oMService.getResourcePool().get(tenantId), isExpectNulll);
		
		MultiTenantUIServiceImpl uiService=(MultiTenantUIServiceImpl) multiTenantConfig.getSpiConfiguration().getUiService();
		assertNullOrNot(uiService.getResourcePool().get(tenantId), isExpectNulll);
		
		
		if(isExpectNulll){
			//class是否被卸载  
			TenantInfo tenantInfo = tenantService.getTenantInfoByTenantId(tenantId);
			if(isDeleted){
				assertNull(tenantInfo);
			}else{
				try {
					Class.forName(tenantInfo.getUiserviceQualifiedName());
					fail("No exception thrown.");
				} catch (ClassNotFoundException e) {
					assertEquals("java.lang.ClassNotFoundException: "+tenantInfo.getUiserviceQualifiedName(), e.toString());
				}
				try {
					Class.forName(tenantInfo.getOmserviceQualifiedName());
					fail("No exception thrown.");
				} catch (ClassNotFoundException e) {
					assertEquals("java.lang.ClassNotFoundException: "+tenantInfo.getOmserviceQualifiedName(), e.toString());
				}
			}
			
			
			
			
		}else{
			//SPI资源可以调用
			Participator participator =oMService.getResourcePool().get(tenantId).getParticipatorInfo(null, IdentityEnum.USER);
		    assertEquals(tenantId, participator.getParticipatorId());
		    assertEquals(tenantId, participator.getParticipatorName());
		    
		    BizExtendDescriptor  bizExtendDescriptor=uiService.getResourcePool().get(tenantId).getBizExtendDescriptor().get(0);
		    assertEquals(tenantId, bizExtendDescriptor.getText());
		    assertEquals(tenantId, bizExtendDescriptor.getValue());
		}
		
	}
	
	private void assertNullOrNot(Object object,boolean isExpectNulll){
		if(isExpectNulll){
			assertNull(object);
		}else{
			assertNotNull(object);
		}
	}


	protected void  assertTenantInfo(TenantInfo tenantInfo){
		assertNotNull(tenantInfo);
		String format="tenantId={},databaseType={},jdbcUrl={},jdbcUsername={},jdbcPassword={},omserviceQualifiedName={},uiserviceQualifiedName={}";
	    String info=String.format(format, tenantInfo.getTenantId(),tenantInfo.getDatabaseType()
	    		,tenantInfo.getJdbcConfiguration().getJdbcUrl()
	    		,tenantInfo.getJdbcConfiguration().getJdbcUsername()
	    		,tenantInfo.getJdbcConfiguration().getJdbcPassword()
	    		,tenantInfo.getOmserviceQualifiedName()
	    		,tenantInfo.getUiserviceQualifiedName());
		System.out.println(tenantInfo.getTenantId());
	}
	
	/**
	 * 注册租户
	 * @throws Exception
	 */
	protected void tenantRegister() throws Exception{
		TenantBuilder tenantBuilder=tenantService.createTenantBuilder()
	             .tenantId("AppFrame")
	             .databaseType("mysql")
	             .jdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_cache_test?useUnicode=true&amp;characterEncoding=utf8")
	             .jdbcUsername("root")
	             .jdbcPassword("root")
	             .omserviceQualifiedName("com.bosssoft.platform.activiti.multi.AppFrameOMServiceImpl")
	             .uiserviceQualifiedName("com.bosssoft.platform.activiti.multi.AppFrameUIService")
	             .addTenantResource("AppFrame.jar", readAsBytes("multitenant/jar/AppFrame.jar"))
	             .register();
		
		tenantBuilder=tenantService.createTenantBuilder()
	             .tenantId("Component")
	             .databaseType("oracle")
	             .jdbcUrl("jdbc:oracle:thin:@172.18.174.80:1521/orcl")
	             .jdbcUsername("bpmnx")
	             .jdbcPassword("bs")
	             .omserviceQualifiedName("com.bosssoft.platform.activiti.multi.ComponentOMServiceImpl")
	             .uiserviceQualifiedName("com.bosssoft.platform.activiti.multi.ComponentUIService")
	             .addTenantResource("Component.jar", readAsBytes("multitenant/jar/Component.jar"))
	             .register();
	}
	
	/**
	 * 部署流程并且启动流程
	 * @param tenantId
	 * @param classpathResource
	 * @param businesskey
	 * @throws Exception
	 */
	protected Model deployProcess(String tenantId,String classpathResource) throws Exception{
		tenantInfoHolder.setCurrentTenantId(tenantId);
		repositoryService.saveCategory(null, "商务", null, false);
		Model model=createAndDeployModel(classpathResource);
		return model;
	}
	
	protected void startProcess(String tenantId,String businesskey,String processDefinitionKey){
		tenantInfoHolder.setCurrentTenantId(tenantId);
		ProcessInstance inst=	runtimeService
				.createProcessInstanceBuilder()
				.processDefinitionKey(processDefinitionKey)
				.businessKey(businesskey)
				.processStarter("u01")
				.start();
	}
	
	
	protected void assertProcessData(String tenantId,String processDefinitionKey,String businesskey) {
		tenantInfoHolder.setCurrentTenantId(tenantId);
		List<Model> modelList=	repositoryService.createModelQuery().list();
		assertEquals(1, modelList.size());
		assertEquals(processDefinitionKey, modelList.get(0).getKey());
		
		List<ProcessInstance> processInstList= runtimeService.createProcessInstanceQuery().list();
		assertEquals(1, processInstList.size());
		assertEquals(businesskey, processInstList.get(0).getBusinessKey());
	}
	
	
	protected void assertOrderBy(String tenantId,String businessPrefix){
		tenantInfoHolder.setCurrentTenantId(tenantId);
		List<HistoricProcessInstance> list=histroyService.createHistoricProcessInstanceQuery().orderByProcessInstanceStartTime().desc().list();
		HistoricProcessInstance one=list.get(0);
		assertEquals(businessPrefix+"_3", one.getBusinessKey());
		
		HistoricProcessInstance three=list.get(2);
		assertEquals(businessPrefix+"_1", three.getBusinessKey());
	}
	
	
	protected void completeTask(String  tenantId){
		   tenantInfoHolder.setCurrentTenantId(tenantId);
		   //查询任务
		   List<Task>  tasks= taskService.createTaskQuery().list();
		   assertEquals(1, tasks.size());
		   taskService.complete(tasks.get(0).getId());
	 }
	
	protected void assertNotificationReceiver(String tenantId, String... receivers) {
		tenantInfoHolder.setCurrentTenantId(tenantId);
		for (String receiverId : receivers) {
			List<NotificationEntity> list = runtimeService.createNotificationListQuery().receiverId(receiverId).list();
			assertEquals(1, list.size());
		}

	}
	
	protected void assertSpi(String tenantId,String expectValue) {
		tenantInfoHolder.setCurrentTenantId(tenantId);
	    OMService omservice=	multiTenantConfig.getSpiConfiguration().getOmService();
	    Participator participator =omservice.getParticipatorInfo(null, IdentityEnum.USER);
	    assertEquals(expectValue, participator.getParticipatorId());
	    assertEquals(expectValue, participator.getParticipatorName());
	    
	    
	    UIService uiService=multiTenantConfig.getSpiConfiguration().getUiService();
	    BizExtendDescriptor  bizExtendDescriptor=uiService.getBizExtendDescriptor().get(0);
	    assertEquals(expectValue, bizExtendDescriptor.getText());
	    assertEquals(expectValue, bizExtendDescriptor.getValue());
		
	}
	
	
}
