
(() => {
  const TestKit = require("./TestKit");

  /**
   * Mocking tests
   */
  (() => {
    const MOCKED_OBJECT = "MockedObject";

    TestKit.mock(MOCKED_OBJECT, true); //global MockedObject === true

    TestKit.test("Mocked bool exists", function (Assert) {
      Assert.assertTrue(global[MOCKED_OBJECT]);
    });

    TestKit.unmock(MOCKED_OBJECT); //global MockedObject is undefined

    TestKit.test("Mocked object was deleted", function (Assert) {
      Assert.assertUndefined(global[MOCKED_OBJECT]);
    });

    TestKit.mock(MOCKED_OBJECT, {value: true}); //global MockedObject === {}

    TestKit.test("Mocked object === {value: true}", function (Assert) {
      Assert.assertEqual('object', typeof global[MOCKED_OBJECT], "Mocked object is {object}");
      Assert.assertTrue(global[MOCKED_OBJECT].value);
    });

    {
      let globalObject = global[MOCKED_OBJECT]; //this should not exist
      TestKit.test("Object deleted and reloaded between tests", function (Assert) {
        Assert.assertUndefined(globalObject);
        Assert.assertTrue(global[MOCKED_OBJECT].value);
      });
    }

    TestKit.mock(MOCKED_OBJECT, () => ({value: true})); //global MockedObject === {value: true}

    TestKit.test("callback returns object {value: true}", function (Assert) {
      Assert.assertTrue(global[MOCKED_OBJECT].value);
    });

    //Mocking exceptions
    {
      TestKit.unmock(MOCKED_OBJECT);
      let errorThrown = false;
      try {
        TestKit.mock(MOCKED_OBJECT);
      }
      catch (e)
      {
        errorThrown = true;
      }
      TestKit.test("Parameter must be provided", function (Assert) {
        Assert.assertTrue(errorThrown,"undefined mocking error");
        Assert.assertThrows("Mocking during tests not allowed",() => {
          TestKit.mock(MOCKED_OBJECT); //global MockedObject is undefined
        });
        Assert.assertThrows("Un-Mocking during tests not allowed",() => {
          TestKit.unmock(MOCKED_OBJECT); //global MockedObject is undefined
        });
        Assert.assertUndefined(global[MOCKED_OBJECT]);
      });
    }

    //Class Mocking
    {
      class A
      {
        static exists()
        {
          return true;
        }
      }

      TestKit.mock(A); //global MockedObject is A

      TestKit.test("Mocked Class A returned without naming", function (Assert) {
        Assert.assertNotNullOrUndefined(global["A"]);
        Assert.assertEqual(A, global["A"]);
        Assert.assertTrue(global["A"].exists());
      });

      TestKit.unmock(A); //global MockedObject is A

      TestKit.test("Mocked Class A can be removed", function (Assert) {
        Assert.assertUndefined(global["A"]);
      });

      TestKit.mock(MOCKED_OBJECT, A); //global MockedObject is A

      TestKit.test("Mocked Class A returned", function (Assert) {
        Assert.assertNotNullOrUndefined(global[MOCKED_OBJECT]);
        Assert.assertEqual(A, global[MOCKED_OBJECT]);
        Assert.assertTrue(global[MOCKED_OBJECT].exists());
      });

      TestKit.mock(MOCKED_OBJECT, () => A); //global MockedObject is A

      TestKit.test("Default Mocked callback Class returned", function (Assert) {
        Assert.assertNotNullOrUndefined(global[MOCKED_OBJECT]);
        Assert.assertEqual(A, global[MOCKED_OBJECT]);
        Assert.assertTrue(global[MOCKED_OBJECT].exists());
      });
    }

    //ES5 class mocking
    {
      function B()
      {

      }

      TestKit.mock(MOCKED_OBJECT, B); //global MockedObject is A

      TestKit.test("Function treated as callback", function (Assert) {
        Assert.assertUndefined(global[MOCKED_OBJECT]);
      });

      B.exists = function()
      {
        return true;
      };

      TestKit.mock(MOCKED_OBJECT, B); //global MockedObject is A

      TestKit.test("Mocked Function with attributes returned", function (Assert) {
        Assert.assertNotNullOrUndefined(global[MOCKED_OBJECT]);
        Assert.assertEqual(B, global[MOCKED_OBJECT]);
        Assert.assertTrue(global[MOCKED_OBJECT].exists());
      });

      TestKit.mock(MOCKED_OBJECT, () => B); //global MockedObject is A

      TestKit.test("Mocked callback Class returned", function (Assert) {
        Assert.assertNotNullOrUndefined(global[MOCKED_OBJECT]);
        Assert.assertEqual(B, global[MOCKED_OBJECT]);
        Assert.assertTrue(global[MOCKED_OBJECT].exists());
      });
    }
  })();


})();