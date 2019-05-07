package com.bosssoft.install.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.repository.Category;
import org.junit.Test;

public class CategoryTest extends BaseTest{
	
	@Test
	public void addCategory(){

		//repositoryService.saveCategory(null, "合同", null,false);
		/*repositoryService.saveCategory("2020002", "财政合同", "2020001",true);
		repositoryService.saveCategory("2020003", "非税合同", null,false);*/
		
		repositoryService.saveCategory(null, "购买合同", "2020001",true);
		
		
	}

	@Test
	public void categoryTest() {
		

		// ---search
		
		List<Category> list=new ArrayList<Category>();
		// 查询列表
		System.out.println("-------类别列表-------");
		 list = repositoryService.createCategoryQuery().listPage(2, 3);
		for (Category category : list) {
			System.out.println(
					category.getId() + "  " + category.getCategoryName() + " " + category.getParentCategoryId());
		}

		// 根据ID查询
		System.out.println("-------根据ID查询-------");
		Category category = repositoryService.createCategoryQuery().categoryId("4755001").singleResult();
		System.out.println(category.getId() + "  " + category.getCategoryName() + " " + category.getParentCategoryId());
		
		//根据名称模糊查询
		System.out.println("-------名称模糊-------");
		list= repositoryService.createCategoryQuery().categoryNameLike("类别").list();
		for (Category ca : list) {
			System.out.println(
					ca.getId() + "  " + ca.getCategoryName() + " " + ca.getParentCategoryId());
		}
		
		System.out.println("-------名称精确-------");
		list= repositoryService.createCategoryQuery().categoryName("人资").list();
		for (Category ca : list) {
			System.out.println(
					ca.getId() + "  " + ca.getCategoryName() + " " + ca.getParentCategoryId());
		}
		
		System.out.println("-------父级类别-------");
		list= repositoryService.createCategoryQuery().parentCategoryId("4750001").listPage(1, 1);
		for (Category ca : list) {
			System.out.println(
					ca.getId() + "  " + ca.getCategoryName() + " " + ca.getParentCategoryId());
		}
		
		System.out.println(repositoryService.createCategoryQuery().count());
	}

	@Test
	public void one(){
		List<Category>	list= repositoryService.createCategoryQuery().list();
		for (Category category : list) {
			System.out.println(category.getId()+" "+category.getCategoryName()+" "+category.isLeaf());
		}
	}
	
}
