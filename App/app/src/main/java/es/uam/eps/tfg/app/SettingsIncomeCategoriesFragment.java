package es.uam.eps.tfg.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;
import es.uam.eps.tfg.app.databinding.FragmentSettingsIncomeCategoriesBinding;

public class SettingsIncomeCategoriesFragment extends Fragment {

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentSettingsIncomeCategoriesBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings_income_categories,
                container,
                false
        );

        // Get income categories list to set the adapter
        List<Category> list = appDAO.getCategoriesByType(CategoryType.INCOME);
        IncomeCategoryAdapter adapter = new IncomeCategoryAdapter(list);
        binding.incomeCategoryListRecyclerView.setAdapter(adapter);

        // Go to the add new category screen
        binding.addNewCategoryButton.setOnClickListener(view -> {
            if(list.size() >= 30) {
                Snackbar.make(view, R.string.too_many_categories, Snackbar.LENGTH_LONG).show();
            } else {
                Navigation.findNavController(view)
                        .navigate(SettingsIncomeCategoriesFragmentDirections
                                .actionSettingsIncomeCategoriesFragmentToSettingsNewIncomeCategoryFragment());
            }
        });

        // Go back to settings
        binding.backToSettingsFab.setOnClickListener( view -> {
            BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
            menu.setVisibility(View.VISIBLE);
            Navigation.findNavController(view)
                    .navigate(SettingsIncomeCategoriesFragmentDirections
                            .actionSettingsIncomeCategoriesFragmentToSettingsFragment());
        });

        return binding.getRoot();
    }
}
