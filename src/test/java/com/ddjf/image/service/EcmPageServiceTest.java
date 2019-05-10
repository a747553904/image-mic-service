package com.ddjf.image.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.ddjf.image.BaseTestCase;
import com.ddjf.image.model.EcmPage;

public class EcmPageServiceTest extends BaseTestCase {

	@Autowired
	private EcmPageService ecmPageService;
	
	@Test
	public void findListTest(){
		EcmPage ecmPage = new EcmPage();
		ecmPage.setObjectno("0000052462");
		ecmPageService.findList(ecmPage);
	}
	
	@Test
	public void getImageListTest(){
		List<String> list = new ArrayList<>();
		list.add("0000052462");
		list.add("0000052562");
		System.out.println(ecmPageService.getImageList(list));
	}
	
	@Test
	public void getBySerialnoTest (){
		System.out.println(ecmPageService.getBySerialno("RL20151021000016"));
	}
	
	@Test
	@Commit
	public void saveTest(){
		EcmPage ecmPage = new EcmPage();
		ecmPage.setSerialno(System.currentTimeMillis()+"");
		ecmPage.setObjecttype("CBCreditApply");
		ecmPage.setObjectno("0000052462");
		ecmPage.setDocumentid("/wls/filesave/document/ddpm_image/image_document/2015/1008/0000052613/RL20151008000001.jpg");
		ecmPage.setTypeno("1001");
		ecmPage.setModifytime("2017/03/06 15:25:22");
		ecmPageService.save(ecmPage);;
	}
	
	@Test
	public void getMaxPageNumTest(){
		System.out.println(ecmPageService.getMaxPageNum("CBCreditApply", "0000052462", "1001"));
	}
	
	@Test
	public void getMaxSortNoTest(){
		System.out.println(ecmPageService.getMaxSortNo("CBCreditApply", "0000052462"));
	}
}
