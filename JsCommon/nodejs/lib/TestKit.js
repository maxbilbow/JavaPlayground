/**
 * Created by max on 09/09/16.
 */
(()=> {

  const Colour = require("./TerminalColour");
  const Assert = require("./Assert");

  const privateScope = new WeakMap();
  function $(test)
  {
    let data = privateScope.get(test);
    if (!data)
      privateScope.set(test, data = {});
    return data;
  }

  function isCallback(obj)
  {
    return typeof obj === "function" && Object.keys(obj).length === 0 && !/^\s*class\s+/.test(obj.toString())
  }

  /**
   * @this {TestKit}
   * @param aName
   * @param aTest
   * @return {{name: *, test: *}}
   * @private
   */
  function _getTest(aName, aTest)
  {
    let testName;
    const count = ($(this)._count++) + 1;
    if (aTest === undefined) {
      aTest = aName;
      testName = 'Test' + count;
    }
    else {
      testName = 'Test' + count + ' [' + aName + ']';
    }
    return {name: testName, test: aTest};
  }

  /**
   * @this {TestKit}
   * @private
   */
  function _before()
  {
    $(this)._testActive = true;
    for (let [name, get] of $(this)._globals.entries())
      global[name] = get();
  }

  /**
   * @this {TestKit}
   * @private
   */
  function _after()
  {
    $(this)._testActive = false;
    for (let name of $(this)._globals.keys())
      delete global[name];
  }

  /**
   * @this {TestKit}
   * @private
   */
  function _report()
  {
    let result = `${$(this)._count} TESTS COMPLETE`;

    if ($(this)._failure > 0) {
      result += ` WITH ${$(this)._failure} FAILURE(S)`;
    }

    console.log();
    console.log(Colour.Underscore, "TEST RESULTS", Colour.End);
    for (let i in $(this)._results) {
      console.log.apply(null, $(this)._results[i]);
    }
    console.log();
    console.log(Colour.Underscore, result, Colour.End);
    console.log('>>>', Colour.FgGreen, 'SUCCESS:', $(this)._success, Colour.End);
    console.log('>>>', Colour.FgRed, 'FAILURE:', $(this)._failure, Colour.End);
  }

  class TestKit
  {
    constructor()
    {
      $(this)._count = 0;
      $(this)._success = 0;
      $(this)._failure = 0;
      $(this)._results = [];
      this.Assert = Assert;
      $(this)._globals = new Map();
      process.on('exit', () => {
        _report.call(this);
      });
    }

    /**
     * Mock objects to exist in global scope only during tests.
     * @param aObjectName {string|*}
     * @param aObjectCallback {function|*} either an object or a callback that returns an object.
     *        Default value is a function that returns undefined.
     * @return {TestKit}
     */
    mock(aObjectName, aObjectCallback = undefined)
    {
      if ($(this)._testActive)
        throw Error("Cannot mock objects during a test");
      if (typeof aObjectName === 'function') {
        aObjectCallback = aObjectName;
        aObjectName = null;
      }
      else if (typeof aObjectName !== "string")
        throw Error("Name not provided for callback: " + aObjectCallback || aObjectName);
      else if (aObjectCallback === undefined)
        throw new Error(`Callback ${aObjectName} was undefined`);

      let callback;
      if (isCallback(aObjectCallback))
        callback = aObjectCallback;
      else
        callback = () => aObjectCallback;

      if (aObjectName === null)
        aObjectName = callback().name;

      $(this)._globals.set(aObjectName, callback);

      return this;
    }

    unmock(aObject){
      if ($(this)._testActive)
        throw Error("Cannot un-mock objects during a test");
      if (typeof aObject === 'string')
        $(this)._globals.delete(aObject);
      else if (typeof aObject === 'function' && aObject.name)
        $(this)._globals.delete(aObject = aObject.name);
      else
      {
        for (let [name,f] of $(this)._globals.entries())
        {
          if (f() === aObject) {
            aObject = name;
            break;
          }
        }
        $(this)._globals.delete(aObject);
      }
      delete global[aObject];
      return this;
    }

    test(aName, aTest)
    {
      const t = _getTest.call(this, aName, aTest);
      try {
        _before.call(this);
        t.test(this.Assert);
        $(this)._results.push([Colour.FgGreen, t.name, 'PASSED', Colour.End]);
        $(this)._success++;
      }
      catch (e) {
        $(this)._results.push([Colour.FgRed, t.name, Colour.End, Colour.BgRed + Colour.FgBlack, 'FAILED', Colour.End]);
        $(this)._results.push([Colour.FgRed, e]);
        $(this)._failure++;
      }
      finally {
        _after.call(this);
      }
      return this;
    }
  }

  module.exports = new TestKit();
})();