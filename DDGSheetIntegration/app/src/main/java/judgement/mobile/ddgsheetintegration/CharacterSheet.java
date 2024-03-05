package judgement.mobile.ddgsheetintegration;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CharacterSheet{
    Header header;
    AbilityCollection abilityScores;
    AbilityCollection abilityModifiers;
    int proficiencyBonus;
    AbilityCollection savingThrows;
    SkillCollection skills;
    CombatCollection combat;
    class Header {
        String characterName;
        String classes;
        String race;
        String playerName;
        int level;
    }
    class AbilityCollection {
        int str, dex, con, _int, wis, cha;
    }
    class SkillCollection {
        int Acrobatics,
            Animal_Handling,
            Arcana,
            Athletics,
            Deception,
            History,
            Insight,
            Intimidation,
            Investigation,
            Medicine,
            Nature,
            Perception,
            Performance,
            Persuasion,
            Religion,
            Sleight_of_Hand,
            Stealth,
            Survival;
    }
    class CombatCollection {
        int AC;
        int initiative;
        String speed;
        int hitPointMax;
        int currentHitPoints;
        String condition;
        String hitDice;

    }

    public int getModifier(String score, boolean isCheck) {
        AbilityCollection source = isCheck ? abilityModifiers : savingThrows;
        switch (score) {
            case "str":
                return source.str;
            case "dex":
                return source.dex;

            case "con":
                return source.con;

            case "int":
                return source._int;

            case "wis":
                return source.wis;

            case "cha":
                return source.cha;

        }
        return 0;
    }
    public int getSkill(String skill) {
        switch(skill) {
            case "Acrobatics": return skills.Acrobatics;
            case "Animal_Handling": return skills.Animal_Handling;
            case "Arcana": return skills.Arcana;
            case "Athletics": return skills.Athletics;
            case "Deception": return skills.Deception;
            case "History": return skills.History;
            case "Insight": return skills.Insight;
            case "Intimidation": return skills.Intimidation;
            case "Investigation": return skills.Investigation;
            case "Medicine": return skills.Medicine;
            case "Nature": return skills.Nature;
            case "Perception": return skills.Perception;
            case "Performance": return skills.Performance;
            case "Persuasion": return skills.Persuasion;
            case "Religion": return skills.Religion;
            case "Sleight_of_Hand": return skills.Sleight_of_Hand;
            case "Stealth": return skills.Stealth;
            case "Survival": return skills.Survival;
        }
        return 0;
    }
    public static CharacterSheet parseJson(String response) {
        Gson gson = new GsonBuilder().create();
        Log.i("CharacterSheet Json parse", response);
        CharacterSheet characterSheetResponse = gson.fromJson(response, CharacterSheet.class);
        return characterSheetResponse;
    }

//    class CharacterSheetAdapter implements Jso
}
