package com.maxbilbow.common.converter;

import com.maxbilbow.common.misc.Chooser;
import org.apache.commons.lang.mutable.*;
import org.apache.commons.lang3.math.Fraction;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NumberConverterTest
{
  
  @Test
  public void roundLargeNumbers() throws ObjectConversionException
  {
  
    final NumberConverter converter = new NumberConverter(true);
    //Int max value
    final Integer result = converter.convert(Long.MAX_VALUE, Integer.class);
    Assert.assertEquals(Integer.MAX_VALUE, (int) result);
    
  }
  
  @Test
  public void overflowLargeNumbers() throws ObjectConversionException
  {
    
    final NumberConverter converter = new NumberConverter(false,false,true);
    //Int max value
    final int result = converter.convert(((long)Integer.MAX_VALUE) + 1L, Integer.class);
    Assert.assertEquals(Integer.MIN_VALUE, result);
    
  }
  
  @Test(expected = ArithmeticException.class)
  public void failLargeNumbers() throws ObjectConversionException
  {
    
    final NumberConverter converter = new NumberConverter(false, true, false);
    //Int max value
    final byte result = converter.convert(Long.MAX_VALUE, Byte.class);
    Assert.assertNotEquals(Byte.MAX_VALUE, result);
    
  }
  
  @Test
  public void roundDecimals() throws ObjectConversionException
  {
    
    final NumberConverter converter = new NumberConverter(false, true);
    //Int max value
    final Integer result = converter.convert(2.5, Integer.class);
    Assert.assertEquals(3, (int) result);
    
  }
  
  @Test(expected = ArithmeticException.class)
  public void doNotRoundDecimals() throws ObjectConversionException
  {
    
    final NumberConverter converter = new NumberConverter(false, false);
    //Int max value
    final int result = converter.convert(2.5, Integer.class);
    Assert.assertNotEquals(3, result);
    
  }
  
  
  @RunWith(Parameterized.class)
  public static class Convert
  {
    private final Number input, expected, nullDefault;
    private final Class<? extends Number> castTo;
  
    private NumberConverter converter = new NumberConverter(true,true);
  
    public Convert(final Number aInput, final Number aExpected)
    {
      input = aInput;
      expected = aExpected;
      castTo = aExpected == null ? randomType() : aExpected.getClass();
      nullDefault = aExpected;
    }
  
    private Class<? extends Number> randomType()
    {
      return Chooser.chooseFrom(Number.class,
              Integer.class,
              Float.class,
              Double.class,
              Short.class,
              Long.class,
              BigDecimal.class,
              BigInteger.class,
              Byte.class);
    }
  
    @Test
    public void convert() throws ObjectConversionException
    {
      Assume.assumeNotNull(castTo);
      final Number result = converter.convert(input,castTo);
      System.out.printf("Converting %s (%s) to %s%n", input, input != null ? input.getClass() : null, castTo);
      doAssert(expected,result);
    }
  
    @Test
    public void testNullDefault() throws ObjectConversionException
    {
      Assume.assumeNotNull(nullDefault);
      final Number result = converter.nullDefault(input,nullDefault);
      doAssert(input != null ? expected : nullDefault, result);
    }
  
    private void doAssert(final Number expected, Number result)
    {
      if (expected == null)
      {
        Assert.assertNull(result);
        return;
      }
      Assert.assertNotNull(result);
    
      Assert.assertSame(result.getClass(), expected.getClass());
    
      if (expected.getClass().getSimpleName().equals("Fraction"))
        Assert.assertTrue(Double.compare(expected.doubleValue(),result.doubleValue()) == 0);
      else
        Assert.assertTrue(new BigDecimal(expected.toString()).compareTo(new BigDecimal(result.toString())) == 0);

//    Assert.assertEquals(expected, result);
    }
  
    @Parameters
    public static List<Object[]> getAllParams()
    {
      final List<Object[]> params = new ArrayList<>();
  
      {
        final byte _byte = 42;
        final int _int = 42;
        final long _long = 42L;
        final short _short = 42;
        final double _double = Double.parseDouble("42.0");
        final float _float = Float.parseFloat("42.0");
        
    
        final Number[] numbers = new Number[]{/*string,*/
                _int,
                _long,
                BigInteger.valueOf(42),
                BigDecimal.valueOf(42.0),
                _double,
                _float,
                _short,
                _byte,
                new org.apache.commons.math.fraction.Fraction(42),
                org.apache.commons.lang.math.Fraction.getFraction(42d),
                Fraction.getFraction(42d),
                new AtomicInteger(42),
                new AtomicLong(42),
                new MutableByte(42),
                new MutableShort(42),
                new MutableInt(42),
                new MutableLong(42),
                new MutableFloat(42),
                new MutableDouble(42)
        };
  
        for (Number expected : numbers)
          for (Number input : numbers)
            params.add(new Number[]{input, expected});
      }
      //Int max value
      params.add(new Object[]{new BigInteger(String.valueOf(Integer.MAX_VALUE)), Integer.MAX_VALUE});
      params.add(new Object[]{Integer.valueOf(Integer.MAX_VALUE).longValue() + 1,
              Integer.MAX_VALUE}); //TODO: what should this do?
  
      //Long max value
      params.add(new Object[]{new BigInteger(String.valueOf(Long.MAX_VALUE)), Long.MAX_VALUE});
      params.add(new Object[]{new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger("1")),
              Long.MAX_VALUE}); //TODO: what should this do?
  
      //Short MAX value
      params.add(new Object[]{Long.MAX_VALUE, Short.MAX_VALUE}); //throws error
  
      //Byte MAX value
      params.add(new Object[]{Long.MAX_VALUE, Byte.MAX_VALUE}); //throws error
  
      //Double MAX value
      params.add(new Object[]{BigDecimal.valueOf(Long.MAX_VALUE), Double.valueOf(String.valueOf(Long.MAX_VALUE))});
  
      //Float MAX value
      params.add(new Object[]{BigDecimal.valueOf(Long.MAX_VALUE), Float.valueOf(String.valueOf(Long.MAX_VALUE))});
      return params;
    }
    
  
    
  }
}