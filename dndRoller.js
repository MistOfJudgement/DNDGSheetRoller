const express = require("express");
const router = express.Router({strict:true});

function getSheetId(url) {
    return /\/spreadsheets\/d\/([a-zA-Z0-9-_]+)/.exec(resourceUrl)[1];
}
/**
 * Convert a cell reference from A1Notation to 0-based indices (for arrays)
 * or 1-based indices (for Spreadsheet Service methods).
 *
 * @param {String}    cellA1   Cell reference to be converted.
 * @param {Number}    index    (optional, default 0) Indicate 0 or 1 indexing
 *
 * @return {object}            {row,col}, both 0-based array indices.
 *
 * @throws                     Error if invalid parameter
 */
function cellA1ToIndex( cellA1, index ) {
  // Ensure index is (default) 0 or 1, no other values accepted.
  index = index || 0;
  index = (index == 0) ? 0 : 1;

  // Use regex match to find column & row references.
  // Must start with letters, end with numbers.
  // This regex still allows induhviduals to provide illegal strings like "AB.#%123"
  var match = cellA1.match(/(^[A-Z]+)|([0-9]+$)/gm);

  if (match.length != 2) throw new Error( "Invalid cell reference" );

  var colA1 = match[0];
  var rowA1 = match[1];

  return { row: rowA1ToIndex( rowA1, index ),
           col: colA1ToIndex( colA1, index ) };
}

/**
 * Return a 0-based array index corresponding to a spreadsheet column
 * label, as in A1 notation.
 *
 * @param {String}    colA1    Column label to be converted.
 *
 * @return {Number}            0-based array index.
 * @param {Number}    index    (optional, default 0) Indicate 0 or 1 indexing
 *
 * @throws                     Error if invalid parameter
 */
function colA1ToIndex( colA1, index ) {
  if (typeof colA1 !== 'string' || colA1.length > 2) 
    throw new Error( "Expected column label." );

  // Ensure index is (default) 0 or 1, no other values accepted.
  index = index || 0;
  index = (index == 0) ? 0 : 1;

  var A = "A".charCodeAt(0);

  var number = colA1.charCodeAt(colA1.length-1) - A;
  if (colA1.length == 2) {
    number += 26 * (colA1.charCodeAt(0) - A + 1);
  }
  return number + index;
}


/**
 * Return a 0-based array index corresponding to a spreadsheet row
 * number, as in A1 notation. Almost pointless, really, but maintains
 * symmetry with colA1ToIndex().
 *
 * @param {Number}    rowA1    Row number to be converted.
 * @param {Number}    index    (optional, default 0) Indicate 0 or 1 indexing
 *
 * @return {Number}            0-based array index.
 */
function rowA1ToIndex( rowA1, index ) {
  // Ensure index is (default) 0 or 1, no other values accepted.
  index = index || 0;
  index = (index == 0) ? 0 : 1;

  return rowA1 - 1 + index;
}
function readSheetWithA1(sheet, location) {
    let loc = cellA1ToIndex(location, 0);
    return sheet[loc.row][loc.col]
}
function getAbilityScore(sheet) {
    scoreMapping = {
        "str":"15",
        "dex":"20",
        "con":"25",
        "int":"30",
        "wis":"35",
        "cha":"40",
    }
    result = {}
    for(let i in scoreMapping) {
        if (scoreMapping.hasOwnProperty(i))
            result[i] = sheet.getCellByA1("C"+scoreMapping[i]).value
    }
    console.log(result);
    return result
}


let attributes = {
    abilityScores: ["str", "dex", "con", "int", "wis", "cha"],
    skills: ["Acrobatics",
        "Animal Handling",
        "Arcana",
        "Athletics",
        "Deception",
        "History",
        "Insight",
        "Intimidation",
        "Investigation",
        "Medicine",
        "Nature",
        "Perception",
        "Performance",
        "Persuasion",
        "Religion",
        "Sleight of Hand",
        "Stealth",
        "Survival"
    ]
}
let locations = require("/site/public/setup/dndSheetLocations.json")
router.get("/roll", (req, res) => {
    res.render("dndSheet");
});
function grabSheet(sheets, url, callback) {

    sheets.spreadsheets.values.get({
        spreadsheetId: url.match(/\/([\w-_]{15,})/)[1],
        range: "A1:BC180"
    }, callback)
}
router.get("/api/getSheet", (req, res)=>{
    grabSheet(req.app.locals.sheets, req.query.q, (err, s_res)=>{
        res.json(s_res)
    })
    
})

router.get("/api/getAbilityScores", (req, res)=> {
    
    
    grabSheet(req.app.locals.sheets, req.query.q, (err, s_res)=>{
        if (err) res.send(err);
        let scoreMapping = {
            "str":"15",
            "dex":"20",
            "con":"25",
            "int":"30",
            "wis":"35",
            "cha":"40",
        }
        result = {}
        for(let i in scoreMapping) {
            if (scoreMapping.hasOwnProperty(i))
                result[i] = readSheetWithA1(s_res.data.values, "C"+scoreMapping[i])
        }
        res.json(result)
    })
})

router.get("/api/getReducedSheet", (req, res)=> {
    grabSheet(req.app.locals.sheets, req.query.q, (err, s_res)=>{
        if (err) return res.send(err);
        if ("errors" in s_res) return res.json({error:"bad"})
        let data = {};
        for (let i in locations) {
            if (locations.hasOwnProperty(i)) {
                // console.log(i)
                if (typeof(locations[i]) == "string") {
                    data[i] = readSheetWithA1(s_res.data.values, locations[i])
                } else {
                    data[i] = {}
                    for (let j in locations[i]) {
                        // console.log("\t" + j)
                        data[i][j] = readSheetWithA1(s_res.data.values, locations[i][j]);
                        
                    }
                }
            }
        }
        //fixing int because aaaaaa
        data.abilityScores._int = data.abilityScores.int
        data.abilityModifiers._int = data.abilityModifiers.int;
        data.savingThrows._int = data.savingThrows.int;
        res.json(data);
    })
})
module.exports = router;
