package com.maxbilbow.common.maths;

import org.apache.commons.lang3.math.Fraction;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class RandomUtilTest
{
  
  @Test
  public void betweenByte()
  {
    final byte min = -5, max = 1;
    boolean[] hits = new boolean[max - min];
    for (int i = 0; i < 1000; ++i)
    {
      final byte result = RandomUtil.between(min, max);
      System.out.println(result);
      final int idx = result - min;
      if (idx < hits.length)
        hits[idx] = true;
      Assert.assertTrue("Between " + min + " and " + max + ": " + result, result >= min && result <= max);
    }
    for (int i = min; i < max; ++i)
      Assert.assertTrue((i) + "hit", hits[i - min]);
  }
  
  @Test
  public void betweenShort()
  {
    final short min = -5, max = 1;
    boolean[] hits = new boolean[max - min];
    for (int i = 0; i < 1000; ++i)
    {
      final short result = RandomUtil.between(min, max);
      System.out.println(result);
      final int idx = result - min;
      if (idx < hits.length)
        hits[idx] = true;
      Assert.assertTrue("Between " + min + " and " + max + ": " + result, result >= min && result <= max);
    }
    for (int i = min; i < max; ++i)
      Assert.assertTrue((i) + "hit", hits[i - min]);
  }
  
  @Test
  public void betweenInt()
  {
    testIntBound(0,0);
    testIntBound(0,1);
    testIntBound(-1,1);
    testIntBound(-1,0);
    testIntBound(-100,0);
    testIntBound(-100,100);
    testIntBound(Integer.MIN_VALUE,Integer.MIN_VALUE+2);
    testIntBound(Integer.MAX_VALUE-2,Integer.MAX_VALUE);
    testIntBound(-100,Integer.MAX_VALUE/10000);
  }
  
  private void testIntBound(final int min, final int max)
  {
    long range = (long)max-min;
    if (range > (Integer.MAX_VALUE / 100))
      throw new AssertionError("Range was too large! " + range);
    System.out.println("Assessing random values on range of " + range);
    boolean[] hits = new boolean[(int) range];
    final int maxIterations = hits.length*100;
    final int halfWay = maxIterations / 2;
    for (int i = 0; i < maxIterations; ++i)
    {
      final int result;
      if (i < halfWay)
        result = RandomUtil.between(min,max);
      else
        result = RandomUtil.between(max,min); // test switching
      final int idx = result-min;
      if (idx<hits.length)
        hits[idx] = true;
      Assert.assertTrue("Between "+min+" and "+max+": " + result, result >= min && result <= max);
    }
    for (int i=min;i<max;++i)
      Assert.assertTrue((i) + " not hit!", hits[i-min]);
  }
  
  @Test
  public void betweenLong()
  {
    {
      boolean oneHit = false, twoHit = false;
      for (int i = 0; i < 1000; ++i)
      {
        final long result = RandomUtil.between(1L, 2L);
        if (result == 1L)
          oneHit = true;
        if (result == 2L)
          twoHit = true;
        Assert.assertTrue("Between 1L and 2L: " + result, result == 1L || result == 2L);
      }
      Assert.assertTrue("1 hit", oneHit);
      Assert.assertTrue("2 hit", twoHit);
    }
  
    {
      boolean minusOneHit = false, zeroHit = false;
      for (int i = 0; i < 1000; ++i)
      {
        final long result = RandomUtil.between(-1L, 0L);
        if (result == -1L)
          minusOneHit = true;
        if (result == 0L)
          zeroHit = true;
        Assert.assertTrue("Between -1L and 0L: " + result, result == -1L || result == 0L);
      }
      Assert.assertTrue("0 hit", zeroHit);
      Assert.assertTrue("-1 hit", minusOneHit);
    }
  
    {
      boolean minusOneHit = false, zeroHit = false, oneHit = false, twoHit = false;
      for (int i = 0; i < 1000; ++i)
      {
        final long result = RandomUtil.between(-1L, 2L);
        if (result == -1L)
          minusOneHit = true;
        else if (result == 0L)
          zeroHit = true;
        else if (result == 1L)
          oneHit = true;
        else if (result == 2L)
          twoHit = true;
        Assert.assertTrue("Between -1L and 2L: " + result, result >= -1L && result <= 2L);
      }
      Assert.assertTrue("0 hit", zeroHit);
      Assert.assertTrue("-1 hit", minusOneHit);
      Assert.assertTrue("1 hit", oneHit);
      Assert.assertTrue("2 hit", twoHit);
    }
  }
  
  @Test
  public void betweenFloat()
  {
    final float min = -5, max = 1;
    boolean[] hits = new boolean[(int) (max - min)];
    for (int i = 0; i < 1000; ++i)
    {
      final float result = RandomUtil.between(min, max);
//      System.out.println(result);
      final float idx = result - min;
      if (idx < hits.length)
        hits[(int) idx] = true;
      Assert.assertTrue("Between " + min + " and " + max + ": " + result, result >= min && result <= max);
    }
    for (int i = (int) min; i < max; ++i)
      Assert.assertTrue((i) + "hit", hits[(int) (i - min)]);
  }
  
  
  @Test
  public void betweenDouble()
  {
    {
      boolean oneHit = false, twoHit = false;
      for (int i = 0; i < 1000; ++i)
      {
        final double result = RandomUtil.between(1.0d, 2.0d);
        final long rounded = Math.round(result);
        if (rounded == 1L)
          oneHit = true;
        if (rounded == 2L)
          twoHit = true;
        Assert.assertTrue("Between 1.0 and 2.0: " + result, result >= 1.0 && result <= 2.0);
      }
      Assert.assertTrue("1 hit", oneHit);
      Assert.assertTrue("2 hit", twoHit);
    }
  
    for (int i=0;i<1000;++i)
    {
      final double result = RandomUtil.between(1.0,1.1);
//      System.out.println(result);
      Assert.assertTrue("Between 1.0 and 1.1: " + result,result >= 1.0 && result <= 1.1);
    }
  
    for (int i=0;i<1000;++i)
    {
      final double result = RandomUtil.between(0.0,0.1);
//      System.out.println(result);
      Assert.assertTrue("Between 0.0 and 0.1: " + result,result >= 0.0 && result <= 0.1);
    }
  
    {
     
      for (int i = 0; i < 1000; ++i)
      {
        final double result = RandomUtil.between(-0.2, -0.1);
       
//      System.out.println(result);
        Assert.assertTrue("Between -0.2 and -0.1: " + result, result >= -0.2 && result <= -0.1);
      }
    }
  
    {
      boolean belowZero = false, aboveZero = false;
      for (int i = 0; i < 1000; ++i)
      {
        final double result = RandomUtil.between(-0.2, 0.1);
        if (result < 0)
          belowZero = true;
        if (result > 0)
          aboveZero = true;
        Assert.assertTrue("Between -0.2 and 0.1: " + result, result >= -0.2 && result <= 0.1);
      }
      Assert.assertTrue("> 0 hit",aboveZero);
      Assert.assertTrue("< 0 hit",belowZero);
    }
  }
  
  @Test
  public void betweenBigDecimal()
  {
    for (int i=0;i<1000;++i)
    {
      final BigDecimal result = RandomUtil.between(BigDecimal.ONE,BigDecimal.valueOf(2));
      Assert.assertTrue(result.compareTo(BigDecimal.ONE) >= 0);
      Assert.assertTrue(result.compareTo(BigDecimal.valueOf(2)) <= 0);
    }
  
    {
      final BigDecimal maxPlus1 = DecimalUtils.MAX_DOUBLE.add(BigDecimal.ONE);
      final BigDecimal maxPlus10 = DecimalUtils.MAX_DOUBLE.add(BigDecimal.TEN);
      testBounds(maxPlus1,maxPlus10);
    }
  
    {
      final BigDecimal minMinus1 = DecimalUtils.MIN_DOUBLE.subtract(BigDecimal.ONE);
      final BigDecimal maxPlus1 = DecimalUtils.MAX_DOUBLE.add(BigDecimal.ONE);
      testBounds(minMinus1,maxPlus1);
    }
    
    testBounds(DecimalUtils.MIN_DOUBLE.multiply(BigDecimal.valueOf(2)),
            DecimalUtils.MAX_DOUBLE.multiply(BigDecimal.valueOf(2)));
  }
  
  private <N extends Number> void testBounds(final Comparable<N> min, final Comparable<N> max)
  {
    for (int i = 0; i < 1000; ++i)
    {
      final N result = RandomUtil.between((N) min, (N) max);
//      System.out.println(result);
      Assert.assertTrue(String.format("Result is LESS than MIN value%nMIN: %s%nACTUAL: %s",min,result),min.compareTo(result) <= 0);
      Assert.assertTrue(String.format("Result is GREATER than MAX value%nMAX: %s%nACTUAL: %s",max,result),max.compareTo(result) >= 0);
    }
  }
  
  @Test
  public void betweenNumber()
  {
    testBounds(Fraction.FOUR_FIFTHS,Fraction.ONE);
  }
}