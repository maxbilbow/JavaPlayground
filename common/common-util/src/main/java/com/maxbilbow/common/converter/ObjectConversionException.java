package com.maxbilbow.common.converter;

import com.sun.istack.internal.NotNull;

public class ObjectConversionException extends RuntimeException
{
  private static final String ERR_MSG_CONVERT = "Unsupported conversion from %s (%s) to %s";
  
  public <T> ObjectConversionException(@NotNull final Object aValue, final Class<T> aNewClass)
  {
    super(String.format(ERR_MSG_CONVERT,aValue,aValue.getClass().getSimpleName(),aNewClass.getSimpleName()));
  }
  
  public <T> ObjectConversionException(@NotNull final Object aValue, final Class<T> aNewClass, Throwable e)
  {
    super(String.format(ERR_MSG_CONVERT,aValue,aValue.getClass().getSimpleName(),aNewClass.getSimpleName()) + ":" + e.getMessage(),e);
  }
  
  
}
