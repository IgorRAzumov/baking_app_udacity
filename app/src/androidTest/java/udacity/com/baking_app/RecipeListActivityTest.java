package udacity.com.baking_app;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import udacity.com.baking_app.activities.RecipeActivity;
import udacity.com.baking_app.activities.RecipesListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private static final String RECIPE_ITEM_BROWNIE = "Brownies";

    @Rule
    public IntentsTestRule<RecipesListActivity> activityRule = new IntentsTestRule<>(
            RecipesListActivity.class);
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResources() {
        idlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void toolbarTest() {
        onView(withId(R.id.tb_toolbar)).check(matches(isDisplayed()));
        onView(withText(activityRule.getActivity().getString(R.string.baking_time)))
                .check(matches(withParent(withId(R.id.tb_toolbar))));
    }



    @Test
    public void clickRecipe_LaunchDetailActivity() {
        onView(withId(R.id.rv_fragment_recipes_list))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeActivity.class.getName()));
        intended(hasExtraWithKey(
                activityRule.getActivity().getResources().getString(R.string.recipe_key)));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


}
