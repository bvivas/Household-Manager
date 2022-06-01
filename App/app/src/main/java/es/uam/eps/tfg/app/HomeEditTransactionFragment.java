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
import es.uam.eps.tfg.app.databinding.FragmentHomeEditTransactionBinding;


public class HomeEditTransactionFragment extends Fragment {

    FragmentHomeEditTransactionBinding binding;

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
                R.layout.fragment_home_edit_transaction,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();


        // Retrieve transaction ID from arguments
        long transactionId = HomeEditTransactionFragmentArgs.fromBundle(getArguments()).getTransactionId();

        // Get transaction from ID
        Transaction t = appDAO.getTransactionById(transactionId);
        if(t == null) {
            throw new NullPointerException("There are no transactions with the specified id: " + transactionId);
        }

        // Starting inputs for the selected transaction
        binding.editAmountTransactionText.setText(String.valueOf(t.getAmount()));
        binding.transactionNoteEdit.setHint(t.getNote());
        if(t.getType() == CategoryType.EXPENSE) {
            binding.radioButtonsGroup.check(R.id.expense_radio_button);
            setCategorySpinner(CategoryType.EXPENSE);
        } else if(t.getType() == CategoryType.INCOME) {
            binding.radioButtonsGroup.check(R.id.income_radio_button);
            setCategorySpinner(CategoryType.INCOME);
        }


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
        binding.updateTransactionButton.setOnClickListener(view -> {
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
                double oldAmount = t.getAmount();
                double oldUpdatedAmount = 0.0;
                Account oldAccount = t.getAccount();

                // The new account and the old account are the same
                if(acc.getName().equals(oldAccount.getName())) {
                    if(cat.getType() == CategoryType.EXPENSE) {
                        // Expense transaction
                        if(t.getType() == CategoryType.EXPENSE) {
                            // It was an expense transaction, now it is also expense
                            updatedAmount = acc.getAmount() + oldAmount;
                            updatedAmount -= amount;
                        } else if(t.getType() == CategoryType.INCOME) {
                            // It was an income transaction, now it is expense
                            updatedAmount = acc.getAmount() - oldAmount;
                            updatedAmount -= amount;
                        }
                    } else if(cat.getType() == CategoryType.INCOME) {
                        // Income transaction
                        if(t.getType() == CategoryType.EXPENSE) {
                            // It was an expense transaction, now it is income
                            updatedAmount = acc.getAmount() + oldAmount;
                            updatedAmount += amount;
                        } else if(t.getType() == CategoryType.INCOME) {
                            // It was an income transaction, now it is also income
                            updatedAmount = acc.getAmount() - oldAmount;
                            updatedAmount += amount;
                        }
                    }
                // The new account and the old account are different
                } else {
                    if(cat.getType() == CategoryType.EXPENSE) {
                        // Expense transaction
                        if(t.getType() == CategoryType.EXPENSE) {
                            // It was an expense transaction, now it is also expense
                            oldUpdatedAmount = oldAccount.getAmount() + oldAmount;
                            updatedAmount = acc.getAmount() - amount;
                        } else if(t.getType() == CategoryType.INCOME) {
                            // It was an income transaction, now it is expense
                            oldUpdatedAmount = oldAccount.getAmount() - oldAmount;
                            updatedAmount = acc.getAmount() - amount;
                        }
                    } else if(cat.getType() == CategoryType.INCOME) {
                        // Income transaction
                        if(t.getType() == CategoryType.EXPENSE) {
                            // It was an expense transaction, now it is income
                            oldUpdatedAmount = oldAccount.getAmount() + oldAmount;
                            updatedAmount = acc.getAmount() + amount;
                        } else if(t.getType() == CategoryType.INCOME) {
                            // It was an income transaction, now it is also income
                            oldUpdatedAmount = oldAccount.getAmount() - oldAmount;
                            updatedAmount = acc.getAmount() + amount;
                        }
                    }
                }

                // If the balance of the account is negative the transaction cannot be made
                if(updatedAmount < 0) {
                    binding.transactionAmountText.setTextColor(
                            App.getContext().getResources().getColor(R.color.red));
                    Snackbar.make(view, R.string.account_error, Snackbar.LENGTH_LONG).show();
                // The old account now does not have enough money
                } else if(oldUpdatedAmount < 0) {
                    binding.transactionAmountText.setTextColor(
                            App.getContext().getResources().getColor(R.color.red));
                    Snackbar.make(view, R.string.old_account_error, Snackbar.LENGTH_LONG).show();
                } else {
                    acc.setAmount(updatedAmount);
                    oldAccount.setAmount(oldUpdatedAmount);
                    appDAO.updateAccount(oldAccount);
                    appDAO.updateAccount(acc);

                    // Update transaction
                    t.setAmount(amount);
                    t.setAccount(acc);
                    t.setType(cat.getType());
                    t.setCategory(cat);
                    t.setNote(note);
                    appDAO.updateTransaction(t);

                    Navigation.findNavController(view)
                            .navigate(HomeEditTransactionFragmentDirections
                                    .actionHomeEditTransactionFragmentToHomeFragment());
                }
            }
        });

        binding.deleteTransactionButton.setOnClickListener(view -> {
            // Update account
            Account acc = t.getAccount();
            double amount = t.getAmount();
            double accountAmount = acc.getAmount();

            if(t.getType() == CategoryType.EXPENSE) {
                accountAmount += amount;
            } else if(t.getType() == CategoryType.INCOME) {
                accountAmount -= amount;
            }

            acc.setAmount(accountAmount);
            appDAO.updateAccount(acc);
            appDAO.deleteTransaction(t);

            // Go back to home screen
            Navigation.findNavController(view)
                    .navigate(HomeEditTransactionFragmentDirections
                            .actionHomeEditTransactionFragmentToHomeFragment());
        });

        // Go back to home screen
        binding.backToHomeFab.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(HomeEditTransactionFragmentDirections
                                .actionHomeEditTransactionFragmentToHomeFragment())
        );
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
