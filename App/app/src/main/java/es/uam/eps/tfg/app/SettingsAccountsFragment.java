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
import es.uam.eps.tfg.app.databinding.FragmentSettingsAccountsBinding;

public class SettingsAccountsFragment extends Fragment {

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentSettingsAccountsBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings_accounts,
                container,
                false
        );

        // Get list of accounts to set the adapter
        List<Account> list = appDAO.getAccounts();
        AccountAdapter adapter = new AccountAdapter(list);
        binding.accountListRecyclerView.setAdapter(adapter);

        // Go back to the settings screen
        binding.backToSettingsFab.setOnClickListener( view -> {
            BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
            menu.setVisibility(View.VISIBLE);

            Navigation.findNavController(view)
                    .navigate(SettingsAccountsFragmentDirections
                            .actionSettingsAccountsFragmentToSettingsFragment());
        });

        // Go to the add new account screen
        binding.addNewAccountButton.setOnClickListener(view -> {
            if(list.size() >= 20) {
                Snackbar.make(view, R.string.too_many_accounts, Snackbar.LENGTH_LONG).show();
            } else {
                Navigation.findNavController(view)
                        .navigate(SettingsAccountsFragmentDirections
                                .actionSettingsAccountsFragmentToSettingsNewAccountFragment());
            }
        });

        return binding.getRoot();
    }
}
