package com.maxbilbow.common.misc;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

public class ChooserTest
{
  
  final Character[] values = {'A','B','C','D'};
  private Map<Character,Integer> hitMap;
  @Before
  public void setUp() throws Exception
  {
    hitMap = new HashMap<>();
    for (int i = 0;i<values.length;++i)
      hitMap.put(values[i],0);
  }
  
  @After
  public void assertAllHit()
  {
    Assert.assertEquals(values.length,hitMap.size());
    for (final Character value : values)
      Assert.assertTrue(hitMap.get(value) > 0);
  }
  
  @Test
  public void chooseVarargs()
  {
    final Character first = values[0];
    final Character[] others =  new Character[values.length-1];
    System.arraycopy(values, 1,others, 0, values.length-1);
    updateHits(() -> Chooser.choose(first,others));
  }
  
  private void updateHits(final Supplier<Character> choice)
  {
    for (int i = 0; i<100;++i)
    hitMap.compute(choice.get(), (c,value) -> ObjectUtils.defaultIfNull(value,0) + 1);
  }
  
  @Test
  public void chooseCollection()
  {
    final Collection<Character> collection = hitMap.keySet();
    updateHits(() -> Chooser.choose(collection));
  }
  
  @Test
  public void chooseList()
  {
    final List<Character> collection = Arrays.asList(values);
    updateHits(() -> Chooser.choose(collection));
  }
}