package judgement.mobile.ddgsheetintegration;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class RollerPageActivity extends AppCompatActivity {
    final String TAG = RollerPageActivity.class.getSimpleName();
    CharacterSheet referenceSheet;
    MaterialButton[] checkSaveScores;
    String[] shortScores = new String[] {"str", "dex", "con", "int", "wis", "cha"};
    String[] skillNames = new String[] {
            "Acrobatics",
            "Animal_Handling",
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
            "Sleight_of_Hand",
            "Stealth",
            "Survival"
    };
    HashMap<String, Integer> skillToScoreMap = new HashMap<String, Integer>() {{
        put("Acrobatics", 1);
        put("Animal_Handling", 4);
        put("Arcana", 3);
        put("Athletics", 0);
        put("Deception", 5);
        put("History",3);
        put("Insight", 4);
        put("Intimidation", 5);
        put("Investigation", 3);
        put("Medicine", 4);
        put("Nature", 3);
        put("Perception", 4);
        put("Performance", 5);
        put("Persuasion", 5);
        put("Religion", 3);
        put("Sleight_of_Hand", 1);
        put("Stealth", 1);
        put("Survival", 4);
    }};
    MaterialButton[] checkSaveSelector;
    FloatingActionButton rollButton;
    MaterialButton[] skillButtons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_sheet_alt);

        referenceSheet = CharacterSheet.parseJson(getIntent().getStringExtra("sheetJson"));

        findViewById(R.id.roll_button).setOnClickListener((view -> displayResult(null)));
        checkSaveScores = new MaterialButton[6];
        checkSaveScores[0] = findViewById(R.id.strMod);
        checkSaveScores[1] = findViewById(R.id.dexMod);
        checkSaveScores[2] = findViewById(R.id.conMod);
        checkSaveScores[3] = findViewById(R.id.intMod);
        checkSaveScores[4] = findViewById(R.id.wisMod);
        checkSaveScores[5] = findViewById(R.id.chaMod);
        checkSaveSelector = new MaterialButton[2];
        checkSaveSelector[0] = findViewById(R.id.abilityCheckToggle);
        checkSaveSelector[1] = findViewById(R.id.savingThrowToggle);

        checkSaveScores[0].setChecked(true);
        checkSaveSelector[0].setChecked(true);

        for(MaterialButton i : checkSaveScores) {
            i.setOnClickListener(new ToggleRadioGroupClickListener(checkSaveScores));
        }

        checkSaveSelector[0].setOnClickListener(new ToggleRadioGroupClickListener(checkSaveSelector) {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                for (int i = 0; i < 6; i++) {
                    formatAbilitySaveButton(checkSaveScores[i], shortScores[i], referenceSheet.getModifier(shortScores[i], true), referenceSheet.getModifier(shortScores[i], false), true);
                }
            }
        });
        checkSaveSelector[1].setOnClickListener(new ToggleRadioGroupClickListener(checkSaveSelector) {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                    for (int i = 0; i < 6; i++) {
                        formatAbilitySaveButton(checkSaveScores[i], shortScores[i], referenceSheet.getModifier(shortScores[i], true), referenceSheet.getModifier(shortScores[i], false), false);
                    }
                }
        });
        checkSaveSelector[0].performClick();
        rollButton = findViewById(R.id.roll_button);
        rollButton.setOnClickListener(view -> roll());

        skillButtons = new MaterialButton[18];
        for (int i = 0; i < 18; i++) {
            skillButtons[i] = findViewById(getResources().getIdentifier(skillNames[i],"id", getPackageName() ));
            formatSkillButton(skillButtons[i], skillNames[i],referenceSheet.getSkill(skillNames[i]) );
        }
        for(int i = 0; i < skillButtons.length; i++) {
            skillButtons[i].setOnClickListener(new SkillListener(skillButtons, i));
        }
    }
    public void roll() {
        int selectedScore = -1;
        for (int i = 0; i < 6; i ++) {
            if(checkSaveScores[i].isChecked()) {
                selectedScore = i;
            }
        }
        boolean isCheck = checkSaveSelector[0].isChecked();
        displayResult(new RollResult(referenceSheet.getModifier(shortScores[selectedScore], isCheck ), 0));
    }
    public void displayResult(RollResult roll) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.roll_button), roll.getExpression(), Snackbar.LENGTH_LONG);
//        View view = snackbar.getView();
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        view.setLayoutParams(params);
        snackbar.setAnchorView(rollButton);
        snackbar.show();
//        Toast toast = Toast.makeText(this, "aaaa", );

    }
    public void formatAbilitySaveButton(MaterialButton button, String scoreName, int check, int save, boolean isCheck) {
        String formattedString = "";
        if (isCheck) {
            formattedString = "<big><b>"+ (check < 0 ? "" : "+") + check+"</b></big>/"+(save < 0 ? "" : "+") +save+"<br/><small>"+scoreName+"</small>";
        } else {
            formattedString = ""+(check < 0 ? "" : "+") +check+"/<big><b>"+(save < 0 ? "" : "+") +save+"</b></big><br/><small>"+scoreName+"</small>";
        }
        button.setText(Html.fromHtml(formattedString, Html.FROM_HTML_MODE_LEGACY));
    }
    public void formatSkillButton(MaterialButton button, String skillName, int mod) {
        String formattedString = "<big><b>" + (mod < 0 ? "" : "+") + mod + "</b></big>   " + skillName;
        button.setText(Html.fromHtml(formattedString, Html.FROM_HTML_MODE_LEGACY));
    }
    class ToggleRadioGroupClickListener implements View.OnClickListener {
        MaterialButton[] buttons;
        public ToggleRadioGroupClickListener(@NonNull MaterialButton[] linkedButtons) {
            buttons = linkedButtons;

        }

        public ToggleRadioGroupClickListener(ViewGroup... groups) {
            int temp = 0;
            for(ViewGroup i : groups) {
                temp += i.getChildCount();
            }
            buttons = new MaterialButton[temp];
            temp = 0;
            for(ViewGroup i: groups) {
                for (int j = 0; j < i.getChildCount(); j++) {
                    if (i.getChildAt(j) instanceof MaterialButton)
                        buttons[temp++] = (MaterialButton) i.getChildAt(j);
                }
            }

        }
        @Override
        public void onClick(View view) {
            for (MaterialButton i : buttons) {
                i.setChecked(false);
            }
            ((MaterialButton)view).setChecked(true);
        }
    }
    class SkillListener extends ToggleRadioGroupClickListener {
        int relatedScore;
        public SkillListener(MaterialButton[] m, int skill) {
            super(m);
            relatedScore = skill;
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            checkSaveSelector[0].performClick();
            checkSaveScores[skillToScoreMap.get(skillNames[relatedScore])].performClick();
        }
    }
}
