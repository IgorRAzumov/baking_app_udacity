package udacity.com.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipesViewHolder> {
    private List<Recipe> recipesList;
    private RecyclerViewCallback recyclerViewCallback;

    private final View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Recipe recipe = (Recipe) v.getTag();
            recyclerViewCallback.onRecipeClick(recipe);
        }
    };

    public RecipesListAdapter(List<Recipe> data, RecyclerViewCallback recyclerViewCallback) {
        recipesList = data;
        this.recyclerViewCallback = recyclerViewCallback;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipe recipe = recipesList.get(position);
        holder.recipeName.setText(recipe.getName());
        holder.itemView.setTag(recipe);
        holder.itemView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return recipesList == null ? 0 : recipesList.size();
    }

    public interface RecyclerViewCallback {
        void onRecipeClick(Recipe recipe);
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_card_recipe_title)
        TextView recipeName;

        RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
