package es.uam.eps.tfg.app;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uam.eps.tfg.app.databinding.ListItemIncomeCategoryBinding;


public class IncomeCategoryAdapter extends RecyclerView.Adapter<IncomeCategoryAdapter.IncomeCategoryHolder> {

    private List<Category> list;

    public IncomeCategoryAdapter(List<Category> list) {
        this.list = list;
    }

    public class IncomeCategoryHolder extends RecyclerView.ViewHolder {
        private ListItemIncomeCategoryBinding binding;

        public IncomeCategoryHolder(ListItemIncomeCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category) {
            binding.setCategory(category);
            binding.executePendingBindings();

            binding.incomeCategoryWidget.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(SettingsIncomeCategoriesFragmentDirections
                                .actionSettingsIncomeCategoriesFragmentToSettingsDeleteCategoryFragment(category.getId()));
            });
        }
    }

    @NonNull
    @Override
    public IncomeCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemIncomeCategoryBinding itemBinding = ListItemIncomeCategoryBinding.inflate(
                inflater,
                parent,
                false);
        return new IncomeCategoryHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeCategoryHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
