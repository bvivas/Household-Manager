package es.uam.eps.tfg.app;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uam.eps.tfg.app.databinding.ListItemAccountBinding;


public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    private List<Account> list;

    public AccountAdapter(List<Account> list) {
        this.list = list;
    }

    public class AccountHolder extends RecyclerView.ViewHolder {
        private ListItemAccountBinding binding;

        public AccountHolder(ListItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Account account) {
            binding.setAccount(account);
            binding.executePendingBindings();
            binding.accountWidget.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(SettingsAccountsFragmentDirections
                                .actionSettingsAccountsFragmentToSettingsAccountInfoFragment(account.getId()));
            });
        }
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemAccountBinding itemBinding = ListItemAccountBinding.inflate(
                inflater,
                parent,
                false);
        return new AccountHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
