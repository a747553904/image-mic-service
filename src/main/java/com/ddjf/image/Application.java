package com.ddjf.image;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.catalina.connector.Connector;
import org.assertj.core.util.Compatibility;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author liuzh
 * @since 2015-12-12 18:22
 */
@Controller
@EnableWebMvc
@SpringBootApplication
@MapperScan(basePackages = "com.ddjf.image.mapper")
public class Application extends WebMvcConfigurerAdapter {



    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("===========执行相关代码===============");
        //执行完毕自动退出
        ExitCodeGenerator exitCodeGenerator = new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 200;
            }
        };
        System.out.println("===========退出springbott===============");
        SpringApplication.exit(ctx, exitCodeGenerator);
        System.out.println("===========退出springbott完毕===============");
    }
    
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {
    	return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if (container instanceof TomcatEmbeddedServletContainerFactory) {
					TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
	                tomcat.addConnectorCustomizers(getTomcatConnectorCustomizer());
				}
			}
    	};
    }
    
    private TomcatConnectorCustomizer getTomcatConnectorCustomizer(){
    	return new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				connector.setMaxPostSize(50000000); // 50 MB
			}
    	};
    }
    
    @RequestMapping("/")
    String home() {
        return "redirect:home";
    }
}
