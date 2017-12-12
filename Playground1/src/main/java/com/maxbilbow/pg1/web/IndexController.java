package com.maxbilbow.pg1.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController
{
  @RequestMapping
  public ModelAndView get()
  {
    return new ModelAndView("index");
  }
}
