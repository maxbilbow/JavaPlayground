package com.maxbilbow.bpmnf;

import com.maxbilbow.common.util.BrowserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BpmnFiddleApplication {

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(BpmnFiddleApplication.class);
		try
		{
			logger.info("Launching BPMN Fiddle");
			final ConfigurableApplicationContext context = SpringApplication.run(BpmnFiddleApplication.class, args);
			BrowserUtil.localHost(context.getEnvironment());
		}
		catch (Throwable aE)
		{
			logger.error("BIG OLD ERROR", aE);
		}
	}
}
