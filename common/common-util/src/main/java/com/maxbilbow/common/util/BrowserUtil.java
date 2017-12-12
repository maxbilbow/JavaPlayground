package com.maxbilbow.common.util;

import com.google.gag.annotation.enforceable.Noop;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.PropertyResolver;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;


public class BrowserUtil
{
  @Noop("Doesn't really work atm")
  public static void localHost()
  {
    final String port = System.getProperty("server.port", SpringProperties.getProperty("server.port"));
    final String ctx = System.getProperty("server.contextPath", SpringProperties.getProperty("server.contextPath"));
    localHost(port,ctx);
  }
  
  public static void localHost(PropertyResolver properties)
  {
    final String port = properties.getProperty("server.port","8080");
    final String ctx = properties.getProperty("server.contextPath","/");
    localHost(port,ctx);
  }
  
  public static void localHost(Map properties)
  {
    final String port = (String) properties.get("server.port");
    final String ctx = (String) properties.get("server.contextPath");
    localHost(port,ctx);
  }
  
  /**
   * Launch on localhost:port
   * @param port
   */
  public static void localHost(final String port, final String ctx)
  {
    String url = "localhost:"+ StringUtils.defaultString(port,"80");
    if (ctx != null)
      url += ctx.startsWith("/") ? ctx : "/" + ctx;
    launch(url);
  }
  
  public static void launch(String address) {
    final String url;
    if (address.startsWith("http"))
      url = address;
    else
      url = "http://"+address;
    
    final Logger logger = LoggerFactory.getLogger(BrowserUtil.class);
    if(Desktop.isDesktopSupported()){
      Desktop desktop = Desktop.getDesktop();
      try {
        desktop.browse(new URI(url));
      } catch (IOException | URISyntaxException e) {
        logger.error("Browser launch error",e);
      }
    }else{
      Runtime runtime = Runtime.getRuntime();
//      String os = System.getProperty("os.name");
      if (OSUtil.isWindows()) {
        final String[] cmd = new String[4];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = "start";
        cmd[3] = url;
        try {
          runtime.exec(cmd);
        } catch (IOException e) {
          logger.error("Browser launch error",e);
        }
      }
      else {
        String cmd = OSUtil.isMac() ? "open " : "xdg-open ";
        try {
          runtime.exec(cmd + url);
        } catch (IOException e) {
          logger.error("Browser launch error",e);
        }
      }
    }
  }
}