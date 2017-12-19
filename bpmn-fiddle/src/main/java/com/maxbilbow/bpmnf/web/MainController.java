package com.maxbilbow.bpmnf.web;

import com.maxbilbow.bpmnf.config.FDLConstants;
import com.maxbilbow.bpmnf.framework.Renumberer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/")
public class MainController
{
  
  @Resource
  private Renumberer mRenumberer;
  
  @RequestMapping
  public ModelAndView get()
  {
    return new ModelAndView("index");
  }
  
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public @ResponseBody String uploadAndRenumber(
          final MultipartFile file, final String prefix, final String suffix, final Integer padding, final Integer start, final Integer shift, final
          HttpServletResponse aResponse)
          throws IOException
  {
    
    final String xml = mRenumberer.renumber(new String(file.getBytes(), FDLConstants.defaultCharset()),start,shift,prefix,suffix,padding);
  
    final byte[] bytes = xml.getBytes(FDLConstants.defaultCharset());
    
    try (final OutputStream os = aResponse.getOutputStream())
    {
      final String streamedFileName =
              StringUtils.substringBeforeLast(file.getOriginalFilename(), ".") + "_renumbered.bpmn";
  
      aResponse.setHeader("Content-Disposition", "attachment; filename=\"" + streamedFileName + "\"");
  
      aResponse.setContentType("application/xml");
  
      aResponse.setContentLength(bytes.length);
  
      FileCopyUtils.copy(bytes, os);
    }
    return xml;
  }
}
