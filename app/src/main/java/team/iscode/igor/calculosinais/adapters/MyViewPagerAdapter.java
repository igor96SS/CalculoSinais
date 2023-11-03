package team.iscode.igor.calculosinais.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import team.iscode.igor.calculosinais.fragments.SinalEntrada_fragment;
import team.iscode.igor.calculosinais.fragments.SinalSaida_fragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new SinalSaida_fragment();
        }
        return new SinalEntrada_fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
