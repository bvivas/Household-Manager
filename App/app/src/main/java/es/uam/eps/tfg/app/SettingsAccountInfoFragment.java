package es.uam.eps.tfg.app;

import android.annotation.SuppressLint;
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
import es.uam.eps.tfg.app.databinding.FragmentSettingsAccountInfoBinding;


public class SettingsAccountInfoFragment extends Fragment {

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentSettingsAccountInfoBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings_account_info,
                container,
                false
        );

        // Retrieve the account ID from arguments
        long accountId = SettingsAccountInfoFragmentArgs.fromBundle(getArguments()).getAccountId();

        Account account = appDAO.getAccountById(accountId);
        if(account == null) {
            throw new NullPointerException("There are no accounts with the specified id: " + accountId);
        }

        // Display the subtitle of the section
        String accountName = account.getName() + " " + App.getContext().getResources().getString(R.string.account_info_text);
        binding.accountNameTextView.setText(accountName);

        // Display the current balance of the account
        @SuppressLint("DefaultLocale")
        String amount = String.format("%.2f", account.getAmount());
        String accountAmount = amount + " \u20ac";
        binding.accountAmountTextView.setText(accountAmount);

        // Delete account
        binding.deleteAccountButton.setOnClickListener(view -> {
            appDAO.deleteAccount(account);
            appDAO.deleteTransactionsMatchingAccount(account.getId());

            Navigation.findNavController(view)
                    .navigate(SettingsAccountInfoFragmentDirections
                            .actionSettingsAccountInfoFragmentToSettingsAccountsFragment());
        });

        // Go back to the accounts screen
        binding.backToAccountsFab.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(SettingsAccountInfoFragmentDirections
                                .actionSettingsAccountInfoFragmentToSettingsAccountsFragment())
        );

        return binding.getRoot();
    }
}
