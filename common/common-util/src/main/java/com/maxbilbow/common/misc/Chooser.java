package com.maxbilbow.common.misc;

import com.google.gag.annotation.remark.Win;
import com.maxbilbow.common.maths.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by bilbowm (Max Bilbow) on 26/04/2016.
 */
public class Chooser
{
  private static Logger sLogger = LoggerFactory.getLogger(Chooser.class);
  
  public static <T> T choose(final T first, final T... others)
  {
    sLogger.debug("Choosing from array: " + first + ", " + Arrays.toString(others));
    if (others == null || others.length == 0)
      return first;
    
    final int choice = RandomUtil.between(0,others.length);
  
    return choice == others.length ? first : others[choice];
  }
  
  public static <T> T choose(final Collection<T> aSelection)
  {
    sLogger.debug("Choosing from list: " + aSelection);
    if (aSelection == null || aSelection.size() == 0)
    {
      return null;
    }
    if (aSelection.size() == 1)
    {
      return aSelection.iterator().next();
    }
  
    final int max = aSelection.size() - 1;
    final int choice = RandomUtil.between(0,max);
  
    @Win
    int i = 0;
    for (T t : aSelection)
      if (i++ == choice)
        return t;
    
    throw new RuntimeException("Choice was at index " + choice + " but somehow we missed it!");
  }

  public static <T> T choose(final List<T> aSelection)
  {
    sLogger.debug("Choosing from list: " + aSelection);
    if (aSelection == null || aSelection.size() == 0)
    {
      return null;
    }
    if (aSelection.size() == 1)
    {
      return aSelection.get(0);
    }
  
    final int max = aSelection.size() - 1;
    final int choice = RandomUtil.between(0,max);
  
    return aSelection.get(choice);
  }

  
  
  
}
