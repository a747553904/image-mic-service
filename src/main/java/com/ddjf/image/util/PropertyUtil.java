package com.ddjf.image.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class PropertyUtil {
	
	private String folderStr;

	public String getFolderStr() {
		return folderStr;
	}

	public void setFolderStr(String folderStr) {
		this.folderStr = folderStr;
	}
	
}
