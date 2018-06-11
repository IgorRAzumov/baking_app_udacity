package udacity.com.baking_app;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import udacity.com.baking_app.actions.OrientationChangeAction;
import udacity.com.baking_app.activities.RecipeActivity;
import udacity.com.baking_app.data.DummyDataCreator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private static final String RECIPE_EXTRA_KEY = "recipe-key";
    private static final int TWO_PANE_WIDTH = 900;
    @Rule
    public IntentsTestRule<RecipeActivity> activityRule = new IntentsTestRule<RecipeActivity>(
            RecipeActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra(RECIPE_EXTRA_KEY, DummyDataCreator.createDummyRecipe(0));
            return intent;
        }
    };

    @Test
    public void toolbarTest() {
        onView(withId(R.id.tb_toolbar)).check(matches(isDisplayed()));
        onView(withText(activityRule.getActivity().getString(R.string.back_to_recipes)))
                .check(matches(withParent(withId(R.id.tb_toolbar))));
    }

    @Test
    public void checkContentRecycler() {
        onView(withId(R.id.rv_fragment_recipe_content))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void fragmentVisibilityTest() throws InterruptedException {
        onView(withId(R.id.fl_activity_recipe_main_container)).check(matches(isDisplayed()));
        int width =getScreenWidth();
        if ((width < TWO_PANE_WIDTH)) {
            onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
            width = getScreenWidth();
            if(width >= 900){
                onView(withId(R.id.fl_activity_recipe_detail_container)).check(matches(isDisplayed()));
            }
        } else {
            onView(withId(R.id.fl_activity_recipe_detail_container)).check(matches(isDisplayed()));
            onView(isRoot()).perform(OrientationChangeAction.orientationPortrait());
            onView(withId(R.id.fl_activity_recipe_main_container)).check(matches(isDisplayed()));
        }


    }

    private int getScreenWidth() {
        return activityRule.getActivity().getResources().getConfiguration().screenWidthDp;
    }

}
