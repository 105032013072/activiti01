package com.bosssoft.install.activiti.multiSchemaVerification;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.BizExtendService;
import org.activiti.engine.BpmnxTool;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.multitenant.MultiSchemaMultiTenantProcessEngineConfiguration;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Category;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.spi.SpiConfiguration;
import org.activiti.engine.spi.identity.IdentityEnum;
import org.activiti.engine.spi.identity.OMService;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosssoft.install.activiti.MockOMServiceImpl;

public class MultiProcessEngineTest {

	public static Logger logger = LoggerFactory.getLogger(MultiProcessEngineTest.class);
	
	private List<TenantMate> tenantInfos=new ArrayList<TenantMate>();
	private DefaultTenantInfoHolder tenantInfoHolder;
	
	public ProcessEngine processEngine ;

	public TaskService taskService ;
	public RuntimeService runtimeService ;
	public HistoryService histroyService ;
	public RepositoryService repositoryService ;
	public IdentityService identityService;
	public ManagementService managementService ;
	public FormService formService;
	public BpmnxTool bpmnxTool;
    public DynamicBpmnService dynamicBpmnService;
	public BizExtendService bizExtendService;
	
	
	//@Before
	public void init() throws Exception{
		initTenantInfos();
		 tenantInfoHolder=new DefaultTenantInfoHolder();
		 for (TenantMate tenantMate : tenantInfos) {
			tenantInfoHolder.addTenant(tenantMate.getTenantId());
		}
		
		SpiConfiguration spiConfiguration=new SpiConfiguration();
		spiConfiguration.setOmService(new MockOMServiceImpl());
		
		
		MultiSchemaMultiTenantProcessEngineConfiguration	config = new MultiSchemaMultiTenantProcessEngineConfiguration(tenantInfoHolder);
		
		config.setDatabaseSchemaUpdate(MultiSchemaMultiTenantProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		config.setDatabaseTablePrefix("X");
		config.setSpiConfiguration(spiConfiguration);
		//config.setJobExecutorActivate(true);
		
		
		for (TenantMate tenantMate : tenantInfos) {
			DataSource dataSource=createDataSource(tenantMate.getJdbcUrl(), tenantMate.getJdbcUsername(), tenantMate.getJdbcPassword());
			config.registerTenant(tenantMate.getTenantId(), dataSource,tenantMate.getDatabaseType());
		}
		
		processEngine = config.buildProcessEngine();
		initActivitiServices(processEngine);
		initProcessData();
	}
	
	private void initProcessData() throws Exception {
		for (TenantMate tenantMate : tenantInfos) {
			tenantInfoHolder.setCurrentTenantId(tenantMate.getTenantId());
			repositoryService.saveCategory(null, "商务", null, false);
			
			Model model=createAndDeployModel(tenantMate.getProcessClassRelativePath());
		}
	}

	@Test
	public void startProcess(){
		tenantStartProcessByProcessKey("cacheTest", "Process_cacheTest");
		tenantStartProcessByProcessKey("org","Process_org");
		
	}
	
	//测试在运行时添加租户
	@Test
	public void  addTenantRuntime(){
		tenantStartProcessByProcessKey("org","Process_org");
		System.out.println("///////成功发起流程Process_org");
		
		//在运行过程中设置新增租户信息
		String newTenantId="cacheTest_02";
		TenantMate cacheTestTenantMate=new TenantMate();
		cacheTestTenantMate.setTenantId(newTenantId);
		cacheTestTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_bs?useUnicode=true&amp;characterEncoding=utf8");
		cacheTestTenantMate.setJdbcPassword("root");
		cacheTestTenantMate.setJdbcUsername("root");
		cacheTestTenantMate.setProcessClassRelativePath("multitenant/Process_cacheTest.bpmnx");
		tenantInfos.add(cacheTestTenantMate);
		
		//tenantInfoHolder.addTenant(cacheTestTenantMate.getTenantId());
		MultiSchemaMultiTenantProcessEngineConfiguration	config =(MultiSchemaMultiTenantProcessEngineConfiguration) processEngine.getProcessEngineConfiguration();
		DataSource dataSource=createDataSource(cacheTestTenantMate.getJdbcUrl(), cacheTestTenantMate.getJdbcUsername(), cacheTestTenantMate.getJdbcPassword());
		config.registerTenant(cacheTestTenantMate.getTenantId(), dataSource,cacheTestTenantMate.getDatabaseType());
		
		
		tenantInfoHolder.setCurrentTenantId(newTenantId);
		repositoryService.saveCategory(null, "商务", null, false);
	}
	
	@Test
	public void jobTest() throws Exception{
		/*testDynamicLloadJar("D:/test/file/activiti01-0.0.1-SNAPSHOT.jar");
        Thread.sleep(10);
         testDynamicLloadJar("D:/test/file/activiti01-0.0.1-SNAPSHOT_2.jar");*/
		
		testDynamicLloadJarByClassLoader("D:/test/file/activiti01-0.0.1-SNAPSHOT.jar","activiti01-0.0.1-SNAPSHOT.jar");
        Thread.sleep(10);
        testDynamicLloadJarByClassLoader("D:/test/file/activiti01-0.0.1-SNAPSHOT_2.jar","activiti01-0.0.1-SNAPSHOT_2.jar");
	}
	
	
	
	
	
	//@Test
	public void testDynamicLloadJar(String path) throws Exception{
		
		//tenantStartProcessByProcessKey("org","Process_org");
		
	 //String path = "D:/test/file/activiti01-0.0.1-SNAPSHOT.jar";//jar文件需放在工程目录下

     loadJar(path);
     Class<OMService> aClass = (Class<OMService>) Class.forName("com.bosssoft.install.activiti.multi.MultiOmService");
     OMService instance = aClass.newInstance();
     instance.getParticipatorInfo("id", IdentityEnum.POSITION);
	}
	
	private void loadJar(String jarPath) {
		File jarFile = new File(jarPath); // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹

        if (jarFile.exists() == false) {
            System.out.println("jar file not found.");
            return;
        }

        //获取类加载器的addURL方法，准备动态调用
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        // 获取方法的访问权限，保存原始值
        boolean accessible = method.isAccessible();
        try {
            //修改访问权限为可写
            if (accessible == false) {
                method.setAccessible(true);
            }

            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            
            //获取jar文件的url路径
            java.net.URL url = jarFile.toURI().toURL();
            
            //jar路径加入到系统url路径里
            method.invoke(classLoader, url);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //回写访问权限
            method.setAccessible(accessible);
        }
		
	}

	//@Test
	public void testDynamicLloadJarByClassLoader(String path,String jarName) throws Exception{
		 /*MyClassLoader classLoader = new MyClassLoader(); 
		 classLoader.loadJar(path);
		 Class<OMService> clz = (Class<OMService>)classLoader.loadClass(jarName,"com.bosssoft.install.activiti.multi.MultiOmService");
		 OMService instance = (OMService) clz.newInstance();
		 instance.getParticipatorInfo("id", IdentityEnum.POSITION);
         */
		MutilClassLoader mutilClassLoader=new MutilClassLoader(path);
		Class<OMService> clz=(Class<OMService>) mutilClassLoader.loadClass("com.bosssoft.install.activiti.multi.MultiOmService");
		OMService instance = (OMService) clz.newInstance();
		System.out.println(instance.getClass());
		 instance.getParticipatorInfo("id", IdentityEnum.POSITION);
		
    }
	
	
	
	
	private void tenantStartProcessByProcessKey(String  tenantId,String processKey){
		tenantInfoHolder.setCurrentTenantId(tenantId);
		ProcessInstance inst=	runtimeService
				.createProcessInstanceBuilder()
				//.processDefinitionKey("Process_1")
				.processDefinitionKey(processKey)
				.businessKey("key1")
				.processStarter("u01")
				//.processInstanceName("测试流程-2018-12-13")
				.start();
	}
	
	

	private void initTenantInfos() {
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
		orgTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_org?useUnicode=true&amp;characterEncoding=utf8");
		orgTenantMate.setJdbcPassword("root");
		orgTenantMate.setJdbcUsername("root");
		orgTenantMate.setProcessClassRelativePath("multitenant/Process_org.bpmnx");
		orgTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
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


	private DataSource createDataSource(String jdbcUrl,String jdbcUsername,String jdbcPassword){
		String jdbcDriver=getJdbcDriver(jdbcUrl);
		if ((jdbcDriver == null) || (jdbcUrl == null) || (jdbcUsername == null)) {
			throw new ActivitiException("DataSource or JDBC properties have to be specified in a process engine configuration");
		}
	
		logger.debug("initializing datasource to db: {}", jdbcUrl);

		PooledDataSource pooledDataSource = new PooledDataSource(ReflectUtil.getClassLoader(), jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		pooledDataSource.forceCloseAll();
		return pooledDataSource;
	}


	private String getJdbcDriver(String jdbcUrl) {
		String jdbcDriver=null;
		if(jdbcUrl.toLowerCase().indexOf("mysql") != -1){
			jdbcDriver="com.mysql.jdbc.Driver";
		}else if(jdbcUrl.toLowerCase().indexOf("oracle") != -1){
			jdbcDriver="oracle.jdbc.driver.OracleDriver";
		}else if(jdbcUrl.toLowerCase().indexOf("postgresql")!=-1){
			jdbcDriver="org.postgresql.Driver";
		}else{
			jdbcDriver=null;
		}
		return jdbcDriver;
	}
	
	
	private void initActivitiServices(ProcessEngine processEngine) {
		taskService = processEngine.getTaskService();
		runtimeService = processEngine.getRuntimeService();
		histroyService = processEngine.getHistoryService();
		repositoryService = processEngine.getRepositoryService();
		identityService = processEngine.getIdentityService();
		managementService = processEngine.getManagementService();
		formService = processEngine.getFormService();
		bpmnxTool = processEngine.getBpmnxTool();
		dynamicBpmnService = processEngine.getDynamicBpmnService();
		bizExtendService = processEngine.getBizExtendService();

	}

	
	@After
	//@Test
	public void tearDown() throws Exception{
	
		System.out.println("\n================== tearDown");
		
		for (TenantMate tenantMate : tenantInfos) {
			deleteProcessData(tenantMate.getTenantId());
		}
	}
	
	
	private void  deleteProcessData(String tenantId){
		tenantInfoHolder.setCurrentTenantId(tenantId);
		// 删除model
		List<Model> list = repositoryService.createModelQuery().list();
		for (Model model : list) {
			repositoryService.deleteModel(model.getId(), true);
		}

		// 删除类别
		List<Category> caList = repositoryService.createCategoryQuery().list();
		for (Category category : caList) {
			repositoryService.deleteCategory(category.getId());
		}
		
	}
	
	/**
	 * 创建model 并且部署
	 * @param classpathResource
	 * @throws Exception
	 */
	public Model createAndDeployModel(String classpathResource) throws Exception{
		String result=readAsString(classpathResource);
		return repositoryService.saveModelAndDeploy(null, result, getCategoryId());
	}
	
	public String readAsString(String classpathResource)throws Exception{
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream(classpathResource);
		/*String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));*/
		StringBuffer out = new StringBuffer();
	     byte[] b = new byte[4096];
	     for (int n; (n = inputStream.read(b)) != -1;) {
	          out.append(new String(b, 0, n));
	     }
		return  out.toString();
	}
	
	
	private String getCategoryId() throws Exception{
		List<Category> list=repositoryService.createCategoryQuery().categoryName("商务").list();
		if(list.isEmpty()){
			throw new Exception("无法找到类别Id");
		}
		
		return list.get(0).getId();
	}

	
	
	
}
