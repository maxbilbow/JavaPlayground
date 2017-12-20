
(()=> {
  "use strict";

  class Assert
  {
    static fail(aMessage)
    {
      if (aMessage)
        throw "Failed Assertion: " + aMessage;
      else
        throw Error("Assertion Failed: Unknown Reason");
    }

    static assertTrue(a, aMessage)
    {
      // console.assert(a, aMessage || a + ' === ' + true);
      if (a !== true) {
        Assert.fail(aMessage || a + ' !== ' + true);
      }
    }


    static assertFalse(a, aMessage)
    {
      if (a !== false) {
        Assert.fail(aMessage || a + ' !== ' + false);
      }
      // console.assert(a === false, aMessage || a + ' === ' + false);
    }

    static assertEqual(a, b, aMessage = "Assert Equal ")
    {
      Assert.assertTrue(a === b, aMessage + " " + a + ' !== ' + b);
    }

    static assertNotEqual(a, b, aMessage)
    {
      Assert.assertFalse(a === b, aMessage || "Assert Not Equal " + a + ' === ' + b);
    }


    static assertEquivalent(a, b, aMessage)
    {
      Assert.assertTrue(a == b, aMessage || "Assert Equivalent " + a + ' != ' + b);
    }

    static assertNotEquivalent(a, b, aMessage = "Assert Not Equivalent " + a + ' == ' + b)
    {
      Assert.assertFalse(a == b, aMessage);
    }

    static assertNull(a, aMessage = "Assert Null: " + JSON.stringify(a))
    {
      if (a !== null) {
        Assert.fail(aMessage);
      }
    }

    static assertNotNull(a, aMessage = "Assert Not Null: null")
    {
      if (a === null) {
        Assert.fail(aMessage);
      }
    }

    static assertUndefined(a, aMessage = "Assert undefined: " + JSON.stringify(a))
    {
      if (a !== undefined) {
        Assert.fail(aMessage);
      }
    }

    static assertNotUndefined(a, aMessage = "Assert Not Undefined: undefined")
    {
      if (a === undefined) {
        Assert.fail(aMessage);
      }
    }

    static assertNullOrUndefined(a, aMessage = "Assert Null OR Undefined: " + JSON.stringify(a))
    {
      if (a !== null && a !== undefined) {
        Assert.fail(aMessage);
      }
    }

    static assertNotNullOrUndefined(a, aMessage = "Assert Null OR Undefined: " + a)
    {
      if (a === null || a === undefined) {
        Assert.fail(aMessage);
      }
    }

    static assertEmpty(a, aMessage = "Assert Empty: " + a)
    {
      if (a !== null && a !== undefined && a === "") {
        Assert.fail(aMessage);
      }
    }
    static assertNotEmpty(a, aMessage = "Assert Not Empty: " + a)
    {
      if (a === null || a === undefined || a === "") {
        Assert.fail(aMessage);
      }
    }

    /**
     * Asset the method throws a specific error
     * Note: this is probably
     *
     * @param aMessage {string|function} optional test message
     * @param aCallback {function|Error|string|object} function to test
     * @param aExpected {Error|string|object} optional error type or error message to match
     */
    static assertThrows(aMessage, aCallback = aMessage, aExpected = null)
    {
      if (typeof aMessage === aCallback) {
        aMessage = null;
      }
      let errorThrown;
      try {
        aCallback();
        errorThrown = false;
      }
      catch (e) {
        errorThrown = true;
        if (aExpected) {
          const pfx = aMessage ? aMessage + "\n" : "";
          if (typeof aExpected === 'string')
            Assert.assertEqual(aExpected, e.message || e, pfx + "  + Incorrect error thrown");
          else if (aExpected instanceof Error) {
            Assert.assertEqual(typeof aExpected, typeof e, pfx +  `  + Error type "${typeof e}" does not match expected "${typeof aExpected}"`);
            if (aExpected.message)
              Assert.assertEqual(aExpected.message, e.message, pfx + "  + Error messages do not match");
          }
        }
      }

      if (!errorThrown)
      {
        let msg = "NO ERROR THROWN";
        if (aMessage)
          msg += ": " + aMessage;
        this.fail(msg);
      }
    }

    /**
     *
     * @param message {Array|string}
     * @param expected {Array}
     * @param test {Array}
     */
    static assertArrayEquals(expected, test, message = "Arrays not equal")
    {
      if (test === null)
      if (expected.length !== test.length)
        this.fail(`${message} Lengths differ:\n - Expected: ${expected}\n - Found: ${test}`);

      for (let i=0;i<expected.length;++i) {
        this.assertEqual(expected[i],test[i],`${message} Index ${i} differs:`);
      }
    }
  }


  module.exports = Assert;
})();
