const fs = require('fs');
const readline = require("readline");
const {google} = require("googleapis");

const SCOPES = ['https://www.googleapis.com/auth/spreadsheets.readonly'];

const auth = new google.auth.GoogleAuth({
    keyFile:LINK_TO_KEYS,
    scopes: ["https://www.googleapis.com/auth/spreadsheets.readonly"]
})

module.exports = (app) => {
  app.locals.google_auth  = auth;
  app.locals.sheets = google.sheets({version:"v4", auth})
}