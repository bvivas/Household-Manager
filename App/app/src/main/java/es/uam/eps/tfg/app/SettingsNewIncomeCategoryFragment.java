package es.uam.eps.tfg.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;
import es.uam.eps.tfg.app.databinding.FragmentSettingsNewIncomeCategoryBinding;

public class SettingsNewIncomeCategoryFragment extends Fragment {
    private FragmentSettingsNewIncomeCategoryBinding binding;

    private String name;

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings_new_income_category,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Update the name text
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        binding.editNewIncomeCategoryName.addTextChangedListener(textWatcher);

        // Add income category
        binding.addIncomeCategoryButton.setOnClickListener(view -> {
            List<Category> categories = appDAO.getCategoriesByType(CategoryType.INCOME);

            if(name == null || TextUtils.isEmpty(name.trim())) {
                binding.newIncomeCategoryName.setTextColor(
                        App.getContext().getResources().getColor(R.color.red));
                Snackbar.make(view, R.string.name_error, Snackbar.LENGTH_LONG).show();
            } else {
                boolean repeatedName = false;
                name = Category.formatCategoryName(name);

                // Check is there is already an expense category with the provided name
                for(Category c : categories) {
                    if(name.equals(c.getName())) {
                        repeatedName = true;
                        break;
                    }
                }
                if(repeatedName) {
                    binding.newIncomeCategoryName.setTextColor(
                            App.getContext().getResources().getColor(R.color.red));
                    Snackbar.make(view, R.string.repeated_name_category_error, Snackbar.LENGTH_LONG).show();
                } else {
                    Category cat = new Category(name, CategoryType.INCOME);
                    appDAO.addCategory(cat);

                    Navigation.findNavController(view)
                            .navigate(SettingsNewIncomeCategoryFragmentDirections
                                    .actionSettingsNewIncomeCategoryFragmentToSettingsIncomeCategoriesFragment());
                }
            }
        });

        // Go back to categories
        binding.backToCategoriesFab.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(SettingsNewIncomeCategoryFragmentDirections
                                .actionSettingsNewIncomeCategoryFragmentToSettingsIncomeCategoriesFragment())
        );
    }
}
