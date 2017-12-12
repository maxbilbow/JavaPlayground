package com.maxbilbow.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class BrowserUtil
{
  
  public static void launch()
  {
    final String port = System.getProperty("server.port");
    final String ctx = System.getProperty("server.contextPath");
    String url = "localhost:"+ (port != null ? port : "80");
    if (ctx != null)
      url += ctx.startsWith("/") ? ctx : "/" + ctx;
    launch(url);
  }
  
  /**
   * Launch on localhost:port
   * @param port
   */
  public static void launch(int port, final String ctx)
  {
    String url = "localhost:"+ port;
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