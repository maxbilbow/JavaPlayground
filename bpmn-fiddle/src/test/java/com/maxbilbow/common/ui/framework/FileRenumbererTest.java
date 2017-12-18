package com.maxbilbow.common.ui.framework;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileRenumbererTest
{
  
  private FileRenumberer mFileRenumberer;
  
  private static Path MRV;
  
  @BeforeClass
  public static void beforeClass() throws URISyntaxException
  {
    MRV = Paths.get(Objects.requireNonNull(FileRenumbererTest.class.getClassLoader().getResource("MRV.bpmn")).toURI());
  }
  
  @Before
  public void setUp() throws Exception
  {
    mFileRenumberer = new FileRenumberer();
    ReflectionTestUtils.setField(mFileRenumberer,"mRenumberer", new Renumberer());
    ReflectionTestUtils.setField(mFileRenumberer,"prefix","_");
    ReflectionTestUtils.setField(mFileRenumberer,"padding",3);
  
  }
  
  @Test
  public void renumberAfms() throws IOException
  {
    mFileRenumberer.renumberAfms(MRV,2,3);
  }
}