package com.maxbilbow.common.converter;

public class ObjectConversionException extends RuntimeException
{
  
  public ObjectConversionException(String message)
  {
    super(message);
  }
  
  public ObjectConversionException(String message, Throwable t)
  {
    super(message,t);
  }
}
