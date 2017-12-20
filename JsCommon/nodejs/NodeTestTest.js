/**
 * Experimenting with other test libraries.
 * Proper unit testing described here:
 * https://www.codementor.io/davidtang/unit-testing-and-tdd-in-node-js-part-1-8t714s877
 */

/*
var chai = require('chai');
var expect = chai.expect; // we are using the "expect" style of Chai
// var CartSummary = require('./../../src/part1/cart-summary');


function isTrue()
{
  return true;
}

function isFalse()
{
  return true;
}

describe("TestTest",function () {
  it("Should return True", function () {
    expect(isTrue()).to.equal(true);
    expect(isFalse()).to.equal(false);
  });
});*/

module.exports = {
  'Test 1' : function(test) {
    test.expect(1);
    test.ok(true, "This shouldn't fail");
    test.done();
  },
  'Test 2' : function(test) {
    test.expect(2);
    test.ok(1 === 1, "This shouldn't fail");
    test.ok(false, "This should fail");
    test.done();
  }
};
