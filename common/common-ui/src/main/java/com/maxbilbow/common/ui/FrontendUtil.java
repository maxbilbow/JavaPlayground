package com.maxbilbow.common.ui;



import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrontendUtil
{
  
  private Charset charset = Charset.defaultCharset();
  private Reference<String> view = null;
  
  private List<String> resources = new ArrayList<>();
  
  @PostConstruct
  public void init()
  {
    System.out.println("HERE");
  }
  public String getJs() throws IOException
  {
    String view = this.view != null ? this.view.get() : null;
    if (view == null)
    {
      final StringBuilder sb = new StringBuilder();
      for (String resource : resources)
      {
        try (final InputStream startsWithStream = this.getClass().getResourceAsStream("/bpmnViewer/startsWith.js"))
        {
          sb.append(IOUtils.toString(startsWithStream, charset));
        }
      }
      this.view = new SoftReference<>(view = sb.toString());
    }
    return view;
  }
}
