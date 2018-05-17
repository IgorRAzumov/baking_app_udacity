package udacity.com.baking_app.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import udacity.com.baking_app.data.Recipe;

public interface BakingApi {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> loadRecipes();
}
