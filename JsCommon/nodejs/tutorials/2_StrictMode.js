/**
 * Lesson 2: Strict Mode
 *
 * It's a good idea to always invoke strict mode. It'll force you to write better code and avoid dangerous habbits
 */

const object = {value:true};


object.value = 1;
console.log("We changed tha value. Noting strange about that.",object);

Object.freeze(object);
object.value = 2;
console.log("Did we change the object's value? Maybe not but who can say for sure?",object);

(() => {
  "use strict";
  try
  {
    object.value = 3;
    console.log("Oh, dang! It changed!",object);
  }
  catch (e)
  {
    console.log("Good! We are not allowed to change a frozen object.\n",e);
  }
})();

//console.log(i); // throws an error in node
