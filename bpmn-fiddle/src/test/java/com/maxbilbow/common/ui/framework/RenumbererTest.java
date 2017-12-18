package com.maxbilbow.common.ui.framework;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RenumbererTest
{
 
  private static final String PATTERN = "_pattern__%s__";
 
  private static final String INPUT =
          " <serviceTask id=\"_002\" name=\"Suppress outbound industry flow and raise alert MRV_002\" ></serviceTask>" +
                  " \n<serviceTask id=\"_003\" name=\"Suppress outbound industry flow and raise alert MRV_003\" ></serviceTask>" +
                  " \n<serviceTask id=\"_005\" name=\"Suppress outbound industry flow and raise alert MRV_005\" ></serviceTask>" +
                  " \n<serviceTask id=\"_007\" name=\"Suppress outbound industry flow and raise alert MRV_007\" ></serviceTask>";
  private static final String PFX = "_", SFX = "";
  
  private static final int PADDING = 3;
  
  private Renumberer mRenumberer = new Renumberer();
  
  private final String xml, expected, message;
  
  private final int first, shift;
  
  public RenumbererTest(final String aXml,
                        final String aExpected,
                        final int aFirst,
                        final int aShift,
                        final String aMessage)
  {
    xml = aXml;
    expected = aExpected;
    message = aMessage;
    first = aFirst;
    shift = aShift;
  }

  @Before
  public void setUp()
  {
  }
 
  @Test
  public void renumber()
  {
    final String result = mRenumberer.renumber(xml,first,shift,PFX,SFX,PADDING);
    Assert.assertEquals(message,expected,result);
  }
  
  
  @Parameters
  public static List<Object[]> getParams()
  {
   
    final List<Object[]> params = new ArrayList<>();
    {
      
      final String RESULT =
              " <serviceTask id=\"_004\" name=\"Suppress outbound industry flow and raise alert MRV_004\" ></serviceTask>" +
                      " \n<serviceTask id=\"_005\" name=\"Suppress outbound industry flow and raise alert MRV_005\" ></serviceTask>" +
                      " \n<serviceTask id=\"_006\" name=\"Suppress outbound industry flow and raise alert MRV_006\" ></serviceTask>" +
                      " \n<serviceTask id=\"_007\" name=\"Suppress outbound industry flow and raise alert MRV_007\" ></serviceTask>";
  
     final int FIRST = 2, SHIFT = 2;
     params.add(new Object[]{INPUT,RESULT,FIRST,SHIFT,"Reordering from 2, shift 2"});
    }
  
    {
    
      final String RESULT =
              " <serviceTask id=\"_001\" name=\"Suppress outbound industry flow and raise alert MRV_001\" ></serviceTask>" +
                      " \n<serviceTask id=\"_002\" name=\"Suppress outbound industry flow and raise alert MRV_002\" ></serviceTask>" +
                      " \n<serviceTask id=\"_003\" name=\"Suppress outbound industry flow and raise alert MRV_003\" ></serviceTask>" +
                      " \n<serviceTask id=\"_004\" name=\"Suppress outbound industry flow and raise alert MRV_004\" ></serviceTask>";
    
      final int FIRST = 1, SHIFT = 0;
      params.add(new Object[]{INPUT,RESULT,FIRST,SHIFT,"Reordering from 2, shift 2"});
    }
  
    {
  
      final String RESULT =
              " <serviceTask id=\"_002\" name=\"Suppress outbound industry flow and raise alert MRV_002\" ></serviceTask>" +
                      " \n<serviceTask id=\"_003\" name=\"Suppress outbound industry flow and raise alert MRV_003\" ></serviceTask>" +
                      " \n<serviceTask id=\"_004\" name=\"Suppress outbound industry flow and raise alert MRV_004\" ></serviceTask>" +
                      " \n<serviceTask id=\"_005\" name=\"Suppress outbound industry flow and raise alert MRV_005\" ></serviceTask>";
    
      final int FIRST =4, SHIFT = 0;
      params.add(new Object[]{INPUT,RESULT,FIRST,SHIFT,"Reordering from 2, shift 2"});
    }
  
  
    {
    
      final String RESULT =
              " <serviceTask id=\"_002\" name=\"Suppress outbound industry flow and raise alert MRV_002\" ></serviceTask>" +
                      " \n<serviceTask id=\"_003\" name=\"Suppress outbound industry flow and raise alert MRV_003\" ></serviceTask>" +
                      " \n<serviceTask id=\"_005\" name=\"Suppress outbound industry flow and raise alert MRV_005\" ></serviceTask>" +
                      " \n<serviceTask id=\"_006\" name=\"Suppress outbound industry flow and raise alert MRV_006\" ></serviceTask>";
    
      final int FIRST =4, SHIFT = 1;
      params.add(new Object[]{INPUT,RESULT,FIRST,SHIFT,"Reordering from 2, shift 2"});
    }
  
    {
    
      final String RESULT =
              " <serviceTask id=\"_002\" name=\"Suppress outbound industry flow and raise alert MRV_002\" ></serviceTask>" +
                      " \n<serviceTask id=\"_003\" name=\"Suppress outbound industry flow and raise alert MRV_003\" ></serviceTask>" +
                      " \n<serviceTask id=\"_006\" name=\"Suppress outbound industry flow and raise alert MRV_006\" ></serviceTask>" +
                      " \n<serviceTask id=\"_007\" name=\"Suppress outbound industry flow and raise alert MRV_007\" ></serviceTask>";
    
      final int FIRST =5, SHIFT = 1;
      params.add(new Object[]{INPUT,RESULT,FIRST,SHIFT,"Reordering from 2, shift 2"});
    }
  
  
    return params;
  }
  
}