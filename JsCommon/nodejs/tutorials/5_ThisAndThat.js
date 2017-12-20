this.description = "The Document";

(() => {
  "use strict";
  const Col = require("../lib/TerminalColour");
  console.header = function () { //logs to console in blue.
    let args = [Col.FgBlue];
    args.push.apply(args, arguments);
    args.push(Col.Reset);
    console.log();
    console.log.apply(null, args);
  };
})();

/**
 * Lesson 5: This and That
 *
 * The this keyword is problematic at best. It's a good idea to know when to use it.
 */
(() => {
  "use strict";
  let Foo = (function () {
    function Foo() {}

    Foo.prototype.description = "this is a Foo";

    Foo.prototype.print = function () {
      console.log("Foo.print()");
      console.log(this.description);
    };

    Foo.prototype.invokeCallback =function(callback)
    {
      callback.call(this);// Foo is the caller
    };

    return Foo;
  })();

  let Bar = (function() {
    function Bar() {
      this.description = "this is a Bar";
      this.print = function () {
        console.log("bar.print()");
        console.log(this.description);
      };
    }
    return Bar;
  })();



  let foo = new Foo();
  let bar = new Bar();
  let object = {
    description: "this is a normal object",
    print: function () {
      console.log("object.print()");
      console.log(this.description);
    }
  };

  console.header("Print all");
  foo.print();
  bar.print();
  object.print();


  console.header("foo.invokeCallback(function () {\n" + "    bar.print(); //bar is the caller\n" + "  });");
  foo.invokeCallback(function () {
    bar.print(); //bar is the caller
  });
  console.header("foo.invokeCallback(bar.print)");
  foo.invokeCallback(bar.print);

  const that = this;

  console.header("function (){}");
  foo.invokeCallback(function () { //this can be assigned by the caller
    console.log("What is this?", this);
    console.log("What is that?", that);
  });

  console.header("() => {}"); // not allowed in IE11
  foo.invokeCallback(() => { // this will not be assigned for an arrow function
    console.log("What is this?", this);
    console.log("What is that?", that);
  });

  console.log("Conclusion: it's a good idea to assign \"this\" to a constant when using callbacks");
})();

console.header("What is this in different scopes?");
console.log("DOCUMENT, this ===",this);

/**
 * Inside an arrow function, "this" has not changed
 */
(()=>{
  "use strict";
  const that = this === global ? "global scope" : this;
  console.log("IIFE () => {} (strict mode), this ===",that);
})();


(()=>{
  const that = this === global ? "global scope" : this;
  console.log("IIFE (() => {})() (not strict) this ===",that);
})();

/**
 * Inside a IIFE function(){}, this has not been defined when using STRICT MODE
 */
(function () {
  "use strict";
  const that = this === global ? "global scope" : this;
  console.log("IIFE function(){} (strict mode), this === ",that);
})();

/**
 * Without STRICT MODE, this could be anything. In this case, it's the global object.
 *
 * In a browser, it may be window.
 */
(function () {
  const that = this === global ? "global scope" : this;
  console.log("IIFE function(){} (not strict), this === ",that); // Throws an error
})();