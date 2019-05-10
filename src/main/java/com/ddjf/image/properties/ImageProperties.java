package com.ddjf.image.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ddjf.image.util.SpringContextUtil;

@Component
@ConfigurationProperties(prefix = "image.config")
public class ImageProperties {

	private String scriptPath;

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}
	
	
	public static ImageProperties INSTANCE(){
		return SpringContextUtil.getBean(ImageProperties.class);
	}
}
