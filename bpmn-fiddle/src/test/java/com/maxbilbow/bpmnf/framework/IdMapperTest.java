package com.maxbilbow.bpmnf.framework;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IdMapperTest
{
  
  private static final String PATTERN = "_pattern__%s__";
  private static final String XML = " <serviceTask id=\"_002\" name=\"Suppress outbound industry flow and raise alert MRV_002\" ></serviceTask>" +
          " \n<serviceTask id=\"_003\" name=\"Suppress outbound industry flow and raise alert MRV_003\" ></serviceTask>" +
          " \n<serviceTask id=\"_005\" name=\"Suppress outbound industry flow and raise alert MRV_005\" ></serviceTask>" +
          " \n<serviceTask id=\"_007\" name=\"Suppress outbound industry flow and raise alert MRV_007\" ></serviceTask>" +
          " \n<serviceTask id=\"_009\" name=\"Suppress outbound industry flow and raise alert MRV_009\" ></serviceTask>" +
          " \n<serviceTask id=\"_010\" name=\"Suppress outbound industry flow and raise alert MRV_010\" ></serviceTask>";
  
  private static final List<int[]> PASS_1_RESULT = Arrays.asList(
          new int[] {2,4},
          new int[] {3,5},
          new int[] {5,6},
          new int[] {9,8},
          new int[] {10,9}
  );
  
  private static final String PASS_2_RESULT = " <serviceTask id=\"_pattern__AAE__\" name=\"Suppress outbound industry flow and raise alert MRV_pattern__AAE__\" ></serviceTask>"+
          " \n<serviceTask id=\"_pattern__AAF__\" name=\"Suppress outbound industry flow and raise alert MRV_pattern__AAF__\" ></serviceTask>"+
          " \n<serviceTask id=\"_pattern__AAG__\" name=\"Suppress outbound industry flow and raise alert MRV_pattern__AAG__\" ></serviceTask>"+
          " \n<serviceTask id=\"_007\" name=\"Suppress outbound industry flow and raise alert MRV_007\" ></serviceTask>"+
          " \n<serviceTask id=\"_pattern__AAI__\" name=\"Suppress outbound industry flow and raise alert MRV_pattern__AAI__\" ></serviceTask>"+
          " \n<serviceTask id=\"_pattern__AAJ__\" name=\"Suppress outbound industry flow and raise alert MRV_pattern__AAJ__\" ></serviceTask>"
          ;
  
  private static final String PASS_3_RESULT = " <serviceTask id=\"_004\" name=\"Suppress outbound industry flow and raise alert MRV_004\" ></serviceTask>"+
          " \n<serviceTask id=\"_005\" name=\"Suppress outbound industry flow and raise alert MRV_005\" ></serviceTask>" +
          " \n<serviceTask id=\"_006\" name=\"Suppress outbound industry flow and raise alert MRV_006\" ></serviceTask>"+
          " \n<serviceTask id=\"_007\" name=\"Suppress outbound industry flow and raise alert MRV_007\" ></serviceTask>"+
          " \n<serviceTask id=\"_008\" name=\"Suppress outbound industry flow and raise alert MRV_008\" ></serviceTask>" +
          " \n<serviceTask id=\"_009\" name=\"Suppress outbound industry flow and raise alert MRV_009\" ></serviceTask>";
  
  private static final  int FIRST = 2, SHIFT = 2, PADDING = 3;
  private static final String PFX = "_", SFX = "";
  
  private IdMapper mapper;
  
  @Before
  public void setUp() throws Exception
  {
    mapper = new IdMapper(PFX,SFX,PADDING,PATTERN);
  }
  
  @After
  public void tearDown() throws Exception
  {
  }
  
  @Test
  public void firstPass()
  {
    final List<int[]> result = (List<int[]>) mapper.firstPass(XML,FIRST,SHIFT);
    Assert.assertEquals(PASS_1_RESULT.size(), result.size());
    for (int i=0;i<result.size();++i)
      Assert.assertArrayEquals(PASS_1_RESULT.get(i),result.get(i));
  }
  
  @Test
  public void secondPass()
  {
    final String result = mapper.secondPass(XML,PASS_1_RESULT);
    Assert.assertEquals(PASS_2_RESULT, result);
  }
  
  @Test
  public void thirdPass()
  {
    final String result = mapper.thirdPass(PASS_2_RESULT);
    Assert.assertEquals(PASS_3_RESULT, result);
  }
}