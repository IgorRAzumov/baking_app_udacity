package udacity.com.baking_app.widgets.ingredientsWidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import udacity.com.baking_app.R;

public class UpdateIngredientsWidgetService extends IntentService {
    public static final String ACTION_UPDATE_INGREDIENTS_WIDGET
            = "com.udacity.baking_app.action.update_ingredients_widget";
    private static final String UPDATE_INGREDIENTS_WIDGET_SERVICE =
            " UpdateIngredientsWidgetService";

    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, UpdateIngredientsWidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGET);
        context.startService(intent);
    }

    public UpdateIngredientsWidgetService() {
        super(UPDATE_INGREDIENTS_WIDGET_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        final String action = intent.getAction();
        if (action != null && action.equals(ACTION_UPDATE_INGREDIENTS_WIDGET)) {
            handleActionUpdateIngredientsWidgets();
        }
    }

    private void handleActionUpdateIngredientsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                R.id.lv_recipe_ingredients_widget_list);

        IngredientsWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds);


    }

}
