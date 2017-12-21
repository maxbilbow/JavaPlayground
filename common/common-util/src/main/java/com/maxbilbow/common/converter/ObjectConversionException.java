package com.maxbilbow.common.converter;


public class ObjectConversionException extends Exception
{
  private static final String ERR_MSG_CONVERT = "Unsupported conversion from %s (%s) to %s";
  
  public <T> ObjectConversionException(final Object aValue, final Class<T> aNewClass)
  {
    super(String.format(ERR_MSG_CONVERT,aValue,aValue.getClass().getSimpleName(),aNewClass.getSimpleName()));
  }
  
  public <T> ObjectConversionException(final String message, final Object aValue, final Class<T> aNewClass)
  {
    super(String.format(ERR_MSG_CONVERT,aValue,aValue.getClass().getSimpleName(),aNewClass.getSimpleName()) + ":" + message);
  }
  
  public <T> ObjectConversionException(final Object aValue, final Class<T> aNewClass, Throwable e)
  {
    super(String.format(ERR_MSG_CONVERT,aValue,aValue.getClass().getSimpleName(),aNewClass.getSimpleName()) + ":" + e.getMessage(),e);
  }
  
  
}
