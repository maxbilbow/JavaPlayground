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
      BrowserUtil.localHost(context.getEnvironment());
    }
    catch (Exception aE)
    {
      logger.error("BIG ERROR", aE);
    }
  
  }
}
