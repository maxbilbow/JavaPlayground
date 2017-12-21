package com.maxbilbow.experimental.converter;

import com.maxbilbow.common.converter.Converter;
import com.maxbilbow.common.converter.NumberConverter;
import com.maxbilbow.common.converter.ObjectConversionException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;

public class ObjectConverter implements Converter
{
  private NumberConverter numberConverter;
  
  private DateTimeConverter dateTimeConverter;
  
  public ObjectConverter(final NumberConverter aNumberConverter,
                         final DateTimeConverter aDateTimeConverter)
  {
    numberConverter = Objects.requireNonNull(aNumberConverter);
    dateTimeConverter = aDateTimeConverter;
  }
  
  @Override
  public boolean accepts(final Object valueToConvert, final Class<?> toClass)
  {
    return true;
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
   * @throws ObjectConversionException if conversion fails or is not supported.
   * @throws NullPointerException if {@code aClassType} is null
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T convert(Object aItemValue, Class<T> aClassType) throws ObjectConversionException
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
      else if (aClassType == String.class || aClassType == CharSequence.class)
      {
        if (aItemValue instanceof Temporal || aItemValue instanceof Date)
          newItemValue = dateTimeConverter.convert(aItemValue, aClassType);
        else
          newItemValue = aItemValue.toString();
      }
      else if (aClassType == Character.class)
      {
        if (aItemValue instanceof CharSequence || aItemValue instanceof Enum || aItemValue instanceof Number)
          newItemValue = aItemValue.toString().charAt(0);
        else if (aItemValue instanceof Boolean)
          newItemValue = Character.toUpperCase(aItemValue.toString().charAt(0));
      }
      else if (Temporal.class.isAssignableFrom(aClassType) || Date.class.isAssignableFrom(aClassType))
      {
        newItemValue = dateTimeConverter.convert(aItemValue, aClassType);
      }
      else if (aClassType == Boolean.class)
      {
        newItemValue = Boolean.valueOf(aItemValue.toString());
      }
      else if (Enum.class.isAssignableFrom(aClassType))
      {
        findEnum:
        {
          final String stringVal = aItemValue.toString();
          for (T type : aClassType.getEnumConstants()) //Look for exact match
          {
            if (stringVal.equals(type.toString()))
            {
              newItemValue = type;
              break findEnum;
            }
          }
          for (T type : aClassType.getEnumConstants()) //find case-insensitive match
          {
            if (stringVal.equalsIgnoreCase(type.toString()))
            {
              newItemValue = type;
              break findEnum;
            }
          }
        }
      }
    }
    catch (RuntimeException e)
    {
      throw new ObjectConversionException(aItemValue, aClassType,e);
    }
    
    
    if (newItemValue == null)
      throw new ObjectConversionException(aItemValue, aClassType);
    
    
    return (T) newItemValue;
  }
  
}
