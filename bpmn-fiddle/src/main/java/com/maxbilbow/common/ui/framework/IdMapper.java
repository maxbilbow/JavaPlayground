package com.maxbilbow.common.ui.framework;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdMapper
{
  private static final String PASS1_PATTERN = " id=\"%s(.*?)%s\" ";
  
  private final String tempPattern;
  
  private static final List<Character> CHARS = Collections.unmodifiableList(Arrays.asList(
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'
  ));
  private final String prefix, suffix;
  private final int padding;
  
  IdMapper(String aPrefix, String aSuffix, final int padding)
  {
    this(aPrefix,aSuffix,padding,"__BPMN_"+UUID.randomUUID().toString() + "%s_FIDDLE__");
  }
  
  IdMapper(String aPrefix, String aSuffix, final int padding, String aTempPattern)
  {
    this.prefix = aPrefix;
    this.suffix = aSuffix;
    this.padding = padding;
    this.tempPattern = aTempPattern;
  }
  
  Collection<int[]> firstPass(String xml, final int first, int shift)
  {
    final List<int[]> idMaps = new ArrayList<>();
  
    {
      final Matcher matcher = Pattern.compile(String.format(PASS1_PATTERN, prefix, suffix)).matcher(xml);
    
    
      final List<Integer> idList = new ArrayList<>();
    
      while (matcher.find())
      {
        final String s = matcher.group(1);
        if (!NumberUtils.isDigits(s))
          continue;
      
        final Integer n = Integer.valueOf(s);
        if (n < first) //then
          continue;
      
        idList.add(n);
      }
    
      if (idList.isEmpty())
        return Collections.emptyList();
      
      idList.sort(Integer::compare);
      
      int count = first;
      
      for (Integer id : idList)
      {
        while (id > count)
        {
          count++;
          shift--;
        }
        
        count++;
      
        final int newId = id + shift;
        
        if (id != newId)
          idMaps.add(new int[]{id, newId});
      }
    }
    return idMaps;
  }
  
  String secondPass(String xml, Collection<int[]> idMaps)
  {
    for (int[] idMap : idMaps)
    {
      final String current = prefix + StringUtils.leftPad(String.valueOf(idMap[0]),padding,'0') + suffix;
      final char[] chars = new char[padding];
      {
        final String paddedNumber = StringUtils.leftPad(String.valueOf(idMap[1]), padding, '0');
        int i = 0;
        for (Character integer : paddedNumber.toCharArray())
          chars[i++] = CHARS.get(Integer.parseInt(integer.toString()));
      }
      final String replacement = new String(chars);
      xml = xml.replace(current,String.format(tempPattern,replacement));
    }
    return xml;
  }
  
  
  String thirdPass(String xml)
  {
    final Matcher matcher = Pattern.compile(String.format(tempPattern, "(.*?)")).matcher(xml);
    
    while (matcher.find())
    {
      final StringBuilder sb = new StringBuilder(prefix);
      for (char c : matcher.group(1).toCharArray())
        sb.append(CHARS.indexOf(c));
      sb.append(suffix);
      xml = xml.replace(matcher.group(), sb.toString());
    }
    return xml;
  }
}
