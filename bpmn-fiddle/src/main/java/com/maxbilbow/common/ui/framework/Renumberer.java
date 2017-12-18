package com.maxbilbow.common.ui.framework;

import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class Renumberer
{
  public String removeGaps(String xml, String prefix, String suffix, final int numberPadding)
  {
    return renumber(xml,1,0,prefix,suffix,numberPadding);
  }
  
  public String renumber(String xml, final int firstNumber, final int shift, String prefix, String suffix, final int numberPadding)
  {
    if (shift < 0)
      throw new IllegalArgumentException("BPMN tasks cannot have ids less than 0. Shift of " + shift  +" is not allowed!");
    if (firstNumber < 1)
      throw  new IllegalArgumentException("BPMN tasks cannot have ids less than 0. First number of " + firstNumber  +" is not allowed!");
    
    final IdMapper mapper = new IdMapper( prefix, suffix,numberPadding);
    final Collection<int[]> idMaps = mapper.firstPass(xml, firstNumber, shift);
    xml = mapper.secondPass(xml,idMaps);
    xml = mapper.thirdPass(xml);
    return xml;
  }
  
}
