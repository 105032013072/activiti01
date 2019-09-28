package com.bosssoft.install.activiti.multiTenantTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.activiti.engine.impl.persistence.entity.EnableState;
import org.activiti.engine.multitenant.TenantInfo;
import org.activiti.engine.repository.Model;
import org.junit.Test;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

public class TenantLazyLoadTest extends TenantOperationTest{

	/**
	 * 开启懒加载
	 */
	protected boolean isTenantLazyLoad(){
		return true;
	}
	
	/**
	 * 租户注册
	 * @throws Exception
	 */
	@Test
	public void testTenantRegister() throws Exception{
		super.testTenantRegister();
	}
	
	/**
	 * 注册之后租户资源懒加载
	 * @throws Exception
	 */
	@Test
	public void testRoutingTenantResource() throws Exception{
		testTenantRegister();
		
		System.out.println("===========加载spi资源");
		assertSpi("AppFrame","AppFrame");
		assertSpi("Component","Component");
		
		
		System.out.println("===========加载数据库资源");
		routingDatasource();
		deletAllProcessData();
		
		//验证所有资源均存在
		assertResourceState("AppFrame", false, false);
		assertResourceState("Component", false, false);
		
		System.out.println("===========以下操作的资源均直接从资源池中获取");
		routingDbSqlSessionFactory();
		deletAllProcessData();
		
		routingSqlSessionFactory();
		
	}
	
	// =====启用租户后资源懒加载
	@Test
	public void TestEnableTenant() throws Exception {
		testTenantRegister();
		
		String forbidTenantId = "AppFrame";
		tenantService.forbidTenant(forbidTenantId);
		
		//被禁用之后无法操作
		TenantInfo tenantInfo = tenantService.getTenantInfoByTenantId(forbidTenantId);
		assertEquals(EnableState.FORBID.getStateCode(), tenantInfo.getEnableState());
		assertResourceState(forbidTenantId,true,false);
		// AppFrame 租户报错
		try {
			Model appFramModel = deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
			fail("No exception thrown.");
		} catch (Exception e) {
			String errorinfo = String.format("org.activiti.engine.ActivitiException: tenant with tenantId=%s have been forbidden", forbidTenantId);
			assertEquals(errorinfo, e.toString());
		}
		
		
		tenantService.enableTenant(forbidTenantId);
		assertResourceState(forbidTenantId,true,false);

		// 可以正常发起流程
		System.out.println("======租户资源懒加载");
		Model appFramModel = deployProcess(forbidTenantId, "multitenant/Process_AppFrame.bpmnx");
		startProcess(forbidTenantId, forbidTenantId, appFramModel.getKey());
		assertProcessData(forbidTenantId, "Process_AppFrame", forbidTenantId);
	}
	
	/**
	 * 测试引擎启动时租户资源的加载
	 * @throws Exception 
	 */
	@Test
	public void testTenantLoadForProcessEngineStart() throws Exception{
		testRoutingTenantResource();

		processEngineRestart();
		
		// 执行禁用租户的测试时发起了流程，需要将已经存在的流程信息清空
		deletAllProcessData();

	   //验证所有激活租户的信息均没有被加载
		assertResourceState("AppFrame", true,false);
		assertResourceState("Component", true,false);
		
		System.out.println("===========加载数据库资源");
		routingDatasource();
		deletAllProcessData();
		
		//验证所有资源均存在
		assertResourceState("AppFrame", false, false);
		assertResourceState("Component", false, false);
		
		System.out.println("===========以下操作的资源均直接从资源池中获取");
		routingDbSqlSessionFactory();
		deletAllProcessData();
		
		routingSqlSessionFactory();
	}
	
	@Test
	/**
	 * 多线程下的懒加载
	 */
	public void testLazyLoadMultiThread() throws Exception{
		tenantRegister();
		
		Model appFramModel=deployProcess("AppFrame", "multitenant/Process_AppFrame.bpmnx");
		Model componentModel=deployProcess("Component", "multitenant/Process_Component.bpmnx");
		assertResourceState("AppFrame",false,false);
		assertResourceState("Component",false,false);
		
		//重启
		processEngineRestart();
		
		assertResourceState("AppFrame",true,false);
		assertResourceState("Component",true,false);
		
		//多线程
		int runnerCount = 50;
		// Rnner数组，想当于并发多少个。
		TestRunnable[] trs = new TestRunnable[runnerCount];
		for (int i = 1; i < runnerCount; i++) {
			trs[i] = new TenantRunnable(i);
		} // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		try {
			// 开发并发执行数组里定义的内容
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除被启用租户的流程数据
	 */
	private void deletAllProcessData(){
		for (String tenantId : tenantInfoHolder.getAllTenants()) {
			deleteProcessData(tenantId);
		}
	}
	
	class TenantRunnable extends TestRunnable{
		
		private String tenantId=null;
        private String processDefinitionKey=null;
		
		
		public TenantRunnable(int index){
			if (index % 2 == 0) {
				tenantId="AppFrame";
				processDefinitionKey="Process_AppFrame";
			} else {
				tenantId="Component";
				processDefinitionKey="Process_Component";
			}
		}
		
		@Override
		public void runTest() throws Throwable {
			startProcess(tenantId, tenantId, processDefinitionKey);
		}
		
	}
}
