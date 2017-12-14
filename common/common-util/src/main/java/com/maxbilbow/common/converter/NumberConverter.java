package com.maxbilbow.common.converter;

import com.maxbilbow.common.maths.DecimalUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

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
  
    final BigDecimal decimal = getDecimal(n, newClass);
  
  
    if (newClass == BigDecimal.class)
      return (N) decimal;
    
    final Number result;
  
    if (newClass == Double.class)
      result = decimal.doubleValue();
    else if (newClass == Float.class)
      result = decimal.floatValue();
    else if (newClass.getSimpleName().equals("Fraction"))
    {
      try
      {
        result = getFraction(decimal, newClass);
        if (result == null)
          throw new ObjectConversionException("Could not find constructor for " + newClass.getName(), n, newClass);
      }
      catch (IllegalAccessException | InstantiationException | InvocationTargetException aE)
      {
        throw new ObjectConversionException(n, newClass, aE);
      }
    }
    else
    {
      final BigInteger integer = roundDecimals && decimal.scale() > 0 ?
              decimal.setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger() :
              decimal.toBigIntegerExact();
      if (newClass == BigInteger.class)
        result = integer;
      else if (newClass == Integer.class)
        result = allowOverflow ? decimal.intValue() : decimal.intValueExact();
      else if (newClass == Short.class)
        result = allowOverflow ? decimal.shortValue() : decimal.shortValueExact();
      else if (newClass == Long.class)
        result = allowOverflow ? decimal.longValue() : decimal.longValueExact();
      else if (newClass == Byte.class)
        result = allowOverflow ? decimal.byteValue() : decimal.byteValueExact();
      else
        try
        {
          result = reflectiveGuess(decimal, newClass);
          if (result == null)
            throw new ObjectConversionException("Could not find suitable constructor for " + newClass.getName(),n, newClass);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException aE)
        {
          throw new ObjectConversionException(n, newClass, aE);
        }
        
    }
    return (N) result;
  }
  
  private <N extends Number> BigDecimal getDecimal(final Number n, final Class<N> newClass)
          throws ObjectConversionException
  {
    final BigDecimal decimal;
    if (n instanceof BigDecimal)
    {
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
    
    if (newClass == BigDecimal.class || newClass == BigInteger.class)
      return decimal;
    
    return handleLargeValues(decimal,DecimalUtils.minValue(newClass),DecimalUtils.maxValue(newClass));
  }
  
  private <N extends Number> Number reflectiveGuess(final BigDecimal decimal,
                                                    final Class<N> newClass)
          throws IllegalAccessException, InvocationTargetException, InstantiationException
  {
    final Map<String,Constructor> constructorMap = new HashMap<>();
    for (Constructor constructor : newClass.getConstructors())
    {
      if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterCount() == 1)
        constructorMap.put(constructor.getParameters()[0].getType().getName().toLowerCase(),constructor);
    }
    if (constructorMap.isEmpty())
      return null;
  
    if (constructorMap.containsKey("bigdecimal"))
      return (Number) constructorMap.get("bigdecimal").newInstance(decimal);
  
    if (constructorMap.containsKey("double"))
      return (Number) constructorMap.get("double").newInstance(decimal.doubleValue());
    
    if (constructorMap.containsKey("biginteger"))
      return (Number) constructorMap.get("biginteger").newInstance(allowOverflow ? decimal.toBigInteger() : decimal.toBigIntegerExact());
      
    if (constructorMap.containsKey("float"))
      return (Number) constructorMap.get("float").newInstance(decimal.floatValue());
  
    if (constructorMap.containsKey("long"))
      return (Number) constructorMap.get("long").newInstance(allowOverflow ? decimal.longValue() : decimal.longValueExact());
  
    if (constructorMap.containsKey("int"))
      return (Number) constructorMap.get("int").newInstance(allowOverflow ? decimal.intValue() : decimal.intValueExact());
  
    if (constructorMap.containsKey("integer"))
      return (Number) constructorMap.get("integer").newInstance(allowOverflow ? decimal.intValue() : decimal.intValueExact());
  
    if (constructorMap.containsKey("short"))
      return (Number) constructorMap.get("short").newInstance(allowOverflow ? decimal.shortValue() : decimal.shortValueExact());
  
    if (constructorMap.containsKey("byte"))
      return (Number) constructorMap.get("byte").newInstance(allowOverflow ? decimal.byteValue() : decimal.byteValueExact());
  
    if (constructorMap.containsKey("string"))
      return (Number) constructorMap.get("string").newInstance(decimal.toPlainString());
    
    return null;
  
  }
  
  private Number getFraction(BigDecimal decimal, final Class<? extends Number> newClass)
          throws InvocationTargetException, IllegalAccessException, InstantiationException
  {
    for (Constructor constructor : newClass.getConstructors())
      if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType().getName().equals("double"))
        return (Number) constructor.newInstance(decimal.doubleValue());
    
    for (Method method : newClass.getDeclaredMethods())
      if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers()) && method.getReturnType().getSimpleName().equals("Fraction") && method.getParameterCount() == 1 &&
              method.getParameters()[0].getType().getName().equals("double"))
        return (Number) method.invoke(null, decimal.doubleValue());
  
    return null;
  }
  
  private BigDecimal handleLargeValues(final BigDecimal decimal, BigDecimal min, BigDecimal max)
  {
    if (max != null && decimal.compareTo(max) > 0)
    {
      if (roundLargeValues)
        return max;
      else if (!allowOverflow)
        throw new ArithmeticException("Large decimal value was larger than Double.MAX_VALUE");
    }
    else if (min != null && decimal.compareTo(min) < 0)
    {
      if (roundLargeValues)
        return min;
      else if (!allowOverflow)
        throw new ArithmeticException("Large decimal value was smaller than Double.MIN_VALUE");
    }
    
    return decimal;
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
