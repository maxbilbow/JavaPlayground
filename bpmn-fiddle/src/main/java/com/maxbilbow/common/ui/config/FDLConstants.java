package com.maxbilbow.common.ui.config;

import java.nio.charset.Charset;

public class FDLConstants
{
  private static final Charset sDefaultCharset = Charset.forName("UTF-8");
  public static Charset defaultCharset()
  {
    return sDefaultCharset;
  }
}
