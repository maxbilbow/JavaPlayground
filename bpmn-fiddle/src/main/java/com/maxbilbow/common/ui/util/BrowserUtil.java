package com.maxbilbow.common.ui.util;

import org.springframework.core.env.PropertyResolver;

import java.awt.*;
import java.io.IOException;
import java.net.URI;


public class BrowserUtil
{
  
  public static void localHost(PropertyResolver properties) throws IOException
  {
    final int port = properties.getProperty("server.port", Integer.class, 8080);
    final String ctx = properties.getProperty("server.contextPath", "/");
    localHost(port, ctx);
  }
  
  /**
   * Launch on localhost:port
   *
   * @param port
   */
  static void localHost(final int port, final String ctx) throws IOException
  {
    String url = "http://localhost:" + port;
    if (ctx != null)
      url += ctx.startsWith("/") ? ctx : "/" + ctx;
    launch(url);
  }
  
  static void launch(final String aPath) throws IOException
  {
    launch(URI.create(aPath));
  }
  
  public static void launch(final URI aURI) throws IOException
  {
    if (Desktop.isDesktopSupported())
    {
      Desktop desktop = Desktop.getDesktop();
      desktop.browse(aURI);
    }
    else if (OSUtil.isWindows())
    {
      final String[] cmd = new String[4];
      cmd[0] = "cmd.exe";
      cmd[1] = "/C";
      cmd[2] = "start";
      cmd[3] = aURI.toString();
      Runtime.getRuntime().exec(cmd);
    }
    else
    {
      final String cmd = OSUtil.isMac() ? "open " : "xdg-open ";
      Runtime.getRuntime().exec(cmd + aURI.toString());
    }
  }
}