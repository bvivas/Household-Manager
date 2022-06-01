package es.uam.eps.tfg.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import es.uam.eps.tfg.app.databinding.FragmentPlanningBinding;


public class PlanningFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentPlanningBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_planning,
                container,
                false
        );

        return binding.getRoot();
    }
}
