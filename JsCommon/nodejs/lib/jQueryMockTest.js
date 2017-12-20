

(() => {
  "use strict";

  const TestKit = require("./TestKit");

  let $;
  TestKit.mock("$",() => {
    return $ = require("./jQueryMock");
  });

  TestKit.test("GET",(Assert) => {
   Assert.fail("Not implemented");
  });


  TestKit.test("EventListeners",(Assert) => {
    let trigger = false;
    $.on('1',(...args)=>{
      Assert.assertArrayEquals(["Hello", "world"],args);
      trigger = true;
    });
    $.trigger('1',"Hello", "world");
    Assert.assertTrue("EventListener Triggered", trigger);
  });




})();