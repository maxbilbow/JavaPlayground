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
    
    return betweenDouble(min, max);
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
      
      return convert(betweenDecimal(min, max), nClass);
    }
    catch (ObjectConversionException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private static double betweenDouble(double min, double max)
  {
    max -= 1.0;
    double diff = 0; //to cope with zero value
    if (Double.compare(min,0.0) <= 0)
    {
      diff = 1 - min;
      min+=diff;
      max+=diff;
    }
    return (min + (Math.random() * max)) - diff;
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
