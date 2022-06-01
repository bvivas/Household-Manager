package es.uam.eps.tfg.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;
import es.uam.eps.tfg.app.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false
        );

        // Get list of transactions to set the adapter
        List<Transaction> list = appDAO.getTransactions();
        TransactionAdapter adapter = new TransactionAdapter(list);
        binding.transactionListRecyclerView.setAdapter(adapter);

        // Filter by expenses
        binding.expensesButton.setOnClickListener(view -> {
            // Set buttons colors
            view.setBackgroundColor(App.getContext().getResources().getColor(R.color.blue_highlight));
            binding.allButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));
            binding.incomesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));

            List<Transaction> expensesList = new ArrayList<>();
            for(Transaction t : list) {
                if(t.getCategory().getType() == CategoryType.EXPENSE) {
                    expensesList.add(t);
                }
            }

            TransactionAdapter expensesAdapter = new TransactionAdapter(expensesList);
            binding.transactionListRecyclerView.setAdapter(expensesAdapter);
        });

        // Filter by incomes
        binding.incomesButton.setOnClickListener(view -> {
            // Set buttons colors
            view.setBackgroundColor(App.getContext().getResources().getColor(R.color.blue_highlight));
            binding.allButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));
            binding.expensesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));

            List<Transaction> incomesList = new ArrayList<>();
            for(Transaction t : list) {
                if(t.getCategory().getType() == CategoryType.INCOME) {
                    incomesList.add(t);
                }
            }

            TransactionAdapter incomesAdapter = new TransactionAdapter(incomesList);
            binding.transactionListRecyclerView.setAdapter(incomesAdapter);
        });

        // Display all transactions
        binding.allButton.setOnClickListener(view -> {
            // Set buttons colors
            view.setBackgroundColor(App.getContext().getResources().getColor(R.color.blue_highlight));
            binding.incomesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));
            binding.expensesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));

            binding.transactionListRecyclerView.setAdapter(adapter);
        });

        return binding.getRoot();
    }
}
