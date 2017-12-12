package com.maxbilbow.pg1;

import com.maxbilbow.common.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class PG1ApplicationTest
{
  
  @Test
  public void test() throws IOException, URISyntaxException
  {
    Assert.assertTrue(FileUtils.getMavenProjectPath("").endsWith("Playground1"));
  }
}