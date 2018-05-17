package udacity.com.baking_app.asyncTasksLoaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.retrofit.RetrofitHelper;

public class RecipesLoader extends AsyncTaskLoader<List<Recipe>> {
    private List<Recipe> data;

    public RecipesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Recipe> loadInBackground() {
        List<Recipe> recipes = null;
        try {
            Response<List<Recipe>> response = RetrofitHelper
                    .getRetrofitHelper()
                    .getBakingApiService()
                    .loadRecipes()
                    .execute();
            if (response.isSuccessful()) {
                recipes = response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        if (this.data == null) {
            this.data = data;
        }
        super.deliverResult(this.data);
    }
}
