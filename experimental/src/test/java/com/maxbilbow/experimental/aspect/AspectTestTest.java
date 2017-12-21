package com.maxbilbow.experimental.aspect;

import org.junit.Assert;
import org.junit.Test;

public class AspectTestTest
{
  @Test
  public void getString()
  {
    final String s = new AspectTest().getString();
    Assert.assertEquals("aspect",s);
  }
}