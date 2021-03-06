package udacity.com.baking_app.widgets.ingredientsWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import udacity.com.baking_app.R;
import udacity.com.baking_app.activities.RecipesListActivity;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.utils.PreferencesUtil;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget);

        Recipe recipe = PreferencesUtil.getRecipe(context);
        if (recipe == null) {
            showNoSelectedRecipeMessage(views);
        } else {
            showIngredientsViews(context, views);
            views.setTextViewText(R.id.tv_recipe_ingredients_widget_recipe_name, recipe.getName());
        }

        setOnClickListener(context, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        UpdateIngredientsWidgetService.startActionUpdateIngredientsWidgets(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        UpdateIngredientsWidgetService.startActionUpdateIngredientsWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action != null && action.equalsIgnoreCase(
                context.getString(R.string.action_on_widget_list_item_click))) {
            Intent startActivityIntent = new Intent(context, RecipesListActivity.class);
            context.startActivity(startActivityIntent);
        }
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    private static void setOnClickListener(Context context, RemoteViews views) {
        Intent listClickIntent = new Intent(context, IngredientsWidgetProvider.class);
        listClickIntent.setAction(context.getString(R.string.action_on_widget_list_item_click));
        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,
                listClickIntent, 0);
        views.setPendingIntentTemplate(R.id.lv_recipe_ingredients_widget_list, listClickPIntent);

    }

    private static void showIngredientsViews(Context context, RemoteViews views) {
        views.setViewVisibility(R.id.lv_recipe_ingredients_widget_list, View.VISIBLE);
        views.setViewVisibility(R.id.tv_recipe_ingredients_widget_recipe_name, View.VISIBLE);
        views.setViewVisibility(R.id.tv_recipe_ingredients_widget_no_ingredients_text, View.GONE);

        Intent adapterIntent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.lv_recipe_ingredients_widget_list, adapterIntent);
    }

    private static void showNoSelectedRecipeMessage(RemoteViews views) {
        views.setViewVisibility(R.id.lv_recipe_ingredients_widget_list, View.GONE);
        views.setViewVisibility(R.id.tv_recipe_ingredients_widget_recipe_name, View.GONE);
        views.setViewVisibility(R.id.tv_recipe_ingredients_widget_no_ingredients_text, View.VISIBLE);
    }
}

