package es.uam.eps.tfg.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;
import es.uam.eps.tfg.app.databinding.FragmentSettingsDeleteCategoryBinding;


public class SettingsDeleteCategoryFragment extends Fragment {

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentSettingsDeleteCategoryBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings_delete_category,
                container,
                false
        );

        long categoryId = SettingsDeleteCategoryFragmentArgs.fromBundle(getArguments()).getCategoryId();

        Category category = appDAO.getCategoryById(categoryId);

        binding.categoryNameTextView.setText(category.getName() + " " + App.getContext().getResources().getString(R.string.delete_category_info_text));

        binding.deleteCategoryButton.setOnClickListener(view -> {
            appDAO.deleteCategory(category);
            appDAO.deleteTransactionsMatchingCategory(category.getId());

            if(category.getType() == CategoryType.EXPENSE) {
                Navigation.findNavController(view)
                        .navigate(SettingsDeleteCategoryFragmentDirections
                                .actionSettingsDeleteCategoryFragmentToSettingsExpenseCategoriesFragment());
            } else if(category.getType() == CategoryType.INCOME) {
                Navigation.findNavController(view)
                        .navigate(SettingsDeleteCategoryFragmentDirections
                                .actionSettingsDeleteCategoryFragmentToSettingsIncomeCategoriesFragment());
            }

        });

        binding.backToSettingsCategoryFab.setOnClickListener(view -> {
            if(category.getType() == CategoryType.EXPENSE) {
                Navigation.findNavController(view)
                        .navigate(SettingsDeleteCategoryFragmentDirections
                                .actionSettingsDeleteCategoryFragmentToSettingsExpenseCategoriesFragment());
            } else if(category.getType() == CategoryType.INCOME) {
                Navigation.findNavController(view)
                        .navigate(SettingsDeleteCategoryFragmentDirections
                                .actionSettingsDeleteCategoryFragmentToSettingsIncomeCategoriesFragment());
            }
        });

        return binding.getRoot();
    }
}
