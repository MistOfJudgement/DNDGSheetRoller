package judgement.mobile.ddgsheetintegration;

import java.util.Random;

public class RollResult {
    int modifier;
    int roll1;
    int roll2;
    int advDis;
    static Random rng= new Random();
    public RollResult(int modifier, int hasAdvDis) {
        this.modifier = modifier;
        advDis = hasAdvDis;
        roll1 = rng.nextInt(20) + 1;
        roll2 = rng.nextInt(20) + 1;
    }

    public int getResult() {
        switch (advDis) {
            case 0:
                return roll1 + modifier;
            case 1:
                return Math.max(roll1, roll2) + modifier;
            case -1:
                return Math.min(roll1, roll2) + modifier;
            default: return 0;
        }
    }

    public String getExpression() {
        if (advDis == 0) {
            return ("" + roll1 + (modifier< 0 ? " " : " +") + modifier + " = " + getResult());
        }

        return "";
    }

}
