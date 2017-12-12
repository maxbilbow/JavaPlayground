package com.maxbilbow.common.converter;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;

public class ObjectConverter
{
  private static final String ERR_MSG_CONVERT = "Could not convert %s (%s) to %s";
  
  private NumberConverter numberConverter;
  
  private DateTimeFormatter dateParser, timeParser; //TODO: handle different formats on conversion
  
  public ObjectConverter(final NumberConverter aNumberConverter,
                         final String dateFormat,
                         final String timeFormat,
                         final String dateTimeFormat)
  {
    numberConverter = Objects.requireNonNull(aNumberConverter);
    dateParser = DateTimeFormatter.ofPattern(dateFormat);
    timeParser = DateTimeFormatter.ofPattern(timeFormat);
  }
  
  /**
   * Converts {@code aItemValue} to the same class type as {@code aNullDefault}.
   *
   * If {@code aItemValue} is null, {@code aNullDefault} is returned.
   *
   * @see #convert(Object, Class)
   *
   * @param aItemValue
   * @param aNullDefault
   * @param <T>
   * @return
   *
   * @throws NullPointerException if {@code aNullDefault} is null.
   * @throws ClassCastException if conversion fails or is not supported.
   */
  public <T> T nullDefault(final Object aItemValue, final T aNullDefault)
  {
    if (aNullDefault == null)
      throw new NullPointerException("A Default value must be provided");
    
    if (aItemValue == null)
      return aNullDefault;
    
    @SuppressWarnings("unchecked")
    final Class<T> conversionClass = (Class<T>) aNullDefault.getClass();
    return convert(aItemValue, conversionClass);
  }
  
  /**
   * Converts aItemValue to the aClassType if possible.
   *
   * It will either return the converted object or throw a runtime exception on error
   *
   * Currently supports conversion to:
   *        @see Integer
   *        @see Long
   *        @see BigInteger
   *        @see BigDecimal
   *        @see String
   *        @see Character
   *        @see LocalDate
   *        @see LocalDateTime
   *        @see LocalTime
   *        @see Boolean
   *
   * @param aItemValue
   * @param aClassType
   * @param <T>
   * @return converted object or null if {@code aItemValue} is null or a blank string
   *
   * @throws ClassCastException if conversion fails or is not supported.
   * @throws NullPointerException if {@code aClassType} is null
   */
  @SuppressWarnings("unchecked")
  public <T> T convert(Object aItemValue, Class<T> aClassType)
  {
    if (aClassType == null)
      throw new NullPointerException("Conversion class type must not be null!");
    
    if (aItemValue == null)
      return null;
    
    if (aClassType != String.class && aItemValue instanceof String && StringUtils.isBlank((String) aItemValue))
      return null;
    
    if (aClassType.isAssignableFrom(aItemValue.getClass()))
      return (T) aItemValue;
    
    Object newItemValue = null;
    try
    {
      if (aClassType.isAssignableFrom(aItemValue.getClass()))
      {
        newItemValue = aItemValue;
      }
      else if (Number.class.isAssignableFrom(aClassType))
      {
        if (aItemValue instanceof Number)
          newItemValue = numberConverter.convert((Number) aItemValue, (Class<? extends Number>) aClassType);
        else
          newItemValue = numberConverter.parse(aItemValue.toString(),(Class<? extends Number>) aClassType);
      }
      else if (aClassType == String.class)
      {
        if (aItemValue instanceof Temporal)
          newItemValue = aItemValue.toString();//TODO dateTimeConverter
        else if (aItemValue instanceof Date)
          newItemValue = aItemValue.toString();
        else
          newItemValue = aItemValue.toString();
      }
      else if (aClassType == Character.class)
      {
        if (aItemValue instanceof String || aItemValue instanceof Enum || aItemValue instanceof Number)
          newItemValue = aItemValue.toString().charAt(0);
      }
      else if (aClassType == LocalDate.class)
      {
        if (aItemValue instanceof LocalDateTime)
          newItemValue = ((LocalDateTime) aItemValue).toLocalDate();
        else if (aItemValue instanceof String)
          newItemValue = LocalDate.parse((String) aItemValue, dateParser);
        else if (aItemValue instanceof Date)
          newItemValue = LocalDateTime.ofInstant(Instant.ofEpochMilli(((Date) aItemValue).getTime()), ZoneId.systemDefault()).toLocalDate();
      }
      else if (aClassType == LocalDateTime.class)
      {
        if (aItemValue instanceof LocalDate)
          newItemValue = ((LocalDate) aItemValue).atStartOfDay();
        else if (aItemValue instanceof String)
          newItemValue = LocalDate.parse((String) aItemValue, dateParser).atStartOfDay();
        else if (aItemValue instanceof Date)
          newItemValue = LocalDateTime.ofInstant(Instant.ofEpochMilli(((Date) aItemValue).getTime()),ZoneId.systemDefault());
      }
      else if (aClassType == LocalTime.class)
      {
        if (aItemValue instanceof String)
          newItemValue = LocalTime.parse((String) aItemValue, timeParser);
      }
      else if (Date.class.isAssignableFrom(aClassType))
      {
        final Long millis;
        if (aItemValue instanceof LocalDateTime)
          millis = ((LocalDateTime) aItemValue).toInstant(ZoneOffset.UTC).toEpochMilli();
        else if (aItemValue instanceof LocalDate)
          millis = ((LocalDate) aItemValue).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        else if (aItemValue instanceof Date)
          millis = ((Date) aItemValue).getTime();
        else if (aItemValue instanceof String)
          millis = LocalDate.parse((String) aItemValue, dateParser).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        else
          millis = null;
        
        if (millis != null)
          if (aClassType == Timestamp.class)
            newItemValue = new Timestamp(millis);
          else if (aClassType == Date.class)
            newItemValue = new Date(millis);
          else if (aClassType == java.sql.Date.class)
            newItemValue = new java.sql.Date(millis);
      }
      else if (aClassType == Boolean.class)
      {
        newItemValue = Boolean.valueOf(aItemValue.toString());
      }
      else if (Enum.class.isAssignableFrom(aClassType))
      {
        if (aItemValue instanceof String)
        {
          for (T type : aClassType.getEnumConstants())
          {
            if (((String) aItemValue).equalsIgnoreCase(type.toString()))
            {
              newItemValue = type;
              break;
            }
          }
        }
      }
    }
    catch (RuntimeException e)
    {
      throw new ClassCastException(
              String.format(ERR_MSG_CONVERT,
                      aItemValue,
                      aItemValue.getClass().getSimpleName(),
                      aClassType.getSimpleName()) + ": " + e.getMessage());
    }
    
    
    if (newItemValue == null)
    {
      throw new ClassCastException(
              String.format(ERR_MSG_CONVERT,
                      aItemValue,
                      aItemValue.getClass().getSimpleName(),
                      aClassType.getSimpleName()));
    }
    
    return (T) newItemValue;
  }
  
}
