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

import es.uam.eps.tfg.app.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentStartBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_start,
                container,
                false
                );

        BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
        menu.setVisibility(View.INVISIBLE);

        // Start the home fragment and the bottom navigation bar
        binding.startScreen.setOnClickListener(view -> {
            menu.setVisibility(View.VISIBLE);

            Navigation.findNavController(view)
                    .navigate(StartFragmentDirections
                            .actionStartFragmentToHomeFragment());
        });

        return binding.getRoot();
    }
}
