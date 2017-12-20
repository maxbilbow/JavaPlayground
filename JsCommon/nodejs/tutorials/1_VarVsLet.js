/**
 * Lesson 1: var
 *
 * The let keyword is supported on IE 11 however some more advanced uses are not.
 *
 * To check what can be used, see https://www.caniuse.com
 */
var i = "unchanged";

/**
 * Using var
 */
(() => {
  var i = 1;
  console.log("var i ===", i); //What does this print?
  {
    if (true) {
      var i = 2;
    }
    console.log("var i ===", i); //What does this print?
  }
  for (var i = 0; i < 100; ++i) {
    //loooop!
  }
  console.log("var i ===", i); //What does this print?
})();

/**
 * using let
 */
(() => {
  let i = 1;
  console.log("let i ===", i);
  {
    if (true) {
      let i = 2;
    }
  }
  console.log("let i ===", i); //What does this print?
  for (let i = 0; i < 100; ++i) {
    //loooop!
  }
  console.log("let i ===", i); //What does this print?
})();

console.log("Note that outside of an IIFE, i is ",i);
//console.log(i); // throws an error in node
