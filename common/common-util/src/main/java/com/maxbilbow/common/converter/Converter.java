package com.maxbilbow.common.converter;

import java.util.Objects;

public interface Converter
{
  boolean accepts(Object valueToConvert, Class<?> toClass);
  
  /**
   *
   * @param object
   * @param toClass
   * @param <T>
   * @return
   *
   * @throws ObjectConversionException
   */
  <R> R convert(Object object, Class<R> toClass) throws ObjectConversionException;
  
  /**
   * Converts {@code aItemValue} to the same class type as {@code aNullDefault}.
   *
   * If {@code aItemValue} is null, {@code aNullDefault} is returned.
   *
   * @see #convert(Object, Class)
   *
   * @param object
   * @param nullDefault
   * @param <R>
   * @return
   *
   * @throws NullPointerException if {@code aNullDefault} is null.
   * @throws ObjectConversionException if conversion fails or is not supported.
   */
  @SuppressWarnings("unchecked")
  default <R> R nullDefault(Object object, R nullDefault) throws ObjectConversionException
  {
    Objects.requireNonNull(nullDefault,"nullDefault not provided");
    if (object == null)
      return nullDefault;
    
    if (nullDefault.getClass().isAssignableFrom(object.getClass()))
      return (R) object;
  
    final R result = convert(object, (Class<R>) nullDefault.getClass());
    return result != null ? result : nullDefault;
  }
}
