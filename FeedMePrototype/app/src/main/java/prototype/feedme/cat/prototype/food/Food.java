package prototype.feedme.cat.prototype.food;

/**
 * Created by AT on 2015/1/1.
 */
public class Food {
    private float vegetable;
    private float protein;
    private float starch;
    private float oil;
    private float dairy;
    private float score;
    private String name;

    public Food(String name, float veg, float pro, float sta, float oil, float dai) {
        this.name = name;
        this.vegetable = veg;
        this.protein = pro;
        this.starch = sta;
        this.oil = oil;
        this.dairy = dai;
        score = veg + pro + sta + oil + dai;
    }

    public float[] getIngredient () {
        float[] ing = new float[7];
        ing[0] = vegetable;
        ing[1] = protein;
        ing[2] = starch;
        ing[3] = oil;
        ing[4] = dairy;
        ing[5] = score;
        return ing;
    }

    public String getName () {
        return name;
    }
 }
