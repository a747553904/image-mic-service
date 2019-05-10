package com.ddjf.image.util;

import com.ddjf.image.service.SequenceService;

public class UniqueIdUtil {

	public static synchronized int getId(){
		SequenceService sequenceService = SpringContextUtil.getBean("sequenceService", SequenceService.class);
		return sequenceService.getSequenceId("UniqueId");
	}
	
	public static String getUniqueId(){
		long id = System.currentTimeMillis() + getId();
		return Long.toString(id);
	}
}
