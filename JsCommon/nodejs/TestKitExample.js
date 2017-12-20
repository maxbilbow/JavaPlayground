// Directory Tools
// makes importing afms js resources easy
const DirectoryTools = require("./lib/DirectoryTools");

const dt = new DirectoryTools(__dirname, this);
dt.include("utsCore/UTSValidator.js");
dt.include("utsCore/ScreenDataType.js");

// Test Kit class to perform unit tests
const TestKit = require("./lib/TestKit");

TestKit.test("Example 1 Will pass",function (Assert) {
  Assert.assertTrue(true);
});

TestKit.test("Example 2 Will fail",function (Assert) {
  Assert.assertTrue(false);
});