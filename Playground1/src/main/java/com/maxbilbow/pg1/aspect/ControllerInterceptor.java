package com.maxbilbow.pg1.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerInterceptor
{
  @Pointcut(value = "execution(public * *(..))")
  public void doSomething() { }
  
}
