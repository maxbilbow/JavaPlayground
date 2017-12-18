package com.maxbilbow.common.ui.web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;

@Controller
@RequestMapping(value = "/common-ui")
public class ResourceController
{
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private Map<String,String> resources = new WeakHashMap<>();
  
  private Charset charset = Charset.defaultCharset();
  
  public ResourceController()
  {
    logger.info("This worked!");
  }
  
  @RequestMapping
  public @ResponseBody String get()
  {
    return "Hello.";
  }
  
  @RequestMapping(params = "src")
  public @ResponseBody String getJs(@RequestParam("src") final String file)
  {
    logger.info("Searching for resource: " + file);
    return resources.computeIfAbsent(file,f -> {
      try (final InputStream startsWithStream = this.getClass().getResourceAsStream("/static/"+file))
      {
        return IOUtils.toString(startsWithStream, charset);
      }
      catch (IOException aE)
      {
        logger.error("",aE);
        return null;
      }
    });
   
  }
}
