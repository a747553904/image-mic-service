package com.ddjf.image.util;

import org.junit.Test;

import com.ddjf.image.BaseTestCase;

public class UniqueIdUtilTest extends BaseTestCase {
	
	@Test
	public void getUniqueIdTest(){
		for(int i=0; i<100; i++){
			new Thread() {
				public void run(){
					System.out.println(UniqueIdUtil.getUniqueId());;
				}
			}.start();
		}
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
