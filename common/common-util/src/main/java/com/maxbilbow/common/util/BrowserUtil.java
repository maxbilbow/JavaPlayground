package com.maxbilbow.common.util;

import com.google.gag.annotation.enforceable.Noop;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.PropertyResolver;

import java.awt.*;
import java.io.IOException;
import java.net.URI;


public class BrowserUtil
{
  @Noop("Doesn't really work atm")
  public static void localHost() throws IOException
  {
    final String port = System.getProperty("server.port", SpringProperties.getProperty("server.port"));
    final String ctx = System.getProperty("server.contextPath", SpringProperties.getProperty("server.contextPath"));
    localHost(port != null ? Integer.parseInt(port) : 8080, ctx);
  }
  
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
  public static void localHost(final int port, final String ctx) throws IOException
  {
    String url = "http://localhost:" + port;
    if (ctx != null)
      url += ctx.startsWith("/") ? ctx : "/" + ctx;
    launch(url);
  }
  
  public static void launch(final String aPath) throws IOException
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