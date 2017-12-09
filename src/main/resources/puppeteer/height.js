const puppeteer = require('puppeteer');    
(async() => {    
const browser = await puppeteer.launch();
const page = await browser.newPage();    
await page.goto('file://'+process.argv[2]);    
const height = await page.evaluate(() => {
	return document.getElementById('questionTable').offsetHeight;
});
console.log(height);
await browser.close();    
})();

