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
import udacity.com.baking_app.data.Step;

public class RecipeContentAdapter extends RecyclerView.Adapter<RecipeContentAdapter.ViewHolder> {
    private static final int INGREDIENTS_POSITION = 0;
    private static final int STEP_POSITION_OFFSET = 1;

    private int selectedPosition;
    private final Recipe recipe;
    private final RecyclerViewCallback recyclerViewCallback;
    private final View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeSelectedItem((int) view.getTag());
            recyclerViewCallback.onRecipeDetailItemClick(selectedPosition);
        }
    };

    public RecipeContentAdapter(int contentPosition, Recipe recipe,
                                RecyclerViewCallback recyclerViewCallback) {
        selectedPosition = contentPosition;
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
                break;
            }
            default: {
                Step step = recipe.getSteps().get(position - STEP_POSITION_OFFSET);
                holder.titleTextView.setText(step.getShortDescription());
            }
        }
        holder.itemView.setTag(position);
        holder.itemView.setSelected((position == selectedPosition));
        holder.itemView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        int count;
        List<Step> steps = recipe.getSteps();
        if (steps == null) {
            count = 0;
        } else {
            count = steps.size() + STEP_POSITION_OFFSET;
        }

        return count;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /*public void setSelectedPosition(int newSelectedPosition) {
        changeSelectedItem(newSelectedPosition);
    }*/

    public interface RecyclerViewCallback {
        void onRecipeDetailItemClick(int selectedStepPosition);
    }

   public void changeSelectedItem(int newSelectedPosition) {
        int lastSelected = selectedPosition;
        selectedPosition = newSelectedPosition;
        if (lastSelected != selectedPosition) {
            notifyItemChanged(lastSelected);
            notifyItemChanged(selectedPosition);

        }
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

