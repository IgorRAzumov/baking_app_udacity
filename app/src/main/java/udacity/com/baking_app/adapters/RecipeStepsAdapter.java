package udacity.com.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.data.Step;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private static final int INGREDIENTS_POSITION = 0;
    private final Recipe recipe;
    private final RecyclerViewCallback recyclerViewCallback;

    private final View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            switch (position) {
                case INGREDIENTS_POSITION: {
                    recyclerViewCallback.onIngredientsClick(recipe);
                    break;
                }
                default: {
                    recyclerViewCallback.onStepClick(recipe, position);
                    break;
                }
            }

        }
    };

    public RecipeStepsAdapter(Recipe recipe, RecyclerViewCallback recyclerViewCallback) {
        this.recipe = recipe;
        this.recyclerViewCallback = recyclerViewCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        switch (position) {
            case INGREDIENTS_POSITION: {
                holder.titleTextView.setText(R.string.ingredients);
                holder.itemView.setTag(INGREDIENTS_POSITION);
                break;
            }
            default: {
                Step step = recipe.getSteps().get(position - 1);
                holder.titleTextView.setText(step.getShortDescription());

                holder.itemView.setTag(position);
                holder.itemView.setOnClickListener(itemClickListener);
            }
        }

    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size() + 1;
    }

    public interface RecyclerViewCallback {
        void onStepClick(Recipe recipe, int selectedStepPosition);

        void onIngredientsClick(Recipe recipe);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_step_card_title)
        TextView titleTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

