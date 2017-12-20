/**
 * Lesson 3: Classes
 *
 * In ES6, we can declare actual classes however this is not supported in IE 11
 * If you write JS classes, you will need to use https://babeljs.io/ to convert it to ES5 friendly JS objects
 *
 * @see https://caniuse.com
 */
(() => {
  "use strict";
  console.log("ES6 Classes:");
  class Foo {
    constructor(...args) {
      console.log(args.length, "arguments received in constructor");
    }
    objectMethod() {
      console.log("Hello, I am a Foo");
    }

    static classMethod() {
      console.log("I am a Foo class");
    }
  }

  class Bar extends Foo {
    objectMethod() {
      super.objectMethod();
      console.log("...but I am also a Bar");
    }
  }

  Foo.classMethod();
  new Foo().objectMethod();
  new Bar().objectMethod();
})();



/**
 * IE11 Friendly classes
 */
(() => {
  "use strict";
  console.log("\nES5 Classes:");

  function Foo() {
    console.log(arguments.length, "arguments received in constructor");
  }

  Foo.prototype.objectMethod = function objectMethod() {
    console.log("Hello, I am a Foo");
  };

  Foo.classMethod = function classMethod() {
    console.log("I am a Foo class");
  };

  const _super = Foo.prototype;
  function Bar() {
    this.super = Foo.prototype;
    return Foo.apply(this, arguments);
  }

  Bar.prototype = Object.create(_super);


  Bar.prototype.objectMethod = function objectMethod() {
    _super.objectMethod.call(this);
    console.log("...but I am also a Bar");
  };

  Foo.classMethod();
  new Foo().objectMethod();
  new Bar().objectMethod();
})();


/**
 * ES5 Friendly classes converted with babeljs.io
 */
(() => {
  "use strict";

  function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

  function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

  function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

  var Foo = function () {
    function Foo() {
      _classCallCheck(this, Foo);

      console.log(arguments.length, "arguments received in constructor");
    }

    Foo.prototype.objectMethod = function objectMethod() {
      console.log("Hello, I am a Foo");
    };

    Foo.classMethod = function classMethod() {
      console.log("I am a Foo class");
    };

    return Foo;
  }();

  var Bar = function (_Foo) {
    _inherits(Bar, _Foo);

    function Bar() {
      _classCallCheck(this, Bar);

      return _possibleConstructorReturn(this, _Foo.apply(this, arguments));
    }

    Bar.prototype.objectMethod = function objectMethod() {
      _Foo.prototype.objectMethod.call(this);
      console.log("...but I am also a Bar");
    };

    return Bar;
  }(Foo);
})();


//console.log(i); // throws an error in node
