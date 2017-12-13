package com.maxbilbow.common.converter;

import com.maxbilbow.common.maths.DecimalUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class NumberConverter
{
  private boolean roundLargeValues = false;
  private boolean roundDecimals = true;
  private boolean allowOverflow = false;
  
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
  
  public <N extends Number> N nullDefault(final Number n, N nullDefault)
  {
    Objects.requireNonNull(nullDefault);
    if (n == null)
      return nullDefault;
    
    return convert(n, (Class<N>) nullDefault.getClass());
  }
  
  public <N extends Number> N parse(final String aStringValue, final Class<N> aClassType)
  {
    return convert(new BigDecimal(aStringValue),aClassType);
  }
  
  @SuppressWarnings("unchecked")
  public <N extends Number> N convert(final Number n, final Class<N> newClass)
  {
    if (n == null)
      return null;
    
    if (newClass.isAssignableFrom(n.getClass()))
      return (N) n;
    
    BigDecimal decimal = new BigDecimal(n.toString());
    if (newClass == BigDecimal.class)
      return (N) decimal;
    
    if (roundDecimals && decimal.scale() > 0)
      decimal = decimal.setScale(0, BigDecimal.ROUND_HALF_UP);
  
    if(roundLargeValues && newClass != BigInteger.class)
    {
      final BigDecimal max,min;
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
    else if (newClass == Integer.class)
      result = useExact ? decimal.intValueExact() : decimal.intValue();
    else if (newClass == Short.class)
      result = useExact ? decimal.shortValueExact() : decimal.shortValue();
    else if (newClass == Long.class)
      result = useExact ? decimal.longValueExact() : decimal.longValue();
    else if (newClass == Byte.class)
      result = useExact ? decimal.byteValueExact() : decimal.byteValue();
    else if (newClass == BigInteger.class)
      result = roundDecimals ? decimal.toBigIntegerExact() : decimal.toBigInteger();
    else
      throw new UnsupportedOperationException();
  
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
