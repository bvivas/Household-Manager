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

import es.uam.eps.tfg.app.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings,
                container,
                false
        );

        // Go to accounts
        binding.accountsCard.setOnClickListener(view -> {
            BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
            menu.setVisibility(View.VISIBLE);

            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToSettingsAccountsFragment());
        });

        // Go to expense categories
        binding.expenseCategoriesCard.setOnClickListener(view -> {
            BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
            menu.setVisibility(View.VISIBLE);

            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToSettingsExpenseCategoriesFragment());
        });

        // Go to income categories
        binding.incomeCategoriesCard.setOnClickListener(view -> {
            BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
            menu.setVisibility(View.VISIBLE);

            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToSettingsIncomeCategoriesFragment());
        });

        return binding.getRoot();
    }
}
