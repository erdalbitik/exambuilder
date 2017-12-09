/*'use strict';

const puppeteer = require('puppeteer');    
(async() => {    
const browser = await puppeteer.launch();
const page = await browser.newPage();    
await page.goto('file://'+process.argv[2]);    
await page.pdf({
  path: process.argv[3],
  format: 'A4',
  margin: {
        top: "20px",
        left: "20px",
        right: "20px",
        bottom: "20px"
  }    
});    
await browser.close();    
})();
*/
const puppeteer = require('puppeteer');

const PDF_OPTIONS = {path: process.argv[3], format: 'A4', printBackground: true, landscape: false};
const IMAGE_LOADING_TIMEOUT = 60 * 1000 * 5;


function waitForAllImagesToCompleteLoading() {
  const allImagesInDocument = Array.from(document.getElementsByTagName('img'));
  return allImagesInDocument
      .map((img) => img.complete)
      .every((completeStatus) => completeStatus);
}

let browser;
puppeteer.launch({headless: true})
  .then((newBrowser) => {
    browser = newBrowser;
    return browser.newPage();
  })
  .then((page) => {
    return page.goto('file://'+process.argv[2])
    .then(() => page.waitForFunction(waitForAllImagesToCompleteLoading, {timeout: IMAGE_LOADING_TIMEOUT}))
    .then(() => page.pdf(PDF_OPTIONS))
    .then(() => page.close());
  })
  .then(() => browser.close());