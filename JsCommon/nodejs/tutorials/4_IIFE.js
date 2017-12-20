/**
 * Lesson 4: IIFE
 *
 * The following is an IIFE (Immediately-Invoked Function Expression)
 *
 * IIFEs are the main way of separating scopes in javascript and keeping the global namespace clean
 *
 */
(function () {
 console.log("I have been called immediately");
})();


(function () {
  console.log("So have I but look at my brackets \"}())\" ");
}());


(() => {
  console.log("I have been called immediately but don't try this on IE 11!\n");
})();

(function incrementCount(indent) {
  console.log(indent + "I have been called immediately and can call myself!");
  indent += "-";
  if (indent.length < 5)
    incrementCount(indent);
})("");

const result = (function () {
  return "\nI am the result of an IIFE\n";
})();

console.log(result);


//Looking at scope

var text = "This text must not change";
console.log(text);
//Note: this could be an if statement or loop, etc...
{
  var text = "If this was Java, you would not be reading this.";
}
text = "Lets try that again";
console.log(text);
(function () {
  var text = "Can you see this text? Nope! IIFE has a different scope!";
}());
console.log(text);