package com.maxbilbow.experimental.converter;


import com.maxbilbow.common.converter.NumberConverter;
import com.maxbilbow.common.converter.ObjectConversionException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(Parameterized.class)
public class ObjectConverterTest
{

  private final Object in, out;
  private final String message;
  private boolean throwsError;
  private static final ObjectConverter converter = new ObjectConverter(
          new NumberConverter(true),
          new DateTimeConverter("yyyyMMdd","HHmmss","yyyyMMdd HHmmss", ZoneId.systemDefault(), Locale.getDefault())
  );

  public ObjectConverterTest(Object aIn, Object aOut, boolean aThrows)
  {
    this.throwsError = aThrows;
    this.in = aIn;
    this.out = aOut;
    this.message = String.format("Converting %s: %s to %s: %s",
            in != null ? in.getClass().getSimpleName() : "null",
            in,out.getClass().getSimpleName(),out);
  }

  @Test
  public void convertObject() throws Exception
  {
    Assume.assumeFalse(this.message + " SHOULD throw an exception", this.throwsError);
    System.out.println(message);
    final Object result = converter.convert(in, out.getClass());
    if (in == null)
      Assert.assertNull(message, result);
    else
    {
      Assert.assertTrue(message, out.getClass().isAssignableFrom(result.getClass()));
      if (out instanceof Number)
      {
        if (out instanceof Comparable)
          Assert.assertTrue(message,((Comparable) out).compareTo(result) == 0);

        final BigDecimal expected = new BigDecimal(out.toString());
        final BigDecimal actual = new BigDecimal(result.toString());
        Assert.assertTrue(message,expected.compareTo(actual) == 0);
      }
      else if (out instanceof String && in instanceof Number && !Objects.equals(out,result))
      {
        System.out.printf("WARNING: %s ~= %s when %s%n",out,result,message);
        Assert.assertTrue(message, new BigDecimal(out.toString()).compareTo(new BigDecimal(result.toString())) == 0);
      }
      else
      {
        Assert.assertEquals(message, out,result);
      }
    }

  }

  @Test(expected = ObjectConversionException.class)
  public void classCastException() throws ObjectConversionException
  {
    Assume.assumeTrue(this.message + " should NOT throw an exception",this.throwsError);
    converter.convert(in, out.getClass());
  }


  /**
   * Integer
   * Long
   * BigInteger
   * BigDecimal
   * String
   * Character
   * LocalDate
   * LocalDateTime
   * LocalTime
   * DateTime
   * Boolean
   *
   */
  @Parameterized.Parameters
  public static List<Object[]> getParameters()
  {
    final List<Object[]> params = new ArrayList<>();

    //null
    params.add(new Object[]{null, "should be null",false});

    //Errors
    params.add(new Object[]{"One",1,true}); //throws an error

    //Primitives
    params.add(new Object[]{"Hello, world.","Hello, world.",false});//string

    //toString
    params.add(new Object[]{new StringBuilder("Hello, world."),"Hello, world.",false});//string

    //Char
    params.add(new Object[]{"Hello, world.",'H',false});
    params.add(new Object[]{1,'1',false});

    params.add(new Object[]{"True",true,false});//bool
    params.add(new Object[]{true,true,false});//bool

    params.add(new Object[]{"4.2",new BigDecimal("4.2"),false});
    params.add(new Object[]{new BigDecimal("4.2"),new BigDecimal("4.2"),false});

    params.add(new Object[]{"42",new BigInteger("42"),false});
    params.add(new Object[]{new BigInteger("42"),new BigInteger("42"),false});

    //Numbers
    {
      final String string = "42";
      final byte _byte = 42;
      final int _int = 42;
      final long _long = 42L;
      final short _short = 42;
      final double _double = Double.parseDouble(string);
      final float _float = Float.parseFloat(string);
      final BigInteger bigInt = BigInteger.valueOf(42);
      final BigDecimal bigDecimal = new BigDecimal(string);


      for (Object expected :  new Object[]{string,_int,_long,bigInt,bigDecimal,_double,_float,_short,_byte})
      {
        params.add(new Object[]{string, expected, false});
        params.add(new Object[]{_int, expected, false});
        params.add(new Object[]{_long, expected, false});
        params.add(new Object[]{bigInt, expected, false});
        params.add(new Object[]{bigDecimal, expected, false});
        params.add(new Object[]{_double, expected, false});
        params.add(new Object[]{_float, expected, false});
        params.add(new Object[]{_short, expected, false});
        params.add(new Object[]{_byte, expected, false});
      }

      //Int max value
      params.add(new Object[]{new BigInteger(String.valueOf(Integer.MAX_VALUE)), Integer.MAX_VALUE, false});
      params.add(new Object[]{Integer.valueOf(Integer.MAX_VALUE).longValue() + 1, Integer.MAX_VALUE, false}); //TODO: what should this do?

      //Long max value
      params.add(new Object[]{new BigInteger(String.valueOf(Long.MAX_VALUE)), Long.MAX_VALUE, false});
      params.add(new Object[]{new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger("1")), Long.MAX_VALUE,
              false}); //TODO: what should this do?

      //Short MAX value
      params.add(new Object[]{Long.MAX_VALUE, Short.MAX_VALUE, false}); //throws error

      //Byte MAX value
      params.add(new Object[]{Long.MAX_VALUE, Byte.MAX_VALUE, false}); //throws error

      //Double MAX value
      params.add(new Object[]{BigDecimal.valueOf(Long.MAX_VALUE), Double.valueOf(String.valueOf(Long.MAX_VALUE)), false});

      //Float MAX value
      params.add(new Object[]{BigDecimal.valueOf(Long.MAX_VALUE), Float.valueOf(String.valueOf(Long.MAX_VALUE)), false});

    }
    //DATES
    {
      final String dateString = "20010101"; // 01/01/2001
      final LocalDate localDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(dateString));
      final LocalDateTime datetime = localDate.atStartOfDay();
      final long millis = Date.parse("Mon, 01 Jan 2001 00:00:00 GMT");//datetime.toInstant(ZoneOffset.UTC).toEpochMilli();
      final Date date = new Date(millis);
      final Date sqlDate = new java.sql.Date(millis);
      final Timestamp timestamp = new Timestamp(millis);

      for (final Object expected : new Object[] {localDate,datetime,date,sqlDate, timestamp})
      {
        System.out.println(expected);
        params.add(new Object[]{dateString, expected, false});
        params.add(new Object[]{localDate, expected, false});
        params.add(new Object[]{datetime, expected, false});
        params.add(new Object[]{date, expected, false});
        params.add(new Object[]{sqlDate, expected, false});
        params.add(new Object[]{timestamp, expected, false});
      }

      params.add(new Object[]{
              LocalDateTime.of(2017,11,16,0,0,56),
              new Timestamp(Date.parse("Thu, 16 Nov 2017 00:00:56 GMT")),
              false});

    }

    {
      //Assignable types that can be cast and returned.
      class Foo
      {
        @Override
        public boolean equals(final Object obj)
        {
          return obj instanceof Foo;
        }
      }
      class Bar extends Foo { }
      //typeOf
      params.add(new Object[]{new Bar(), new Foo(), false});//Bar is a Foo so can expect Bar to be returned
      params.add(new Object[]{new Foo(), new Bar(), true}); //Foo cannot be cast to Bar so expect an error
    }
    //TODO: Many more combinations
    return params;
  }
}