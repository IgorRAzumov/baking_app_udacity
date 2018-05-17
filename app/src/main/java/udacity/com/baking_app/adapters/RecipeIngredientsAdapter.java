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
import udacity.com.baking_app.data.Ingredient;

public class RecipeIngredientsAdapter
        extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {
    private final List<Ingredient> ingredientList;

    public RecipeIngredientsAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public RecipeIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.ingredient_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientText.setText(ingredient.getIngredient());
        holder.measureText.setText(ingredient.getMeasure());
        holder.quantityText.setText(String.valueOf(ingredient.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return ingredientList == null ? 0 : ingredientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredients_card_ingredient)
        TextView ingredientText;
        @BindView(R.id.tv_ingredients_card_measure)
        TextView measureText;
        @BindView(R.id.tv_ingredients_card_quantity)
        TextView quantityText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
