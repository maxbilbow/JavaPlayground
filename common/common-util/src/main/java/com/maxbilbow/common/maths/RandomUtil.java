package com.maxbilbow.common.maths;

import com.maxbilbow.common.converter.NumberConverter;
import com.maxbilbow.common.converter.ObjectConversionException;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.Objects;

public class RandomUtil
{
  
  private static Reference<NumberConverter> weakConverter;
  
  private static <N extends Number> N convert(Number aNumber, Class<N> aClass) throws ObjectConversionException
  {
    NumberConverter converter;
    if (weakConverter == null)
      converter = null;
    else
      converter = weakConverter.get();
    
    if (converter == null)
      weakConverter = new WeakReference<>(converter = new NumberConverter(true,true, false));
    
    return converter.convert(aNumber, aClass);
    
  }
  
  public static byte between(byte min, byte max)
  {
    return (byte) between((long) min, (long) max);
  }
  
  public static short between(short min, short max)
  {
    return (short) between((long) min, (long) max);
  }
  
  public static int between(int min, int max)
  {
    long result = between((long) min, (long) max);
    return (int) result;//(int) between((long) min, (long) max);
  }
  
  public static long between(long min, long max)
  {
    if (min == max)
      return min;
  
    if (min > max)
    {
      final long tmp = min;
      min = max;
      max = tmp;
    }
  
    return Math.round(betweenDouble(min,max));
  }
  
  public static float between(float min, float max)
  {
    return (float) between((double) min, (double) max);
  }
  
  public static double between(double min, double max)
  {
    if (min == max)
      return min;
    
    if (min > max)
    {
      final double tmp = min;
      min = max;
      max = tmp;
    }
    
    if (max-min < Double.MAX_VALUE)
      return betweenDouble(min, max);
    else
      return betweenBigDecimal(BigDecimal.valueOf(min), BigDecimal.valueOf(max)).doubleValue();
  }
  
  public static <N extends Number> N between(N aMin, N aMax)
  {
    try
    {
      @SuppressWarnings("unchecked")
      final Class<N> nClass = (Class<N>) aMax.getClass();
      final BigDecimal min = convert(Objects.requireNonNull(aMin), BigDecimal.class);
      final BigDecimal max = convert(Objects.requireNonNull(aMax), BigDecimal.class);
      if (min.compareTo(DecimalUtils.MAX_DOUBLE) < 0 && min.compareTo(DecimalUtils.MIN_DOUBLE) > 0
              && max.compareTo(DecimalUtils.MAX_DOUBLE) < 0 && max.compareTo(DecimalUtils.MIN_DOUBLE) > 0)
        return convert(between(min.doubleValue(),max.doubleValue()),nClass);
      
      return convert(betweenBigDecimal(min, max), nClass);
    }
    catch (ObjectConversionException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private static double betweenDouble(double min, double max)
  {
    max -= 1.0;//to keep within bounts
    final double diff;
    {
      final double diff1 = 1 - min;//make min non-negative
      double diff2 = 0; //to cope with zero value
  
      min += diff1;
      max += diff1;
  
      if (Double.compare(max, 0) == 0)
        diff2 = 1;
  
      min += diff2;
      max += diff2;
  
      diff = diff1 + diff2;
    }
  
    return (min + (Math.random() * max)) - diff;
  }
  
  private static BigDecimal betweenBigDecimal(BigDecimal min, BigDecimal max)
  {
    max = max.subtract(BigDecimal.ONE);
    final BigDecimal diff;
    {
      BigDecimal diff1 = BigDecimal.ONE.subtract(min),//Set as 1.
              diff2 = BigDecimal.ZERO; //to cope with zero value
     
      
      min = min.add(diff1);
      max = max.add(diff1);
    
      if (max.compareTo(BigDecimal.ZERO) == 0)
        diff2 = BigDecimal.ONE;
    
      min = min.add(diff2);
      max = max.add(diff2);
    
      diff = diff1.add(diff2);
    }
    
    
    
    return min.add((BigDecimal.valueOf(Math.random()).multiply(max))).subtract(diff);
  }
  
  private static BigDecimal betweenDecimal(final BigDecimal aMIN, final BigDecimal aMAX)
  {
    BigDecimal min = aMIN,max = aMAX;
    if (min.compareTo(max) == 0)
      return min;
  
    if (min.compareTo(max) > 0)
    {
      final BigDecimal tmp = min;
      min = max;
      max = tmp;
    }
    
    final BigDecimal diff = BigDecimal.ONE.subtract(min);
    
    min = min.add(diff);
    max = max.add(diff);
  
    BigDecimal result;
    if (max.compareTo(DecimalUtils.MAX_DOUBLE) < 0)
      result = BigDecimal.valueOf(betweenDouble(min.doubleValue(),max.doubleValue()));
    else
    {
      max = max.subtract(BigDecimal.ONE);
      result = min.add((BigDecimal.valueOf(Math.random()).multiply(max)));
    }
  
    result = result.subtract(diff);
    
  /*  if (result.compareTo(aMAX) > 0 || result.compareTo(aMIN) < 0)
      throw new RuntimeException("Result not within bounds! " + result);*/
    
    return result;
  }
  
  
  
  
  
}
