package com.maxbilbow.common.converter;

import com.maxbilbow.common.maths.DecimalUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class NumberConverter implements Converter
{
  private boolean roundLargeValues = false;
  private boolean roundDecimals = true;
  private boolean allowOverflow = false;
  
  @Override
  public boolean accepts(final Object valueToConvert, final Class<?> toClass)
  {
    if (Number.class.isAssignableFrom(toClass))
      return true;
    else if (toClass == String.class)
      return valueToConvert == null || valueToConvert instanceof Number;
    
    return false;
  }
  
  public NumberConverter(final boolean aRoundLargeValues)
  {
    this(aRoundLargeValues,true,false);
  }
  
  public NumberConverter(final boolean aRoundLargeValues, final boolean aRoundDecimals)
  {
    this(aRoundLargeValues,aRoundDecimals,false);
  }
  
  public NumberConverter(boolean aRoundLargeValues, boolean aRoundDecimals, boolean aAllowOverflow)
  {
    roundLargeValues = aRoundLargeValues;
    roundDecimals = aRoundDecimals;
    allowOverflow = aAllowOverflow;
  }
  
  
  @Override
  @SuppressWarnings("unchecked")
  public <N> N convert(final Object object, final Class<N> toClass) throws ObjectConversionException
  {
    if (object == null)
      return null;
    
    if (toClass.isAssignableFrom(object.getClass()))
      return (N) object;
    
    if (toClass == String.class && object instanceof Number)
      return (N) toString((Number) object);
    if (object instanceof Number)
      return (N) convertNumber((Number) object, (Class<? extends Number>) toClass);
    else if (object instanceof CharSequence)
      return (N) parse(object.toString(), (Class<? extends Number>) toClass);
    
    throw new ObjectConversionException(object, toClass);
  }
  
  public String toString(final Number n)
  {
    return n != null ? n.toString() : null;
  }
  
  public <N extends Number> N parse(final String aStringValue, final Class<N> aClassType)
          throws ObjectConversionException
  {
    if (StringUtils.isBlank(aStringValue))
      return null;
    
    return convertNumber(new BigDecimal(aStringValue),aClassType);
  }
  
  @SuppressWarnings("unchecked")
  public <N extends Number> N convertNumber(final Number n, final Class<N> newClass) throws ObjectConversionException
  {
    if (n == null)
      return null;
  
    if (newClass.isAssignableFrom(n.getClass()))
      return (N) n;
  
    BigDecimal decimal;
    if (n instanceof BigDecimal)
    {
      if (newClass == BigDecimal.class)
        return (N) n;
      decimal = (BigDecimal) n;
    }
    else
    {
      final String s = n.toString();
      if (s.indexOf('/') == -1)
        decimal = new BigDecimal(s);
      else
      {
        final String[] fraction = s.split("/");
        if (fraction.length != 2)
          throw new ObjectConversionException(n, newClass);
        decimal = new BigDecimal(fraction[0]).divide(new BigDecimal(fraction[1]), MathContext.UNLIMITED);
      }
    }
  
  
    if (newClass == BigDecimal.class)
      return (N) decimal;
    
    /*if (roundDecimals && decimal.scale() > 0)
      decimal = decimal.setScale(0, BigDecimal.ROUND_HALF_UP);*/
  
    if (roundLargeValues && newClass != BigInteger.class)
    {
      final BigDecimal max, min;
      max = DecimalUtils.maxValue(newClass);
      min = DecimalUtils.minValue(newClass);
      if (max != null && decimal.compareTo(max) > 0)
        decimal = max;
      else if (min != null && decimal.compareTo(min) < 0)
        decimal = min;
    }
  
    final boolean useExact = !(allowOverflow && (roundDecimals || decimal.scale() == 0));
  
    final Number result;
  
    if (newClass == Float.class)
      result = decimal.floatValue();
    else if (newClass == Double.class)
      result = decimal.doubleValue();
    else if (newClass == BigInteger.class)
      result = roundDecimals ? decimal.toBigIntegerExact() : decimal.toBigInteger();
    else if (newClass.getSimpleName().equals("Fraction"))
      try
      {
        findConstructor:
        {
          for (Constructor constructor : newClass.getConstructors())
          {
            if (constructor.getParameterCount() == 1 && constructor.getParameters()[0].getName().equals("double"))
            {
              result = (Number) constructor.newInstance(decimal.doubleValue());
              break findConstructor;
            }
          }
  
          for (Method method : newClass.getDeclaredMethods())
          {
            if (method.getReturnType().getSimpleName().equals("Fraction") && method.getParameterCount() == 1 &&
                    method.getParameters()[0].getType().getName().equals("double"))
            {
              result = (Number) method.invoke(null, decimal.doubleValue());
              break findConstructor;
            }
          }
          throw new ObjectConversionException("Could not find constructor for Fraction", n, newClass);
        }
      }
      catch (IllegalAccessException | InstantiationException | InvocationTargetException aE)
      {
        throw new ObjectConversionException(n, newClass, aE);
      }
    else
    {
      if (roundDecimals && decimal.scale() > 0)
        decimal = decimal.setScale(0, BigDecimal.ROUND_HALF_UP);
      if (newClass == Integer.class)
        result = useExact ? decimal.intValueExact() : decimal.intValue();
      else if (newClass == Short.class)
        result = useExact ? decimal.shortValueExact() : decimal.shortValue();
      else if (newClass == Long.class)
        result = useExact ? decimal.longValueExact() : decimal.longValue();
      else if (newClass == Byte.class)
        result = useExact ? decimal.byteValueExact() : decimal.byteValue();
      else
        throw new ObjectConversionException(n, newClass);
    }
    return (N) result;
  }
  
  public NumberConverter allowOverflow(final boolean aAllowOverflow)
  {
    allowOverflow = aAllowOverflow;
    return this;
  }
  
  public NumberConverter roundLargeValues(final boolean aRoundLargeValues)
  {
    roundLargeValues = aRoundLargeValues;
    return this;
  }
  

}
