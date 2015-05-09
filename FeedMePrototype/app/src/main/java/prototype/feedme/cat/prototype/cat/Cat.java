package prototype.feedme.cat.prototype.cat;

import android.content.SharedPreferences;

import prototype.feedme.cat.prototype.activity.CatActivity;

/**
 * Cat's Attribute
 * Created by AT on 2014/12/28.
 */
public class Cat {
    public  static final int CAT_DEAD = -1;
    public static final int CAT_ILL = 0;
    public static final int CAT_NOR = 1;
    public static final int CAT_WEL = 2;

    private static int DIETTYPE;
    private static int STATUS = 1;
    private static int DAYS;
    private static float HEALTH = 0;
    private static float VEG;
    private static float PRO;
    private static float STA;
    private static float OIL;
    private static float DAI;
    private static String BORN;

    public static void setStatus(int status) {
        if(status < -1 || status > 2)
            throw new IllegalArgumentException();
        STATUS = status;
    }

    public static int getStatus() {return STATUS;}

    public static void setDays(int days) {
        if(days < 0)
            throw new IllegalArgumentException();
        DAYS = days;
    }
    public static Integer getDays() {return DAYS;}

    public static float getHealth () {return HEALTH;}

    public static void feed (float[] food) {
        if(food.length < 6)
            throw new IllegalArgumentException();
        setDiet(VEG + food[0], PRO + food[1], STA + food[2], OIL + food[3], DAI + food[4]);
        setHealth(food[5] + HEALTH);
    }
    public static String getBorn() { return BORN;}
    public static void setBorn(String born) {BORN = born;}


    public static float getVEG () {return VEG;}
    public static float getPRO () {return PRO;}
    public static float getSTA () {return STA;}
    public static float getOIL () {return OIL;}
    public static float getDAI () {return DAI;}
    public static void setDiet(float veg, float pro, float sta, float oil, float dai) {
        VEG = veg;
        PRO = pro;
        STA = sta;
        OIL = oil;
        DAI = dai;
        System.out.println("Set Diet statrt");
        System.out.println(VEG + " " + PRO + " " + STA + " " +OIL + " " + DAI);
    }

    public static void setHealth (float health) {
        int result = CAT_NOR;
        System.out.println(health);
        if(health >= 300) {
            HEALTH = 300;
            result = CAT_WEL;
        }
        else if(health <= 0) {
            HEALTH = 0;
            result = CAT_DEAD;
            setDiet(0, 0, 0, 0, 0);
        }
        else {
            HEALTH = health;
            if (HEALTH >= 75 && HEALTH <= 150) result = CAT_NOR;
            else if (HEALTH > 150) result = CAT_WEL;
            else result = CAT_ILL;
        }
        setStatus(result);
    }


}
