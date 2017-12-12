package com.maxbilbow.common.converter;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateTimeConverter
{
  private DateTimeFormatter[] dateTimeFormatters;

  private DateTimeFormatter[] dateFormatters;
  
  private DateTimeFormatter[] timeFormatters;
  
  public DateTimeConverter(final String[] dateTimePatterns, final String[] datePatterns, final String[] timePatterns)
  {
    this.dateTimeFormatters = new DateTimeFormatter[dateTimePatterns.length];
    this.dateFormatters = new DateTimeFormatter[datePatterns.length];
    this.timeFormatters = new DateTimeFormatter[timePatterns.length];
  }
  
  public <T> T convert(Object aValue, Class<T> newClass)
  {
    if (aValue == null)
      return null;
    if (aValue instanceof Temporal)
      return convertTemporal((Temporal) aValue,newClass);
    else if (aValue instanceof Date)
      return convertDate((Date) aValue, newClass);
    else
      return parse(aValue.toString(), newClass);
  }
  
  <T> T parse(final String value, final Class<T> aNewClass)
  {
    if (StringUtils.isBlank(value))
      return null;
    
    final List<DateTimeFormatter> parsers = new ArrayList<>();
    if (Temporal.class.isAssignableFrom(aNewClass) || Date.class.isAssignableFrom(aNewClass))
    {
      parsers.addAll(Arrays.asList(dateTimeFormatters));
      parsers.addAll(Arrays.asList(dateFormatters));
    }
    else
    {
      parsers.addAll(Arrays.asList(timeFormatters));
    }
    TemporalAccessor temporalAccessor = null;
    DateTimeParseException firstParseException = null;
    for (DateTimeFormatter parser : parsers)
    {
      try
      {
        temporalAccessor = parser.parse(value);
        firstParseException = null;
        break;
      }
      catch (DateTimeParseException e)
      {
        if (firstParseException == null)
          firstParseException = e;
      }
    }
    if (firstParseException != null)
      throw firstParseException; //TODO: better error handling
    if (temporalAccessor == null)
      throw new NullPointerException("temporal accessor was null!");
    
    return convertTemporal(LocalDateTime.from(temporalAccessor),aNewClass);
  }
  
  <T> T convertDate(final Date aDate, final Class<T> aNewClass)
  {
    return null;
  }
  
  <T> T convertTemporal(Temporal aTemporal, Class<T> newClass)
  {
    return null;
  }
}
