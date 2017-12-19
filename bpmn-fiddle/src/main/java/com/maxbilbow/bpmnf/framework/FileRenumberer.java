package com.maxbilbow.bpmnf.framework;

import com.maxbilbow.bpmnf.config.FDLConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class FileRenumberer
{
  @Resource
  private Renumberer mRenumberer;
  
  @Value("${bpmnf.pattern.prefix:_}")
  private String prefix;
  
  @Value("${bpmnf.pattern.suffix:}")
  private String suffix;
  
  @Value("${bpmnf.pattern.padding:3}")
  private int padding;
  
  public Path renumberAfms(Path input, int start, int shift) throws IOException
  {
    String xml = new String(Files.readAllBytes(FDLTools.assertValidInput(input)), FDLConstants.defaultCharset());
    
    xml = mRenumberer.renumber(xml,start,shift,prefix,suffix,padding);
    
    final Path output = getOutputPathFromInput(input, 0);
    Files.createFile(output);
    Files.write(output,xml.getBytes(Charset.defaultCharset()));
    return output;
  }
  
  private static Path getOutputPathFromInput(final Path input, int attempt)
  {
    final String suffix = attempt == 0 ? "_renumbered" : "_renumbered_" + attempt;
    final String newFileName = StringUtils.substringBeforeLast(Objects.toString(input.getFileName()),".") + suffix + ".bpmn";
    final Path output = Paths.get(
            input.getParent() + File.separator + newFileName);
    
    if (!Files.exists(output))
      return output;
    
    return getOutputPathFromInput(input, attempt+1);
  }
}
