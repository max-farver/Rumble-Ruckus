var date = new Date();
var a = 5;
var b = 5;
var match

if ( a === b ) {
    match = true;
} else {
    match = false;
}

document.body.innerHTML = "<h1>Today is: " + date + "</h1>";

var pens;
pens = ["red", "blue", "green", "orange"];

// console.log("Before: ", pens);
// console.log(match);
// console.log("Array length: ", pens.length);
// pens.shift();
// pens.unshift("purple", "black");
// pens.pop()
// pens.push("pink", "prussian blue");
// console.log("After: ", pens);

function multiply() {
    var result = 3 * 4;
    console.log("3 multiplied by 4 is ", result);
}

multiply();

var divided = function() {
    var result = 3 / 4;
    console.log("3 divided by 4 is ", result);
}

divided();

// Immediately Invoked Function Expression.
// Runs as soon as the browser finds it:
(function() {
    var result = 12 / 0.75;
    console.log("12 divided by 0.75 is ", result);
}())
