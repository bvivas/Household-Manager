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
import es.uam.eps.tfg.app.databinding.FragmentSettingsNewAccountBinding;


public class SettingsNewAccountFragment extends Fragment {
    private FragmentSettingsNewAccountBinding binding;

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
                R.layout.fragment_settings_new_account,
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

        binding.editNewAccountName.addTextChangedListener(textWatcher);

        // Handle the add new account button
        binding.addAccountButton.setOnClickListener(view -> {
            List<Account> accounts = appDAO.getAccounts();

            String inputAmount = binding.editNewAccountInitialAmount.getText().toString();
            if(name == null || TextUtils.isEmpty(name.trim())) {
                binding.newAccountName.setTextColor(
                        App.getContext().getResources().getColor(R.color.red));
                Snackbar.make(view, R.string.name_error, Snackbar.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(inputAmount)) {
                binding.newAccountInitialAmount.setTextColor(
                        App.getContext().getResources().getColor(R.color.red));
                Snackbar.make(view, R.string.amount_error, Snackbar.LENGTH_LONG).show();
            } else {
                double amount = Double.parseDouble(binding.editNewAccountInitialAmount.getText().toString());
                boolean repeatedName = false;
                name = Account.formatAccountName(name);

                // Check if there is already an account with the same name
                for(Account a : accounts) {
                    if(name.equals(a.getName())) {
                        repeatedName = true;
                        break;
                    }
                }
                if(repeatedName) {
                    binding.newAccountName.setTextColor(
                            App.getContext().getResources().getColor(R.color.red));
                    Snackbar.make(view, R.string.repeated_name_account_error, Snackbar.LENGTH_LONG).show();
                } else {
                    Account acc = new Account(name, amount);
                    appDAO.addAccount(acc);

                    Navigation.findNavController(view)
                            .navigate(SettingsNewAccountFragmentDirections
                                    .actionSettingsNewAccountFragmentToSettingsAccountsFragment());
                }
            }
        });

        // Go back to the accounts screen
        binding.backToAccountsFab.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(SettingsNewAccountFragmentDirections
                                .actionSettingsNewAccountFragmentToSettingsAccountsFragment())
        );
    }
}
