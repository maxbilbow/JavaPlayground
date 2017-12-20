# Node Tests


#### Key Tools:
 * [TestKit](lib/TestKit.js) - a simple framework for running unit tests
 * [DirectoryTools](lib/DirectoryTools.js) - easily include javascript resources from [webapp/static/js](../../main/webapp/static/js)

## Getting Set Up

   1. Install Node: https://nodejs.org
   2. (optional) Intellij node plugin - get this installed.
   3. Run tests.
   
If you have the node plugin installed, you should be able to right click and run the test files.
Otherwise, navigate to the test directory and run the following in a terminal:

```batch
    cd /src/test/nodejs
    node UTSValidatorTest.js
```

## Using TestKit.js

Simple NodeJS test case. More comprehensive libraries are available
but this way requires the lease faff.

#### 1. Import the Things
```javascript

    // Directory Tools 
    // makes importing afms js resources easy
    const DirectoryTools = require("./lib/DirectoryTools");
   
    const dt = new DirectoryTools(__dirname, this);
    dt.include("utsCore/UTSValidator.js");
    dt.include("utsCore/ScreenDataType.js");
    
    // Test Kit class to perform unit tests
    const TestKit = require("./lib/TestKit");
    
```

#### 2. Mock any required methods (e.g. UTSTools which uses a lot of ajax and jQuery)

```javascript

   /**
    * Mock UTSTools
    */
   TestKit.mock("UTSTools", {
         //Method that requires ajax request and session
         ifLoggedIn: function(aCallback) {
           aCallback();
         }
   });
 
```


#### 3. Define tests

```javascript

    TestKit.test("This test will pass", (Assert) => {
      Assert.assertEqual(true,true, "true should === true");
      Assert.assertNotEqual(true,false,"true should !== false");
    });

    TestKit.test("This test will fail", (Assert) => {
      Assert.assertTrue(false, "Obviously not true.");
    });

```

#### 4. Execute Test File with Node

 * *Intellij (with node plugin):* Right click > Run / Debug; or
 * *Terminal:*
  ```batch
          cd location/of/test/file
          node YourTestFile.js
  ```
    
#### Output:

Results are displayed in the terminal.

```
 TEST RESULTS
 Test1 [This test will pass] PASSED 
 Test2 [This test will fail] FAILED 
 Failed Assertion: Obviously not true.

 2 TESTS COMPLETE WITH 1 FAILURE(S)
 >>>  SUCCESS: 1 
 >>>  FAILURE: 1 

```
