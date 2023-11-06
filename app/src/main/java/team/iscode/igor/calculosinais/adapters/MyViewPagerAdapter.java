package team.iscode.igor.calculosinais.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import team.iscode.igor.calculosinais.fragments.SinalEntrada_fragment;
import team.iscode.igor.calculosinais.fragments.SinalSaida_fragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    private SinalEntrada_fragment sinalEntradaFragment;
    private SinalSaida_fragment sinalSaidaFragment;
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        sinalEntradaFragment = new SinalEntrada_fragment(); // Inicialize o fragmento
        sinalSaidaFragment = new SinalSaida_fragment(); // Inicialize o fragmento
    }

    public void setSinalEntradaFragmentArguments(Bundle arguments) {
        sinalEntradaFragment.setArguments(arguments);
    }

    public void setSinalSaidaFragmentArguments(Bundle arguments) {
        sinalSaidaFragment.setArguments(arguments);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return sinalSaidaFragment;
        }
        return sinalEntradaFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
