package com.ddjf.image.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ddjf.image.BaseTestCase;

public class SequenceServiceTest extends BaseTestCase {

	@Autowired
	private SequenceService sequenceService;
	
	@Test
	public void getSequenceIdTest(){
		System.out.println(sequenceService.getSequenceId("UniqueId"));;
	}
}
