package com.maxbilbow.experimental.aspect;

public aspect AspectExample
{
  pointcut callGetString() :
          call(String AspectTest.getString());
  
//  before() : callGetString() { }
//
//  after() : callGetString() { }
  
  String around() : callGetString() {
    return "aspect" + proceed();
  }
}
