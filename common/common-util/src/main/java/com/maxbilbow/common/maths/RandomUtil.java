package com.maxbilbow.common.maths;

import com.maxbilbow.common.converter.NumberConverter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class RandomUtil
{
  
  private static Reference<NumberConverter> weakConverter;
  
  private static Number convert(Number aNumber, Class<? extends Number> aClass)
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
    
    return Math.round((Math.random() * max)) + min;
  }
  
  public static <N extends Number> N between(N aMin, N aMax)
  {
    Double min = (Double) convert(Objects.requireNonNull(aMin),Double.class);
    Double max = (Double) convert(Objects.requireNonNull(aMax),Double.class);
    if (min.compareTo(max) == 0)
      return aMin;
    
    if (min.compareTo(max) > 0)
    {
      final Double tmp = min;
      min = max;
      max = tmp;
    }
    
    
    return (N) convert(Math.round((Math.random() * max)) + min, aMax.getClass());
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
    
    return Math.round((Math.random() * max)) + min;
  }
}
