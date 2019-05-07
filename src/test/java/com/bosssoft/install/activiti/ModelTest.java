package com.bosssoft.install.activiti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.impl.repository.ExportProcessInfo;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.bosssoft.platform.common.utils.FileUtils;


public class ModelTest extends BaseTest{

	/*@Test
	public void test() throws IOException{
		Model model2 = repositoryService.createModelQuery().modelKey("test1.bpmn").singleResult();
		// 部署该model
		if (repositoryService.createProcessDefinitionQuery().processDefinitionKey("test1").list().isEmpty()) {
			deployModel(repositoryService, model2.getId());
		}
	}
	
	public static Deployment deployModel(RepositoryService repositoryService, String modelId) throws IOException
	{
		Model modelData = repositoryService.getModel(modelId);
		//EditorSource就是XML格式的
		byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);

		String processName = modelData.getName() + ".bpmn20.xml";
		Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
				.addString(processName, new String(bpmnBytes, "utf-8")).deploy();

		//设置部署ID
		modelData.setDeploymentId(deployment.getId());
		repositoryService.saveModel(modelData);

		return deployment;
	}*/
	
	@Test
	public void saveModel(){
		//保存模型
		InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream("com/contract/process-test.bpmnx");
		String result = new BufferedReader(new InputStreamReader(inputStream))
		        .lines().collect(Collectors.joining(System.lineSeparator()));
		
	  //  repositoryService.saveModelAndDeploy(null, result, "910001");
       Model model=repositoryService.saveModel(null, result, "910001");
  
        repositoryService.saveModelAndUpdateProcessDefinition(modelId, processDefinitionStr);
 
	  
  
	  /*ProcessDefinition processDefinition =repositoryService.createProcessDefinitionQuery()
	                  .deploymentId(model.getDeploymentId())
	                   .processDefinitionKey(model.getKey())
	                   .singleResult();*/
	}
	
	@Test
	public void modelSearch() throws IOException{
	List<Model> list=	repositoryService.createModelQuery()
			                           // .latestVersion()
			                            
			                           // .defaultProcess()
			                            .modelKey("finance12")
			                               //.deployed()
			                               
			                              .list();
	
	
	
	for (Model model : list) {
		System.out.println(model.getId()+"   "  +model.getCategory()+" "+model.getName()+" "+model.getKey()+" "+model.getEditorSourceValueId());
	     model.getProcessDefinitionId();

		//根据资源ID获取流程定义ID
		/*InputStream inputStream= repositoryService.getResourceAsStreamByResourceId(model.getEditorSourceValueId());
		
		byte[] bytes = new byte[0];
		bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		String str = new String(bytes);
		System.out.println(str);*/
	}

	}
	
	@Test
	public void setDefault(){
		repositoryService.setDefaultProcess("2375002");
	}
	
	
	
	@Test
	public void deleteModel(){
		List<Model> list=repositoryService.createModelQuery().list();
		for (Model model : list) {
			repositoryService.deleteModel(model.getId(), true);
		}
	}
	
	@Test
	public void export(){
		List<String> list=new ArrayList<String>();
		list.add("2340001");
		list.add("2360002");
		List<ExportProcessInfo> infoList=	repositoryService.exportBulkProcess(list);
		
		for (ExportProcessInfo exportProcessInfo : infoList) {
			String context=new String(exportProcessInfo.getProcessResource());
			
			System.out.println(exportProcessInfo.getProcessKey()+"");
		}
	}
	
	
	@Test
	public void importModel() throws IOException{
		InputStream inputStream = new FileInputStream("D:/test/import/contract.bpmn");
		
		List<byte[]> processResourceList=new ArrayList<byte[]>();
		byte[] byt = new byte[inputStream.available()];
		inputStream.read(byt);
	    processResourceList.add(byt);
		
	    inputStream.close();
		
		repositoryService.importBulkProcess("03", processResourceList, true);
		/*new File("D:/test/import/contract.bpmn").delete();*/
	}
	
	
	@Test
	public void getResource() throws IOException{
		InputStream  inputStreamRresource=repositoryService.getResourceAsStreamByResourceId("60013");
		byte[] bytes = new byte[0];
		bytes = new byte[inputStreamRresource.available()];
		inputStreamRresource.read(bytes);
		String str = new String(bytes);
		System.out.println(str);
		
	}
	
	
	
	
}
