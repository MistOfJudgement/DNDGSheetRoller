Dnd Sheet Integration
By Tushar Rangaswamy
On startup, the app should show a text input field and a button. The text field should be blank to load a default sheet or link to a google sheet copied from this template (https://docs.google.com/spreadsheets/d/1ApmbXHTln99fPTUpanyQRTXNzXbQ8UBTt3Uq8xInQKw/edit#gid=359784640)
If you link to a sheet, it should have at minimum, the left 6 ability scores filled according the green note and the cell labeled "EXPERIENCE" should be a non-negative integer
Clicking the button loads the character sheet into the app using a custom api hosted on Director. The related files for director are also included with sensitive information redacted

The loaded character sheet should have two cards labeled "Ability Scores" and "Skills"
Each of the six ability modifiers can be selected in before clicking the dice button located at the bottom of the screen.
Each modifer has two numbers depending on whether you wish to roll a check or a save, indicated by the buttons below the 6 scores
If a skill is selected it will automatically change the appropriate abilities that will need to be rolled.
(Note, the numbers on the skills do not reflect the actual modifier that needs to be applied.
Pressing the dice button on the bottom will present a snackbar popup with the roll of a 20 sided dice plus the selected modifier.

This project implements the Volley Request to pull data from a custom end point. This endpoint uses the google apis to interface with a google sheet. It also uses the views added by the Material library. 
The app creates a custom type of OnClickListener that makes selections mutually exclusive.