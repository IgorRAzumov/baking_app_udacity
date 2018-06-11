package udacity.com.baking_app.data;

import java.util.ArrayList;
import java.util.List;

public class DummyDataCreator {
    private static final int DUMMY_ITEMS_COUNT = 5;
    private static final String INGREDIENT_NAME = "Sugar";
    private static final String INGREDIENT_MEASURE = "Gram";
    private static final String STEP_SHORT_DESCRIPTION = "Recipe Introduction";
    private static final String STEP_DESCRIPTION = "Recipe Introduction";
    private static final String VIDEO_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
    private static final String RECIPE_NAME = "recipe name";
    private static final String VIDEO_THUMBNAIL = null;

    private DummyDataCreator() {
    }

    public static Recipe createDummyRecipe(int id) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < DUMMY_ITEMS_COUNT; i++) {
            ingredients.add(new Ingredient((float) i, INGREDIENT_MEASURE, INGREDIENT_NAME));
            steps.add(new Step(i, STEP_SHORT_DESCRIPTION, STEP_DESCRIPTION, VIDEO_URL, VIDEO_THUMBNAIL));
        }
        return new Recipe(id, RECIPE_NAME, ingredients, steps);
    }
}
