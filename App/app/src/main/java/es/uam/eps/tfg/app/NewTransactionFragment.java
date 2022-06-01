package es.uam.eps.tfg.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;
import es.uam.eps.tfg.app.databinding.FragmentNewTransactionBinding;


public class NewTransactionFragment extends Fragment {
    private FragmentNewTransactionBinding binding;

    private String note;
    String expenseText = App.getContext().getResources().getString(R.string.type_expense_text);
    String incomeText = App.getContext().getResources().getString(R.string.type_income_text);

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
                R.layout.fragment_new_transaction,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Clear inputs for new transactions
        binding.editAmountTransactionText.setText(null);
        binding.editAccountTransaction.setAdapter(null);
        binding.editCategoryTransaction.setAdapter(null);
        binding.radioButtonsGroup.clearCheck();
        binding.transactionNoteEdit.setText(null);

        // Update the note text
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                note = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        binding.transactionNoteEdit.addTextChangedListener(textWatcher);

        // Set account spinner
        List<Account> accounts = appDAO.getAccounts();
        setAccountSpinner(accounts);

        // Handle checked radio buttons
        binding.radioButtonsGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            int radioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(radioButtonId);
            String selectedText = radioButton.getText().toString();

            if(selectedText.equals(expenseText)) {
                // Expense radio button: set category spinner to display expense categories
                setCategorySpinner(CategoryType.EXPENSE);
            } else if(selectedText.equals(incomeText)) {
                // Income radio button: set category spinner to display income categories
                setCategorySpinner(CategoryType.INCOME);
            }
        });

        // Handle add transaction button
        binding.addTransactionButton.setOnClickListener(view -> {
            String inputAmount = binding.editAmountTransactionText.getText().toString();

            // Check the amount field is not empty
            if(TextUtils.isEmpty(inputAmount)) {
                binding.transactionAmountText.setTextColor(
                        App.getContext().getResources().getColor(R.color.red));
                Snackbar.make(view, R.string.amount_error, Snackbar.LENGTH_LONG).show();
            // Check a category has been selected
            } else if(binding.editCategoryTransaction.getSelectedItem() == null) {
                binding.transactionCategoryText.setTextColor(
                        App.getContext().getResources().getColor(R.color.red));
                Snackbar.make(view, R.string.category_error, Snackbar.LENGTH_LONG).show();
            } else {
                double amount = Double.parseDouble(inputAmount);

                // Category updating
                Spinner categorySpinner = binding.editCategoryTransaction;
                String categoryText = categorySpinner.getSelectedItem().toString();
                List<Category> categories = new ArrayList<>();
                Category cat = null;

                // Get final checked value of radio buttons
                int radioButtonId = binding.radioButtonsGroup.getCheckedRadioButtonId();
                RadioButton radioButton = binding.radioButtonsGroup.findViewById(radioButtonId);
                String selectedText = radioButton.getText().toString();

                if(selectedText.equals(expenseText)) {
                    categories = appDAO.getCategoriesByType(CategoryType.EXPENSE);
                }else if(selectedText.equals(incomeText)) {
                    categories = appDAO.getCategoriesByType(CategoryType.INCOME);
                }

                for(Category c : categories) {
                    if(c.getName().equals(categoryText)) {
                        cat = c;
                    }
                }
                if(cat == null) {
                    throw new NullPointerException("Category not found");
                }

                // Account updating
                Spinner accountSpinner = binding.editAccountTransaction;
                String accountText = accountSpinner.getSelectedItem().toString();
                Account acc = null;

                for(Account a : accounts) {
                    if(a.getName().equals(accountText)) {
                        acc = a;
                    }
                }
                if(acc == null) {
                    throw new NullPointerException("Account not found");
                }

                double updatedAmount = 0.0;

                if(cat.getType() == CategoryType.EXPENSE) {
                    // Expense transaction
                    updatedAmount = acc.getAmount() - amount;
                } else if(cat.getType() == CategoryType.INCOME) {
                    // Income transaction
                    updatedAmount = acc.getAmount() + amount;
                }

                // If the balance of the account is negative the transaction cannot be made
                if(updatedAmount < 0) {
                    binding.transactionAmountText.setTextColor(
                            App.getContext().getResources().getColor(R.color.red));
                    Snackbar.make(view, R.string.account_error, Snackbar.LENGTH_LONG).show();
                } else {
                    acc.setAmount(updatedAmount);
                    appDAO.updateAccount(acc);

                    // Create new transaction
                    Transaction t = new Transaction(amount, acc, cat.getType(), cat, note);
                    appDAO.addTransaction(t);

                    Snackbar.make(view, R.string.transaction_added, Snackbar.LENGTH_LONG).show();

                    Navigation.findNavController(view)
                            .navigate(NewTransactionFragmentDirections
                                    .actionNewTransactionFragmentSelf());
                }
            }
        });
    }

    void setAccountSpinner(List<Account> accounts) {
        List<String> accountNames = new ArrayList<>();
        for(Account a : accounts) {
            accountNames.add(a.getName());
        }
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(
                App.getContext(),
                android.R.layout.simple_spinner_item,
                accountNames
        );
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editAccountTransaction.setAdapter(accountAdapter);
    }

    void setCategorySpinner(CategoryType type) {
        List<Category> categories = appDAO.getCategoriesByType(type);
        List<String> categoryNames = new ArrayList<>();
        for(Category c : categories) {
            categoryNames.add(c.getName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                App.getContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editCategoryTransaction.setAdapter(categoryAdapter);
    }
}
