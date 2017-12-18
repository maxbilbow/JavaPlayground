package com.maxbilbow.common.ui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bilbowm (Max Bilbow) on 04/01/2016.
 */
public class OSUtil
{
  private static final String OS;
  
  static
  {
    final Logger logger = LoggerFactory.getLogger(OSUtil.class);
    OS = System.getProperty("os.name").toLowerCase();
    if (isWindows()) {
      logger.info("OS is Windows");
    } else if (isMac()) {
      logger.info("OS is Mac");
    } else if (isUnix()) {
      logger.info("OS is Unix or Linux");
    } else if (isSolaris()) {
      logger.info("OS is Solaris");
    } else {
      logger.warn("OS ("+OS+")is not known to "+OSUtil.class.getSimpleName());
    }
  }
  
  public static boolean isWindows() {

    return (OS.contains("win"));

  }
  
  public static boolean isMac() {

    return (OS.contains("mac"));

  }

  public static boolean isUnix() {

    return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));

  }
  
  public static boolean isSolaris() {

    return (OS.contains("sunos"));

  }

}