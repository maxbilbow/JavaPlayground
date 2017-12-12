package com.maxbilbow.pg1;

import com.maxbilbow.common.util.BrowserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PG1Application
{
  public static void main(String[] args) {
    final Logger logger = LoggerFactory.getLogger(PG1Application.class);
    try
    {
      logger.info("Launching BFP");
      final ConfigurableApplicationContext context = SpringApplication.run(PG1Application.class, args);
    
      final int port = context.getEnvironment().getProperty("server.port",Integer.class,8080);
      final String ctxPath = context.getEnvironment().getProperty("server.contextPath");
      logger.info("Launching Browser on port: " + port + ", path: " + ctxPath);
      BrowserUtil.launch(port,ctxPath);
    }
    catch (Exception aE)
    {
      logger.error("BIG ERROR", aE);
    }
  
  }
}
