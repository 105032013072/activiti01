package com.bosssoft.install.activiti.multiTenantTest;

import java.io.InputStream;
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
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.TenantService;
import org.activiti.engine.impl.cfg.multitenant.DefaultTenantInfoHolder;
import org.activiti.engine.impl.cfg.multitenant.MultiSchemaMultiTenantProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.multitenant.MultiTenantOMServiceImpl;
import org.activiti.engine.impl.cfg.multitenant.MultiTenantUIServiceImpl;
import org.activiti.engine.impl.cfg.multitenant.TenantInfoHolder;
import org.activiti.engine.impl.multitenant.TenantServiceImpl;
import org.activiti.engine.impl.persistence.entity.EnableState;
import org.activiti.engine.impl.persistence.entity.JdbcConfiguration;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.multitenant.TenantInfo;
import org.activiti.engine.repository.Category;
import org.activiti.engine.repository.Model;
import org.activiti.engine.spi.SpiConfiguration;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosssoft.install.activiti.multiSchemaVerification.TenantMate;
import com.mchange.v2.c3p0.DataSources;

public class MultiTenantBaseTest {

public static Logger logger = LoggerFactory.getLogger(MultiTenantBaseTest.class);
	
	protected List<TenantMate> tenantInfos=new ArrayList<TenantMate>();
	protected TenantInfoHolder tenantInfoHolder;
	
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
	public TenantService tenantService;
	public MultiSchemaMultiTenantProcessEngineConfiguration multiTenantConfig;
	
	@Before
	public void init() throws Exception{
		
		tenantInfoHolder=new DefaultTenantInfoHolder();

		MultiSchemaMultiTenantProcessEngineConfiguration	config = new MultiSchemaMultiTenantProcessEngineConfiguration(tenantInfoHolder);
		
		config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		config.setDatabaseTablePrefix("X");
		config.setProcessEngineDatabaseType("mysql");
		//config.setJobExecutorActivate(true);
		
		DataSource dataSource=createDataSource("jdbc:mysql://127.0.0.1:3306/activiti_tenant?useUnicode=true&amp;characterEncoding=utf8", "root", "root");
		config.setProcessEngineDataSource(dataSource);
		config.setTenantLazyLoad(isTenantLazyLoad());
		
		processEngine = config.buildProcessEngine();
		initActivitiServices(processEngine);
	}
	
	protected boolean isTenantLazyLoad(){
		return true;
	}

	protected void initProcessData() throws Exception {
		for (TenantMate tenantMate : tenantInfos) {
			tenantInfoHolder.setCurrentTenantId(tenantMate.getTenantId());
			repositoryService.saveCategory(null, "商务", null, false);
			
			Model model=createAndDeployModel(tenantMate.getProcessClassRelativePath());
		}
	}

	protected void initTenantInfos() throws Exception {
		TenantMate appFrameTenantMate=new TenantMate();
		JdbcConfiguration appFrameJdbcConfiguration=new JdbcConfiguration();
		appFrameTenantMate.setTenantId("AppFrame");
		appFrameJdbcConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_cache_test?useUnicode=true&amp;characterEncoding=utf8");
		appFrameJdbcConfiguration.setJdbcUsername("root");
		appFrameJdbcConfiguration.setJdbcPassword("root");
		appFrameTenantMate.setDatabaseType("mysql");
		appFrameTenantMate.setJdbcConfiguration(appFrameJdbcConfiguration);
		appFrameTenantMate.setOmserviceQualifiedName("com.bosssoft.platform.activiti.multi.AppFrameOMServiceImpl");
		appFrameTenantMate.setUiserviceQualifiedName("com.bosssoft.platform.activiti.multi.AppFrameUIService");
		appFrameTenantMate.setProcessClassRelativePath("multitenant/Process_AppFrame.bpmnx");
		appFrameTenantMate.addResource("AppFrame.jar", readAsBytes("multitenant/jar/AppFrame.jar"));
		tenantInfos.add(appFrameTenantMate);
		
		
		TenantMate componentTenantMate=new TenantMate();
		JdbcConfiguration componentTenantMateJdbcConfiguration=new JdbcConfiguration();
		componentTenantMate.setTenantId("Component");
		componentTenantMateJdbcConfiguration.setJdbcUrl("jdbc:oracle:thin:@172.18.174.80:1521/orcl");
		componentTenantMateJdbcConfiguration.setJdbcUsername("bs");
		componentTenantMateJdbcConfiguration.setJdbcPassword("bpmnx");
		componentTenantMate.setDatabaseType("oracle");
		componentTenantMate.setJdbcConfiguration(componentTenantMateJdbcConfiguration);
		componentTenantMate.setOmserviceQualifiedName("com.bosssoft.platform.activiti.multi.ComponentOMServiceImpl");
		componentTenantMate.setUiserviceQualifiedName("com.bosssoft.platform.activiti.multi.ComponentUIService");
		componentTenantMate.setProcessClassRelativePath("multitenant/Process_Component.bpmnx");
		componentTenantMate.addResource("Component.jar", readAsBytes("multitenant/jar/Component.jar"));
		tenantInfos.add(componentTenantMate);
		
		/*TenantMate nullTenantMate=new TenantMate();
		nullTenantMate.setTenantId(null);
		nullTenantMate.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_org?useUnicode=true&amp;characterEncoding=utf8");
		nullTenantMate.setJdbcPassword("root");
		nullTenantMate.setJdbcUsername("root");
		nullTenantMate.setProcessClassRelativePath("multitenant/Process_org.bpmnx");
		nullTenantMate.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
		tenantInfos.add(nullTenantMate);*/
	}
	
	public byte[] readAsBytes(String classpathResource)throws Exception{
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream(classpathResource);
		/*String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));*/
		byte[] byt = new byte[inputStream.available()];
		inputStream.read(byt);
		return byt;
	}
	
	
	protected DataSource createDataSource(String jdbcUrl,String jdbcUsername,String jdbcPassword){
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
        tenantService=processEngine.getTenantService();
        multiTenantConfig=(MultiSchemaMultiTenantProcessEngineConfiguration) processEngine.getProcessEngineConfiguration();
	}

	
	@After
	//@Test
	public void tearDown() throws Exception{
	
		System.out.println("\n================== tearDown");
		
		for (String tenantId : tenantInfoHolder.getAllTenants()) {
			deleteProcessData(tenantId);
		}
		
		cleanTenantData();
	}
	
	private void cleanTenantData(){
		((TenantServiceImpl)tenantService).setCurrentTenantAsProcessEngine();
		List<TenantInfo> list=tenantService.createTenantQuery().list();
		for (TenantInfo tenantInfo : list) {
			tenantService.deleteTenant(tenantInfo.getTenantId());
		}
	}
	
	protected void  deleteProcessData(String tenantId){
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
