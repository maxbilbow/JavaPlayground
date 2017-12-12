package com.maxbilbow.common.maths;

import org.apache.commons.math.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.WeakHashMap;

public class DecimalUtils
{
  public final static BigDecimal MAX_INT = BigDecimal.valueOf(Integer.MAX_VALUE);
  public final static BigDecimal MIN_INT = BigDecimal.valueOf(Integer.MIN_VALUE);
  
  public final static BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
  public final static BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);
  
  public final static BigDecimal MAX_SHORT = BigDecimal.valueOf(Short.MAX_VALUE);
  public final static BigDecimal MIN_SHORT = BigDecimal.valueOf(Short.MIN_VALUE);
  
  public final static BigDecimal MAX_BYTE = BigDecimal.valueOf(Byte.MAX_VALUE);
  public final static BigDecimal MIN_BYTE = BigDecimal.valueOf(Byte.MIN_VALUE);
  
  public final static BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);
  public final static BigDecimal MIN_DOUBLE = BigDecimal.valueOf(Double.MIN_VALUE);
  
  public final static BigDecimal MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);
  public final static BigDecimal MIN_FLOAT = BigDecimal.valueOf(Float.MIN_VALUE);
  
  private static final Map<Class, BigDecimal> minValueMap = new WeakHashMap<>();
  private static final Map<Class, BigDecimal> maxValueMap = new WeakHashMap<>();
  
  public static BigDecimal maxValue(Class<? extends Number> aClass)
  {
    if (aClass == BigDecimal.class || aClass == BigInteger.class)
      return null;
  
    return maxValueMap.computeIfAbsent(aClass,n -> {
      if (n.getSimpleName().contains("Double"))
        return MAX_DOUBLE;
      if (n.getSimpleName().contains("Long"))
        return MAX_LONG;
      if (n.getSimpleName().contains("Float"))
        return MAX_FLOAT;
      if (n.getSimpleName().contains("Integer"))
        return MAX_INT;
      if (n.getSimpleName().contains("Short"))
        return MAX_SHORT;
      if (n.getSimpleName().contains("Byte"))
        return MAX_BYTE;
      try
      {
        return new BigDecimal(aClass.getField("MAX_VALUE").get(null).toString());
      }
      catch (IllegalAccessException | NoSuchFieldException aE)
      {
        throw new IllegalArgumentException(aE);
      }
    });
  }
  
  public static BigDecimal minValue(Class<? extends Number> aClass)
  {
    if (aClass == BigDecimal.class || aClass == BigInteger.class || aClass == BigFraction.class)
      return null;
    
    return minValueMap.computeIfAbsent(aClass, n -> {
      if (n.getSimpleName().contains("Double"))
        return MIN_DOUBLE;
      if (n.getSimpleName().contains("Long"))
        return MIN_LONG;
      if (n.getSimpleName().contains("Float"))
        return MIN_FLOAT;
      if (n.getSimpleName().contains("Integer"))
        return MIN_INT;
      if (n.getSimpleName().contains("Short"))
        return MIN_SHORT;
      if (n.getSimpleName().contains("Byte"))
        return MIN_BYTE;
      try
      {
        return new BigDecimal(aClass.getField("MIN_VALUE").get(null).toString());
      }
      catch (IllegalAccessException | NoSuchFieldException aE)
      {
        throw new IllegalArgumentException(aE);
      }
    });
  }
  
}
