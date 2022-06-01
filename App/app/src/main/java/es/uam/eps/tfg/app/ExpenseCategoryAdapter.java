package es.uam.eps.tfg.app;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uam.eps.tfg.app.databinding.ListItemExpenseCategoryBinding;


public class ExpenseCategoryAdapter extends RecyclerView.Adapter<ExpenseCategoryAdapter.ExpenseCategoryHolder> {

    private List<Category> list;

    public ExpenseCategoryAdapter(List<Category> list) {
        this.list = list;
    }

    public class ExpenseCategoryHolder extends RecyclerView.ViewHolder {
        private ListItemExpenseCategoryBinding binding;

        public ExpenseCategoryHolder(ListItemExpenseCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category) {
            binding.setCategory(category);
            binding.executePendingBindings();

            binding.expenseCategoryWidget.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(SettingsExpenseCategoriesFragmentDirections
                                .actionSettingsExpenseCategoriesFragmentToSettingsDeleteCategoryFragment(category.getId()));
            });
        }
    }

    @NonNull
    @Override
    public ExpenseCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemExpenseCategoryBinding itemBinding = ListItemExpenseCategoryBinding.inflate(
                inflater,
                parent,
                false);
        return new ExpenseCategoryHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseCategoryHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
