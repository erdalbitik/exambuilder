var wp = require('webpage');
var system = require("system");

var page = wp.create();

page.viewportSize = { width: 597, height: 597};

page.open(system.args[1], function(){
    console.log(page.evaluate(function(){
        return document.getElementById('questionTable').offsetHeight;
    }));
    phantom.exit();
});

   