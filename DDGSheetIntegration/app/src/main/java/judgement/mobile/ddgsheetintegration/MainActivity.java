
package judgement.mobile.ddgsheetintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    EditText urlTextBox;
    Button loadButton;
    boolean startedLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startedLoad = false;
//        setContentView(R.layout.character_sheet);
        // Instantiate the RequestQueue.
        String url = "https://" /* Endpoint doesn't work.*/ +"api/getReducedSheet?q=";
        RequestQueue queue = Volley.newRequestQueue(this);


// Add the request to the RequestQueue.

//        CharacterSheet.parseJson("{\"header\":{\"characterName\":\"Orrn Quinn Lauden\",\"classes\":\"Illusion Wizard 5\",\"race\":\"Forest Gnome\",\"playerName\":\"Tushar\",\"level\":\"5\"},\"abilityScores\":{\"str\":\"8\",\"dex\":\"13\",\"con\":\"14\",\"int\":\"18\",\"wis\":\"12\",\"cha\":\"9\"},\"abilityModifiers\":{\"str\":\"-1\",\"dex\":\"+1\",\"con\":\"+2\",\"int\":\"+4\",\"wis\":\"+1\",\"cha\":\"-1\"},\"proficiencyBonus\":\"+3\",\"savingThrows\":{\"str\":\"-1\",\"dex\":\"+1\",\"con\":\"+2\",\"int\":\"+7\",\"wis\":\"+4\",\"cha\":\"-1\"},\"skills\":{\"Acrobatics\":\"+1\",\"Animal Handling\":\"+1\",\"Arcana\":\"+7\",\"Athletics\":\"-1\",\"Deception\":\"-1\",\"History\":\"+7\",\"Insight\":\"+4\",\"Intimidation\":\"-1\",\"Investigation\":\"+7\",\"Medicine\":\"+1\",\"Nature\":\"+4\",\"Perception\":\"+1\",\"Performance\":\"-1\",\"Persuasion\":\"-1\",\"Religion\":\"+4\",\"Sleight of Hand\":\"+1\",\"Stealth\":\"+1\",\"Survival\":\"+1\"},\"combat\":{\"AC\":\"11\",\"initiative\":\"+1\",\"speed\":\"25 ft\",\"hitPointMax\":\"32\",\"currentHitPoints\":\"32\",\"condition\":\"\",\"hitDice\":\"5d6\"}}");

        urlTextBox = findViewById(R.id.urlTextBox);
        loadButton = findViewById(R.id.loadButton);
        loadButton.setOnClickListener((view -> {
            if(!startedLoad) {
                startedLoad = true;
                if (urlTextBox.getText().toString().isEmpty()) {
                    loadCharacterSheet(""); return;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + urlTextBox.getText().toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                CharacterSheet.parseJson(response);
                                if (response.contains("error")) {
                                    loadCharacterSheet("");
                                } else {
                                    loadCharacterSheet(response);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadCharacterSheet("");
                    }
                });
                queue.add(stringRequest);
            }

        }));
    }

    public void loadCharacterSheet(String data) {
        if(data.isEmpty()) {
            data = "{\"header\":{\"characterName\":\"Default\",\"classes\":\"Fighter 1\",\"race\":\"Human\",\"playerName\":\"A\",\"level\":\"1\"},\"abilityScores\":{\"str\":\"14\",\"dex\":\"14\",\"con\":\"14\",\"int\":\"14\",\"wis\":\"14\",\"cha\":\"14\"},\"abilityModifiers\":{\"str\":\"+2\",\"dex\":\"+2\",\"con\":\"+2\",\"int\":\"+2\",\"wis\":\"+2\",\"cha\":\"+2\"},\"proficiencyBonus\":\"+2\",\"savingThrows\":{\"str\":\"+4\",\"dex\":\"+2\",\"con\":\"+4\",\"int\":\"+2\",\"wis\":\"+2\",\"cha\":\"+2\"},\"skills\":{\"Acrobatics\":\"+4\",\"Animal Handling\":\"+4\",\"Arcana\":\"+2\",\"Athletics\":\"+2\",\"Deception\":\"+2\",\"History\":\"+4\",\"Insight\":\"+2\",\"Intimidation\":\"+2\",\"Investigation\":\"+2\",\"Medicine\":\"+2\",\"Nature\":\"+2\",\"Perception\":\"+2\",\"Performance\":\"+2\",\"Persuasion\":\"+4\",\"Religion\":\"+2\",\"Sleight of Hand\":\"+2\",\"Stealth\":\"+2\",\"Survival\":\"+2\"},\"combat\":{\"AC\":\"12\",\"initiative\":\"+2\",\"speed\":\"30 ft\",\"hitPointMax\":\"12\",\"currentHitPoints\":\"0\",\"hitDice\":\"1d10\"}}";
        }

        Intent transfer = new Intent(this, RollerPageActivity.class);
        transfer.putExtra("sheetJson", data);
        startActivity(transfer);
        startedLoad = false;
    }

}