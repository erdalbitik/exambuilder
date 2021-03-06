// html2pdf.js
var page = new WebPage();
var system = require("system");
// change the paper size to letter, add some borders
// add a footer callback showing page numbers
page.paperSize = {
  format: "A4",
  orientation: "portrait",
  margin: {left:"1.5cm", right:"0cm", top:"2.5cm", bottom:"0cm"},
  footer: {
    height: "1.0cm",
    contents: phantom.callback(function(pageNum, numPages) {
    	if(system.args[3] && system.args[3] == 'true') {
    		return "<div style='text-align:center;'><small>" + pageNum +" / " + numPages + "</small></div>";
    	} else return "";
    })
  }
};
//page.zoomFactor = (system.os.name === 'linux'?  0.654545: 0.50)
// assume the file is local, so we don't handle status errors
page.open(system.args[1], function (status) {
  // export to target (can be PNG, JPG or PDF!)
  page.render(system.args[2]);
  phantom.exit();
});