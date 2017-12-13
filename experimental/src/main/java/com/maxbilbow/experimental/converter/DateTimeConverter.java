package com.maxbilbow.experimental.converter;

import com.maxbilbow.common.converter.ObjectConversionException;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class DateTimeConverter
{
  private DateTimeFormatter[] dateTimeFormatters;

  private DateTimeFormatter[] dateFormatters;
  
  private DateTimeFormatter[] timeFormatters;
  
  private ZoneId zoneId;
  private Locale locale;
  private ZoneOffset zoneOffset = ZoneOffset.UTC;
  
  public DateTimeConverter(final String dateTimePattern, final String datePattern, final String timePattern, ZoneId aZoneId, Locale aLocale)
  {
    this(new String[]{Objects.requireNonNull(dateTimePattern)},
            new String[]{Objects.requireNonNull(datePattern)},
            new String[]{Objects.requireNonNull(timePattern)},
            aZoneId,
            aLocale);
  }
  public DateTimeConverter(final String[] dateTimePatterns,
                           final String[] datePatterns,
                           final String[] timePatterns,
                           final ZoneId aZoneId, final Locale aLocale)
  {
    this.dateTimeFormatters = getFormatters(dateTimePatterns,aLocale);
    this.dateFormatters = getFormatters(datePatterns,aLocale);
    this.timeFormatters = getFormatters(timePatterns,aLocale);
    this.zoneId = aZoneId;
    this.locale = aLocale;
  }
  
  private static DateTimeFormatter[] getFormatters(String[] patterns, Locale aLocale)
  {
    final DateTimeFormatter[] formatters = new DateTimeFormatter[patterns.length];
    for (int i=0;i<patterns.length;++i)
      formatters[i] = DateTimeFormatter.ofPattern(patterns[i], aLocale);
    return formatters;
  }
  
  public <T> T convert(Object aValue, Class<T> newClass) throws ObjectConversionException
  {
    if (aValue == null)
      return null;
    
    if (newClass.isAssignableFrom(aValue.getClass()))
      return (T) aValue;
  
    if (aValue instanceof TemporalAccessor)
      return convertTemporalAccessor((TemporalAccessor) aValue, newClass);
    
    if (aValue instanceof Date)
      return convertTemporalAccessor(LocalDateTime.ofInstant(Instant.ofEpochMilli(((Date) aValue).getTime()), zoneId), newClass);
      
    if (aValue instanceof CharSequence)
    {
      if (StringUtils.isBlank((CharSequence) aValue))
        return null;
      else
        return parse((CharSequence) aValue, newClass);
    }
    
    throw new ObjectConversionException(aValue, newClass);
    
  }
  
  private <T> T parse(final CharSequence value, final Class<T> aNewClass) throws ObjectConversionException
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
    
    return convertTemporalAccessor(temporalAccessor, aNewClass);
  }
  
  @SuppressWarnings("unchecked")
  private <T> T convertTemporalAccessor(final TemporalAccessor accessor, Class<T> newClass)
          throws ObjectConversionException
  {
    final Temporal dateOrTime = getMostDetailedType(accessor);
    if (dateOrTime != null)
    {
      if (dateOrTime instanceof LocalDateTime)
      {
        final LocalDateTime dateTime = (LocalDateTime) dateOrTime;
        if (newClass == LocalDate.class)
          return (T) dateTime.toLocalDate();
        else if (newClass == LocalDateTime.class)
          return (T) dateTime;
        else if (newClass == LocalTime.class)
          return (T) dateTime.toLocalTime();
        else if (Date.class.isAssignableFrom(newClass))
        {
          final Long millis = dateTime.toInstant(zoneOffset).toEpochMilli();
      
          if (newClass == Timestamp.class)
            return (T) new Timestamp(millis);
          else if (newClass == Date.class)
            return (T) new Date(millis);
          else if (newClass == java.sql.Date.class)
            return (T) new java.sql.Date(millis);
        }
      }
      else if (dateOrTime instanceof LocalTime)
      {
        if (newClass == LocalTime.class)
          return (T) dateOrTime;
      }
    }
    throw new ObjectConversionException(accessor, newClass);
  }
  
  private Temporal getMostDetailedType(final TemporalAccessor aAccessor)
  {
    if (aAccessor.isSupported(ChronoField.YEAR))
    {
      int MM=1, dd=1, HH=0, mm=0, ss=0, nn=0;
      final int yyyy = aAccessor.get(ChronoField.YEAR);
      if (aAccessor.isSupported(ChronoField.MONTH_OF_YEAR))
        MM = aAccessor.get(ChronoField.MONTH_OF_YEAR);
      if (aAccessor.isSupported(ChronoField.DAY_OF_MONTH))
        dd = aAccessor.get(ChronoField.DAY_OF_MONTH);
      if (aAccessor.isSupported(ChronoField.HOUR_OF_DAY))
        HH = aAccessor.get(ChronoField.HOUR_OF_DAY);
      if (aAccessor.isSupported(ChronoField.MINUTE_OF_HOUR))
        mm = aAccessor.get(ChronoField.MINUTE_OF_HOUR);
      if (aAccessor.isSupported(ChronoField.SECOND_OF_MINUTE))
        ss = aAccessor.get(ChronoField.SECOND_OF_MINUTE);
      if (aAccessor.isSupported(ChronoField.NANO_OF_SECOND))
        nn = aAccessor.get(ChronoField.NANO_OF_SECOND);
      return LocalDateTime.of(yyyy,MM,dd,HH,mm,ss,nn);
    }
    else if (aAccessor.isSupported(ChronoField.HOUR_OF_DAY))
    {
      int mm=0, ss=0, nn=0;
      final int HH = aAccessor.get(ChronoField.HOUR_OF_DAY);
      if (aAccessor.isSupported(ChronoField.MINUTE_OF_HOUR))
        mm = aAccessor.get(ChronoField.MINUTE_OF_HOUR);
      if (aAccessor.isSupported(ChronoField.SECOND_OF_MINUTE))
        ss = aAccessor.get(ChronoField.SECOND_OF_MINUTE);
      if (aAccessor.isSupported(ChronoField.NANO_OF_SECOND))
        nn = aAccessor.get(ChronoField.NANO_OF_SECOND);
      return LocalTime.of(HH,mm,ss,nn);
    }
    else if (aAccessor.isSupported(ChronoField.SECOND_OF_DAY))
    {
      return LocalTime.ofNanoOfDay(aAccessor.get(ChronoField.SECOND_OF_DAY));
    }
    else if (aAccessor.isSupported(ChronoField.NANO_OF_DAY))
    {
      return LocalTime.ofNanoOfDay(aAccessor.get(ChronoField.NANO_OF_DAY));
    }
    return null;
  }
}
