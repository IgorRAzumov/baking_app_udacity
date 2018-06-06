package udacity.com.baking_app.widgets.ingredientsWidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Ingredient;
import udacity.com.baking_app.utils.PreferencesUtil;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteFactory(this.getApplicationContext());
    }
}

class ListViewRemoteFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private final ArrayList<Ingredient> ingredientsList;

     ListViewRemoteFactory(Context applicationContext) {
        this.context = applicationContext;
        ingredientsList = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
       if (ingredientsList.size() != 0) {
            ingredientsList.clear();
        }

        List<Ingredient> ingredients = PreferencesUtil.getRecipe(context).getIngredients();
        ingredientsList.addAll(ingredients);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredientsList.get(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.ingredient_list_item);

        remoteViews.setTextViewText(R.id.tv_ingredients_item_ingredient, ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.tv_ingredients_item_quantity,
                String.valueOf(ingredient.getQuantity()));
        remoteViews.setTextViewText(R.id.tv_ingredients_item_measure, ingredient.getMeasure());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
