package es.uam.eps.tfg.app;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uam.eps.tfg.app.databinding.ListItemTransactionBinding;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) {
        this.list = list;
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {
        private ListItemTransactionBinding binding;

        public TransactionHolder(ListItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Transaction transaction) {

            if(transaction.getCategory().getType() == CategoryType.EXPENSE) {
                binding.listTransactionAmount.setTextColor(App.getContext().getResources().getColor(R.color.red));
            } else if(transaction.getCategory().getType() == CategoryType.INCOME) {
                binding.listTransactionAmount.setTextColor(App.getContext().getResources().getColor(R.color.green));
            }
            binding.setTransaction(transaction);
            binding.executePendingBindings();

            binding.transactionWidget.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(HomeFragmentDirections
                                .actionHomeFragmentToHomeEditTransactionFragment(transaction.getId()));
            });
        }
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemTransactionBinding itemBinding = ListItemTransactionBinding.inflate(
                inflater,
                parent,
                false);
        return new TransactionHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
