package com.maxbilbow.pg1;

import com.maxbilbow.common.util.ProjectUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class PG1ApplicationTest
{
  
  @Test
  public void test() throws IOException, URISyntaxException
  {
    Assert.assertTrue(ProjectUtils.getMavenProjectPath("").endsWith("Playground1"));
  }
}